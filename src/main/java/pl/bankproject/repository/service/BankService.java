package pl.bankproject.repository.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.bankproject.repository.exceptions.NoSuchClientInRepositoryException;
import pl.bankproject.repository.exceptions.NoSufficientFundsException;
import pl.bankproject.repository.exceptions.WrongClientDetailsException;
import pl.bankproject.repository.interfaces.ClientSpringJpaRepository;
import pl.bankproject.repository.entity.Client;

import java.util.Objects;
@Service
public class BankService {

    final private ClientSpringJpaRepository clientRepository;
    @Autowired
    public BankService(ClientSpringJpaRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public void save(Client client) {
        clientRepository.save(client);
    }

    public Client findByEmail(String email) {

        return clientRepository.findByEmail(email);
    }

    public void transfer(String fromEmail, String toEmail, double amount) {
        validateAmount(amount);
        final Client from = clientRepository.findByEmail(fromEmail);
        final Client to = clientRepository.findByEmail(toEmail);
        if (from.getEmail().equals(to.getEmail())) {
            throw new WrongClientDetailsException("You can't transfer money to yourself");
        }
        if (from.getBalance() - amount >= 0) {
            from.setBalance(from.getBalance() - amount);
            System.out.println("You transfer " + amount + " from your account");
            System.out.println("Your available amount right now is: " + from.getBalance());
        } else {
            throw new NoSufficientFundsException("Not enough amount on your account to transfer");
        }
        Objects.requireNonNull(to).setBalance(to.getBalance() + amount);
        clientRepository.save(from);
        clientRepository.save(to);

    }

    public boolean deleteClient(String email) {
        if (email == null) {
            throw new IllegalArgumentException();
        }
        if(clientRepository.findByEmail(email)==null){
            throw new NoSuchClientInRepositoryException("No such client in repository");
        }
        if(clientRepository.findByEmail(email).getBalance()>0){
            throw new IllegalArgumentException();
        }
        final Client clientToDelete = clientRepository.findByEmail(email);
        clientRepository.deleteByEmail(clientToDelete);
        return true;

    }


    public void showBalance(String clientEmail) {
        System.out.println("Your balance is: " + clientRepository.findByEmail(clientEmail).getBalance());
    }

//    public void showClients() {
//        clientRepository.listOfClients();
//    }


    public void withdraw(String email, double amount) {
        validateAmount(amount);
        if(Objects.isNull(email)){
            throw new IllegalArgumentException();
        }
        final String lowerCaseEmail = email.toLowerCase();
        final Client withdrawClient = clientRepository.findByEmail(lowerCaseEmail);

        if (amount > withdrawClient.getBalance()) {
            throw new NoSufficientFundsException("Amount is greater than your balance");
        }

        withdrawClient.setBalance(withdrawClient.getBalance() - amount);
        clientRepository.save(withdrawClient);

    }

    private void validateAmount(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
    }
}
