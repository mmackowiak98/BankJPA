package pl.bankservice.service;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.bankproject.Client;
import pl.bankproject.exceptions.NoSuchClientInRepositoryException;
import pl.bankproject.exceptions.NoSufficientFundsException;
import pl.bankproject.exceptions.WrongClientDetailsException;
import pl.bankproject.repository.InMemoryClientRepository;
import pl.bankproject.service.BankService;

import java.util.ArrayList;
import java.util.List;


public class BankServiceTest {
    private BankService service;
    private ArrayList<Client> clients;

    @BeforeEach
    public void setUp() {
        clients = new ArrayList<>();
        InMemoryClientRepository clientRepository = new InMemoryClientRepository(clients);
        service = new BankService(clientRepository);

    }


    @Test
    public void transfer_allParamsOk_fundsTransferred() {
        //given
        double amount = 100;
        String emailFrom = "m@m.pl";
        String emailTo = "k@k.pl";
        final Client clientFrom = new Client("Maciek", emailFrom, 3000);
        final Client clientTo = new Client("Kamil", emailTo, 1000);
        clients.add(clientFrom);
        clients.add(clientTo);
        //when
        service.transfer(emailFrom, emailTo, amount);
        //then
        final Client actualFromClient = service.findByEmail(emailFrom);
        final Client actualToClient = service.findByEmail(emailTo);
        final Client expectedClientFrom = new Client("Maciek", emailFrom, 2900);
        final Client expectedClientTo = new Client("Kamil", emailTo, 1100);

        final SoftAssertions softAssertions = new SoftAssertions();
        softAssertions
                .assertThat(expectedClientFrom)
                .isEqualTo(actualFromClient);
        softAssertions
                .assertThat(expectedClientTo)
                .isEqualTo(actualToClient);
        softAssertions.assertAll();

    }

    @Test
    public void transfer_allFunds_fundsTransferred() {
        double amount = 3000;
        String emailFrom = "m@m.pl";
        String emailTo = "k@k.pl";
        final Client clientFrom = new Client("Maciek", emailFrom, 3000);
        final Client clientTo = new Client("Kamil", emailTo, 1000);
        clients.add(clientFrom);
        clients.add(clientTo);
        //when
        service.transfer(emailFrom, emailTo, amount);
        Client actualFromClient = service.findByEmail(emailFrom);
        Client actualToClient = service.findByEmail(emailTo);
        Client expectedFromClient = new Client("Maciek", emailFrom, 0);
        Client expectedToClient = new Client("Kamil", emailTo, 4000);

        final SoftAssertions softAssertions = new SoftAssertions();

        softAssertions
                .assertThat(expectedFromClient)
                .isEqualTo(actualFromClient);
        softAssertions
                .assertThat(expectedToClient)
                .isEqualTo(actualToClient);
        softAssertions.assertAll();
    }

    @Test
    public void transfer_notEnoughFunds_NoSufficientFundsException() {
        //given
        double amount = 3000;
        String emailFrom = "m@m.pl";
        String emailTo = "k@k.pl";
        final Client clientFrom = new Client("Maciek", emailFrom, 2000);
        final Client clientTo = new Client("Kamil", emailTo, 1000);
        clients.add(clientFrom);
        clients.add(clientTo);
        //when - then
        Assertions.assertThrows(NoSufficientFundsException.class, () -> service.transfer(emailFrom, emailTo, amount));
    }

    @Test
    public void transfer_negativeAmount_IllegalArgumentException() {
        //given
        double amount = -100;
        String emailFrom = "m@m.pl";
        String emailTo = "k@k.pl";
        final Client clientFrom = new Client("Maciek", emailFrom, 2000);
        final Client clientTo = new Client("Kamil", emailTo, 1000);
        clients.add(clientFrom);
        clients.add(clientTo);
        //when - then
        Assertions.assertThrows(IllegalArgumentException.class, () -> service.transfer(emailFrom, emailTo, amount));
    }

