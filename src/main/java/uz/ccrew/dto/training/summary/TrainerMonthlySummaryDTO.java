package uz.ccrew.dto.training.summary;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"trainerUsername", "trainerFirstName", "trainerLastName", "isActive", "years"})
public class TrainerMonthlySummaryDTO {
    private String trainerUsername;
    private String trainerFirstName;
    private String trainerLastName;
    @JsonProperty("isActive")
    private boolean isActive;
    List<YearsDTO> years;
}
