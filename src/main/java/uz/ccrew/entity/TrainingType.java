package uz.ccrew.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.ccrew.entity.base.BaseEntity;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "training_types")
public class TrainingType extends BaseEntity {
    @Column(name = "training_type_name", nullable = false, unique = true)
    private String trainingTypeName;
}
