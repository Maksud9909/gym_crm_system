package uz.ccrew.dto.user;

import lombok.*;
import jakarta.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCredentials {
    @NotBlank(message = "Username should not be empty")
    private String username;
    @NotBlank(message = "Password should not be empty")
    private String password;
}
