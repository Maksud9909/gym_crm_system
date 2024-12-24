package uz.ccrew.entity;

import uz.ccrew.entity.base.BaseEntity;

import lombok.Setter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "training_types")
public class TrainingType extends BaseEntity {
    @Column(name = "training_type_name", nullable = false, unique = true)
    private String trainingTypeName;
}
