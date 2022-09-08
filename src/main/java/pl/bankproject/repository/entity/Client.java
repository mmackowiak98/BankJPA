package pl.bankproject.repository.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
@Entity //obowiązkowa adnotacja - tworzy obiekt gotowy do mapowania go na baze danych
@Table(name = "users")
//pointing to which table in DB we want to save data -- no need if class name is same as table name in DB
@Data
@NoArgsConstructor
public class Client {


    @Id //some kind of a primary key
    @GeneratedValue // auto increment field
    @Column(name="USER_ID") // says that our ID field  relate to user_id column in DB
    private Long id;

    @Column(name="FIRST_NAME")
    private String name;

    @Column(name="MAIL")
    private String email;

    @OneToMany(fetch = FetchType.EAGER, cascade =  CascadeType.ALL)
    @JoinColumn(name="user_id")
    private List<Account> accounts;

    public Client(String name, String email, List<Account> accounts) {
        this.name = name;
        this.email = email;
        this.accounts = accounts;
    }

    public double getBalance() {
        if(!accounts.isEmpty()){
            return accounts.get(0).getBalance();
        }
        return 0;
    }


    public void setBalance(double newBalance) {
        if(!accounts.isEmpty()){
            accounts.get(0).setBalance(newBalance);
        }
    }
}
