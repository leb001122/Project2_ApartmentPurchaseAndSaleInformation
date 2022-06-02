package persistence.domain;

import lombok.*;

@Getter
@Setter
@Builder
public class User {
    private String id;
    private String password;
    private String name;
    private String email;
}
