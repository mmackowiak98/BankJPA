package pl.bankproject;

import javax.persistence.*;
import java.util.Objects;
@Entity //obowiązkowa adnotacja
@Table(name = "USERS") //pointing to which table in DB we want to save data -- no need if class name is same as table name in DB

public class Client {
    @Id //some kind of a primary key
    @GeneratedValue // auto increment field
    @Column(name="USER_ID") // says that our ID field  relate to user_id column in DB
    private Long id;
    @Column(name="FIRST_NAME")
    private String name;
    @Column(name="MAIL")
    private String email;
    @Transient //skips field
    private double balance;

    public Client() {
    }

    public Client(String name, String email, double balance) {
        this.name = name;
        this.email = email;
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Double.compare(client.balance, balance) == 0
                && Objects.equals(name, client.name)
                && Objects.equals(email, client.email);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email, balance);
    }

    @Override
    public String toString() {
        return "Client{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", balance=" + balance +
                '}';
    }


}
