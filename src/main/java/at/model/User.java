package at.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class User {
    @Getter
    private String alias;
    @Getter
    private String login;
    @Getter
    private String password;
    @Getter
    private String role;

}
