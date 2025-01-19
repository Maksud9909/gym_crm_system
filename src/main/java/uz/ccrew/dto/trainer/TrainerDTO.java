package uz.ccrew.dto.trainer;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainerDTO {
    private String username;
    private String firstName;
    private String lastName;
    private String trainingTypeName;
}
