package uz.ccrew.entity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class Trainer extends User {
    private String specialization;

    public Trainer(Long id, String firstName, String lastName, String username, String password, Boolean isActive, String specialization) {
        super(id, firstName, lastName, username, password, isActive);
        this.specialization = specialization;
    }
}
