package pl.bankproject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.bankproject.repository.entity.Account;
import pl.bankproject.repository.entity.Client;
import pl.bankproject.repository.service.BankService;

import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class Main implements CommandLineRunner {
    private final BankService bankService;

    @Autowired
    public Main(BankService bankService) {
        this.bankService = bankService;
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) throws Exception {



        try (final Scanner scanner = new Scanner(System.in)) {

            while (true) {
                System.out.println("1.Add client");
                System.out.println("2.Find client by email");
                System.out.println("3.Transfer money");
                System.out.println("4.Show balance");
                System.out.println("5.Delete client");
                System.out.println("6.Show all clients");
                final String next = scanner.next();


                if (next.equals("1")) {
                    addUser(scanner);
                }
                if (next.equals("2")) {
                    printUser(scanner);
                }
                if (next.equals("3")) {
                    transferMoney(scanner);
                }
                if (next.equals("4")) {
                    showBalance(scanner);
                }
                if (next.equals("5")) {
                    removeClient(scanner);
                }
                if (next.equals("6")) {
//                    showClient();
                }


            }
        }

    }

//    private void showClient() {
//        bankService.showClients();
//    }

    private void removeClient(Scanner scanner) {
        System.out.println("Email of client to remove: ");
        final String clientEmail = scanner.next();
        bankService.deleteClient(clientEmail);
    }

    private void showBalance(Scanner scanner) {
        System.out.println("Which email balance");
        final String clientEmail = scanner.next();
        bankService.showBalance(clientEmail);

    }

    private void printUser(Scanner scanner) {
        System.out.println("Client email: ");
        final String clientEmail = scanner.next();
        System.out.println(bankService.findByEmail(clientEmail).toString());
    }

    private void addUser(Scanner scanner) {
        System.out.println("Set client name");
        final String clientName = scanner.next();
        System.out.println("Set client email");
        final String clientEmail = scanner.next();
        System.out.println("Set client account balance");
        final int clientBalance = scanner.nextInt();
        final Account account = new Account(clientBalance, "PLN");
        final List<Account> accountsList = List.of(account);
        bankService.save(new Client(clientName, clientEmail, accountsList));
    }

    private void transferMoney(Scanner scanner) {
        System.out.println("From which email: ");
        final String fromEmail = scanner.next();
        System.out.println("To which email");
        final String toEmail = scanner.next();
        System.out.println("How much to transfer");
        final double amount = scanner.nextDouble();
        bankService.transfer(fromEmail, toEmail, amount);

    }


}
