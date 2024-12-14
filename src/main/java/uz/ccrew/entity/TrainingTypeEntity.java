package uz.ccrew.entity;

import uz.ccrew.entity.base.BaseEntity;

import lombok.Setter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "training_type_entity")
public class TrainingTypeEntity extends BaseEntity {
    @Enumerated(EnumType.STRING)
    @Column(name = "training_type", nullable = false, unique = true)
    private TrainingType trainingType;
}
