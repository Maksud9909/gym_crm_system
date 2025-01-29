package uz.ccrew.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import uz.ccrew.entity.base.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import org.hibernate.annotations.Immutable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "training_types")
@Immutable
@SuperBuilder
public class TrainingType extends BaseEntity {
    @Column(name = "training_type_name", nullable = false, unique = true)
    private String trainingTypeName;
}
