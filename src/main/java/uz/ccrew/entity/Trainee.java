package uz.ccrew.entity;

import java.util.Date;

public class Trainee extends User {
    private Date dateOfBirth;
    private String address;

    public Trainee(Long id, String firstName, String lastName, String username, String password, Boolean isActive, Date dateOfBirth, String address) {
        super(id, firstName, lastName, username, password, isActive);
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
