package at.model;

import jdk.nashorn.internal.objects.annotations.Constructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class User {
    private String alias;
    private String login;
    private String password;
    private String role;
    private String name;

    public User(String alias, String login, String password, String role) {
        this.alias = alias;
        this.login = login;
        this.password = password;
        this.role = role;
    }

    public User() {
    }
}
