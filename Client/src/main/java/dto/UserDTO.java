package dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO implements Serializable {

    private String id;
    private String password;
    private String name;
    private String email;

}

