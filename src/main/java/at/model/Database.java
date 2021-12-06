package at.model;


import com.jcraft.jsch.*;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Getter
public class Database {

    private final String alias;
    private final String serviceName;
    private final String protocol;
    private final String tunnelHost;
    private final String tunnelUser;
    private final String tunnelPassword;
    private final String tunnelPort;
    private final String databaseLogin;
    private final String databasePass;
    private int assinged_port;
    private final Logger LOG = LogManager.getLogger(Database.class);


    public Database(String alias, String serviceName, String protocol, String host, String user, String userPass, String port, String databaseName, String databasePass) {
        this.alias = alias;
        this.serviceName = serviceName;
        this.protocol = protocol;
        this.tunnelHost = host;
        this.tunnelUser = user;
        this.tunnelPassword = userPass;
        this.tunnelPort = port;
        this.databaseLogin = databaseName;
        this.databasePass = databasePass;
    }

    public ArrayList<String> executeQuerry(String sqlQuery) {
        ArrayList<String> res = new ArrayList<>();
        Session session=null;
        JSch jsch=new JSch();
        String command = "psql -h localhost -p 5432 -U "+ databaseLogin+" -d"+alias+" -W -c \""+sqlQuery+" \"";
        LOG.info("Происходит попытка выполнить запрос в БД: "+sqlQuery.replaceAll("\\n"," " ));
        System.out.println("");
        try {
            session=jsch.getSession(tunnelUser,tunnelHost,Integer.parseInt(tunnelPort));
            session.setPassword(tunnelPassword);
            Properties config=new Properties();
            config.put("StrictHostKeyChecking","no");
            config.put("Compression", "yes");
            config.put("ConnectionAttempts","2");
            session.setConfig(config);
            session.connect();
            assinged_port=session.setPortForwardingL(5432,tunnelHost,Integer.parseInt(tunnelPort) );
        } catch (JSchException e) {
            e.printStackTrace();
        }
        try {
            Assertions.assertNotNull(session,"Сессия не была открыта, проверьте данные ssh");
            Channel channel=session.openChannel("exec");
            ((ChannelExec)channel).setCommand(command);
            OutputStream out=channel.getOutputStream();
            InputStream in=channel.getInputStream();
            channel.connect();
            out.write((databasePass+"\n").getBytes());
            out.flush();
            InputStream err=((ChannelExec) channel).getErrStream();

            StringBuilder resStr= new StringBuilder();
            byte[] tmp=new byte[1024];
            do{
                int i=err.read(tmp, 0, 1024);
                if(i<0)break;
                resStr.append(new String(tmp, 0, i));
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }while(err.available()>0);
            if(channel.isClosed()){
                System.out.println("exit-status: "+channel.getExitStatus());
            }

            res.addAll(Arrays.stream(resStr.toString().split("\n")).collect(Collectors.toList()));
            res.remove(0);
            String line;
            if(res.size()==0) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                while ((line = reader.readLine()) != null) {
                    res.add(line);
                }
            }
            channel.disconnect();
            session.disconnect();
        } catch (JSchException | IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    public void update(String sqlUpdate) {
        executeQuerry(sqlUpdate);
    }

    public ArrayList<Map<String, String>> select(String query) {
        ArrayList<String> resQuerry = executeQuerry(query);

        ArrayList<Map<String, String>> res = new ArrayList<>();

        Assertions.assertFalse(resQuerry.get(0).startsWith("ERROR"), "Запрос вернул ошибку: \n"+ String.join("\n", resQuerry)+"\n");
        Assertions.assertNotEquals("(0 rows)",resQuerry.get(resQuerry.size()-2),"Запрос вернул 0 строк");

        List<String> headers=new ArrayList<>();
        String firstStr=resQuerry.get(0).replaceAll(" ", "");
        while(firstStr.contains("|")){
            int ind=firstStr.indexOf("|")+1;
            StringBuilder substr= new StringBuilder();
            while(ind<firstStr.length() && !Arrays.asList(' ', '|').contains(firstStr.charAt(ind))) {
                substr.append(firstStr.charAt(ind));
                ind++;
            }
            String deleted= new StringBuilder().append("|").append(substr).toString();
            firstStr= firstStr.replace(deleted, "");
            headers.add(substr.toString());
        }
        headers.add(0,firstStr);
        for(int i=2;i<resQuerry.size()-2;i++){
            String str=resQuerry.get(i);
            Map<String,String> map= new HashMap<>();
            for(int j=1;str.contains("|");j++){
                int ind=str.indexOf("|")+1;
                StringBuilder substr= new StringBuilder();
                while(ind<str.length() && str.charAt(ind)!='|') {
                    substr.append(str.charAt(ind));
                    ind++;
                }
                String deleted= new StringBuilder().append("|").append(substr).toString();
                str= str.replaceFirst(Pattern.quote(deleted), "");
                if (substr.toString().trim().length()>0)
                    map.put(headers.get(j),substr.toString().trim());
                else
                    map.put(headers.get(j),null);
            }
            if (str.trim().length()>0)
                map.put(headers.get(0),str.trim());
            else
                map.put(headers.get(0),null);
            res.add(map);
        }

        LOG.info("Запрос выполнен успешно");
        return res;
    }

    public class SQLTimeoutError extends Error {
        SQLTimeoutError(String message) {
            super(message);
        }
    }

}
