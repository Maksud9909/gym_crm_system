package uz.ccrew.entity;

public class Trainer extends User {
    private String specialization;

    public Trainer() {

    }

    public Trainer(Long id, String firstName, String lastName, String username, String password, Boolean isActive, String specialization) {
        super(id, firstName, lastName, username, password, isActive);
        this.specialization = specialization;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
}
