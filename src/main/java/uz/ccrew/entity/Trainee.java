package uz.ccrew.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Entity
@Table(name = "trainees")
public class Trainee extends User {
    @Column(name = "date_of_birth")
    private LocalDateTime dateOfBirth;
    @Column(name = "adress")
    private String address;
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;
    @ManyToMany(mappedBy = "trainers")
    private List<Trainee> trainees = new ArrayList<>();
    @OneToMany(mappedBy = "trainee_id")
    private Training training;
}
