package uz.ccrew.dto.auth;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotBlank;

@Getter
@Builder
@NotNull
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordDTO {
    @NotBlank(message = "Username should be not empty")
    private String username;
    @NotBlank(message = "Old password should be not empty")
    private String oldPassword;
    @NotBlank(message = "New password should be not empty")
    private String newPassword;
}
