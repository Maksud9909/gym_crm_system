package uz.ccrew.dto.summary;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class YearsDTO implements Serializable {
    private int year;
    List<MonthsDTO> months;
}
