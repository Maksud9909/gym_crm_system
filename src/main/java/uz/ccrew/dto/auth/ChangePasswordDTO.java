package uz.ccrew.dto.auth;

import lombok.Getter;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordDTO {
    private String username;
    private String oldPassword;
    private String newPassword;
}
