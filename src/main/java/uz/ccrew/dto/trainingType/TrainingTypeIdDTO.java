package uz.ccrew.dto.trainingType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainingTypeIdDTO {
    private Long id;
    private String trainingTypeName;
}
