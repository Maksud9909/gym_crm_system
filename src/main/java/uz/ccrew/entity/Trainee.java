package uz.ccrew.entity;

import uz.ccrew.entity.base.BaseEntity;

import lombok.*;
import jakarta.persistence.*;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.time.LocalDate;
import java.util.ArrayList;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true, exclude = {"trainers", "training"})
@Entity
@Table(name = "trainees")
public class Trainee extends BaseEntity {
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
    @Column(name = "address")
    private String address;
    @ManyToMany(mappedBy = "trainees", fetch = FetchType.LAZY)
    private List<Trainer> trainers = new ArrayList<>();
    @OneToMany(mappedBy = "trainee", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Training> training = new ArrayList<>();
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", unique = true)
    private User user;
}
