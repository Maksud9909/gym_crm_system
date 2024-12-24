package uz.ccrew.entity;

import uz.ccrew.entity.base.BaseEntity;

import lombok.Setter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.Table;
import jakarta.persistence.Entity;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "specializations")
public class Specialization extends BaseEntity {
    private String specialization;
}
