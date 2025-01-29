package uz.ccrew.dto.trainer;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonRootName(value = "Trainers")
public class TrainerDTO {
    private String username;
    private String firstName;
    private String lastName;
    private String trainingTypeName;
}
