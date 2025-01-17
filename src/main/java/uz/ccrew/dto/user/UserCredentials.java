package uz.ccrew.dto.user;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCredentials {
    private String username;
    private String password;
}
