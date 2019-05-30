package app.entity;

import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
public class Client implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false, name = "email")
    private String email;

    @Column(nullable = false, name = "address")
    private String address;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Person person;

    @Size(min = 8, max = 255, message = "Minimum cellphone length: 8 characters")
    @Column(nullable = false, name = "cellphone")
    private String cellphone;

    @Column(nullable = false)
    private int maxReserve;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public int getMaxReserve() {
        return maxReserve;
    }

    public void setMaxReserve(int maxReserve) {
        this.maxReserve = maxReserve;
    }
}