    @Test
    public void transfer_transferToMyself_WrongClientDetailsException(){
        //given
        String email = "m@m.pl";
        final Client client = new Client("Maciek", email, 2000);
        clients.add(client);
        Assertions.assertThrows(WrongClientDetailsException.class, () -> service.transfer(email,email,100));
    }

    @Test
    public void deleteClient_deleteClientWithNullEmail_IllegalArgumentException(){
        final Client maciek = new Client("Maciek", "m@m.pl", 2000);
        clients.add(maciek);
        Assertions.assertThrows(IllegalArgumentException.class, () -> service.deleteClient(null));
    }

    @Test
    public void deleteClient_deleteClientWithWrongEmail_NoSuchClientInRepositoryException(){
        final Client maciek = new Client("Maciek", "m@m.pl", 2000);
        clients.add(maciek);
        Assertions.assertThrows(NoSuchClientInRepositoryException.class, () -> service.deleteClient("k@k.pl"));

    }

    @Test
    public void deleteClient_deleteClientWithPositiveBalance_IllegalArgumentException(){
        final Client maciek = new Client("Maciek", "m@m.pl", 2000);
        clients.add(maciek);
        Assertions.assertThrows(IllegalArgumentException.class, () -> service.deleteClient("m@m.pl"));
    }

    @Test
    public void deleteClient_allParamsOk_clientDeleted(){
        final Client maciek = new Client("Maciek", "m@m.pl", 0);
        clients.add(maciek);
        service.deleteClient("m@m.pl");
        final boolean shouldBeFalse = clients.stream()
                .anyMatch(client -> client.getEmail().equals("m@m.pl"));
        Assertions.assertFalse(shouldBeFalse);
    }

    @Test
    public void withdraw_validAmount_balanceChangedCorrectly(){
        //when
        final String email = "m@m.pl";
        final Client client= new Client("Maciek", email, 1000);
        clients.add(client);
        //when
        service.withdraw(email, 100);
        //then
        Client expectedClient = new Client("Maciek", email, 900);
        final Client actualClient = clients.get(0);
        Assertions.assertEquals(expectedClient,actualClient);
    }

    @Test
    public void withdraw_correctFloatingPointAmount_balanceChangedCorrectly(){
        //when
        final String email = "m@m.pl";
        final Client client= new Client("Maciek", email, 1000);
        clients.add(client);
        //when
        service.withdraw(email, 100.5);
        //then
        Client expectedClient = new Client("Maciek", email, 899.5);
        final Client actualClient = clients.get(0);
        Assertions.assertEquals(expectedClient,actualClient);
    }

    @Test
    public void withdraw_negativeAmount_IllegalArgumentException(){

        final String email = "m@m.pl";
        final Client client= new Client("Maciek", email, 1000);
        clients.add(client);
        Assertions.assertThrows(IllegalArgumentException.class, () -> service.withdraw(email, -100));
    }

    @Test
    public void withdraw_amountGreaterThanBalance_NoSufficientFundsException(){

        final String email = "m@m.pl";
        final Client client= new Client("Maciek", email, 1000);
        clients.add(client);
        Assertions.assertThrows(NoSufficientFundsException.class, () -> service.withdraw(email, 1100));
    }

    @Test
    public void withdraw_upperCaseEmail_balanceChangedCorrectly(){
        //when
        final String email = "M@M.pl";
        final Client client= new Client("Maciek", "m@m.pl", 1000);
        clients.add(client);
        //when
        service.withdraw(email, 100);
        //then
        Client expectedClient = new Client("Maciek", "m@m.pl", 900);
        Assertions.assertTrue(clients.contains(expectedClient));
    }

    @Test
    public void withdraw_nullEmailProvided_IllegalArgumentException(){

        final String email = null;
        Assertions.assertThrows(IllegalArgumentException.class, () -> service.withdraw(email, 100));
    }







}
