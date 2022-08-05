package pl.bankproject.service;

import pl.bankproject.Client;
import pl.bankproject.exceptions.NoSufficientFundsException;
import pl.bankproject.exceptions.WrongClientDetailsException;
import pl.bankproject.repository.ClientRepository;

import java.util.Objects;

public class BankService {
    final private ClientRepository clientRepository;

    public BankService(ClientRepository clientRepository) {
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
        if (from != null && to != null) {
            if (from.getEmail().equals(to.getEmail())) {
                throw new WrongClientDetailsException("You can't transfer money to yourself");
            }
            if (from.getBalance() - amount >= 0) {
                from.setBalance(from.getBalance() - amount);
                System.out.println("You transfered " + amount + " from your account");
                System.out.println("Your available amount right now is: " + from.getBalance());
            } else {
                throw new NoSufficientFundsException("Not enough amount on your account to transfer");
            }
        }
        Objects.requireNonNull(to).setBalance(to.getBalance() + amount);

    }

    public void deleteClient(String email) {
        if (email == null) {
            throw new IllegalArgumentException();
        }
        final Client clientToDelete = clientRepository.findByEmail(email);
        clientRepository.remove(clientToDelete);

    }


    public void showBalance(String clientEmail) {
        System.out.println("Your balance is: " + clientRepository.findByEmail(clientEmail).getBalance());
    }

    public void showClients() {
        clientRepository.listOfClient();
    }


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

    }

    private void validateAmount(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
    }
}
