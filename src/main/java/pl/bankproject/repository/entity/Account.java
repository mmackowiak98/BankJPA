package pl.bankproject.repository.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name="accounts")
@Data
@NoArgsConstructor

public class Account  {
    @Id
    @GeneratedValue
    @Column(name="account_id")
    private long id;

    @Column(name="balance")
    private double balance;

    @Column(name="currency")
    private String currency;

    public Account(double balance, String currency) {
        this.balance = balance;
        this.currency = currency;
    }
}
