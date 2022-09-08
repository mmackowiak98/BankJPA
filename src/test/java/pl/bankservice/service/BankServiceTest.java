package pl.bankservice.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.bankproject.exceptions.NoSuchClientInRepositoryException;
import pl.bankproject.exceptions.NoSufficientFundsException;
import pl.bankproject.exceptions.WrongClientDetailsException;
import pl.bankproject.interfaces.ClientSpringJpaRepository;
import pl.bankproject.repository.entity.Account;
import pl.bankproject.repository.entity.Client;
import pl.bankproject.service.BankService;

import java.util.Collections;

import static org.mockito.Mockito.*;


public class BankServiceTest {
    private BankService service;
    private ClientSpringJpaRepository repository;


    @BeforeEach
    public void setUp() {
        repository = mock(ClientSpringJpaRepository.class);
        service = new BankService(repository);

    }


    @Test
    public void transfer_allParamsOk_fundsTransferred() {
        //given
        double amount = 100;
        String emailFrom = "m@m.pl";
        String emailTo = "k@k.pl";

        final Client clientFrom = new Client("Maciek", emailFrom, Collections.singletonList(new Account(1000, "PLN")));
        final Client clientTo = new Client("Kamil", emailTo, Collections.singletonList(new Account(500, "PLN")));
        when(repository.findByEmail(emailFrom)).thenReturn(clientFrom);
        when(repository.findByEmail(emailTo)).thenReturn(clientTo);
        //when
        service.transfer(emailFrom, emailTo, amount);
        //then
        final Client expectedClientFrom = new Client("Maciek",
                emailFrom,
                Collections.singletonList(new Account(900, "PLN")));

        final Client expectedClientTo = new Client("Kamil",
                emailTo,
                Collections.singletonList(new Account(600, "PLN")));

        verify(repository).save(expectedClientFrom);
        verify(repository).save(expectedClientTo);
    }

    @Test
    public void transfer_allFunds_fundsTransferred() {
        double amount = 3000;
        String emailFrom = "m@m.pl";
        String emailTo = "k@k.pl";
        final Client clientFrom = new Client("Maciek", emailFrom, Collections.singletonList(new Account(3000, "PLN")));
        final Client clientTo = new Client("Kamil", emailTo, Collections.singletonList(new Account(500, "PLN")));
        when(repository.findByEmail(emailFrom)).thenReturn(clientFrom);
        when(repository.findByEmail(emailTo)).thenReturn(clientTo);
        //when
        service.transfer(emailFrom, emailTo, amount);
        //then
        Client expectedFromClient = new Client("Maciek", emailFrom, Collections.singletonList(new Account(0, "PLN")));
        Client expectedToClient = new Client("Kamil", emailTo, Collections.singletonList(new Account(3500, "PLN")));
        verify(repository).save(expectedFromClient);
        verify(repository).save(expectedToClient);

    }

    @Test
    public void transfer_notEnoughFunds_NoSufficientFundsException() {
        //given
        double amount = 3000;
        String emailFrom = "m@m.pl";
        String emailTo = "k@k.pl";
        final Client clientFrom = new Client("Maciek", emailFrom, Collections.singletonList(new Account(1000, "PLN")));
        final Client clientTo = new Client("Kamil", emailTo, Collections.singletonList(new Account(3000, "PLN")));
        when(repository.findByEmail(emailFrom)).thenReturn(clientFrom);
        when(repository.findByEmail(emailTo)).thenReturn(clientTo);
        //when - then
        Assertions.assertThrows(NoSufficientFundsException.class, () -> service.transfer(emailFrom, emailTo, amount));
    }

    @Test
    public void transfer_negativeAmount_IllegalArgumentException() {
        //given
        double amount = -100;
        String emailFrom = "m@m.pl";
        String emailTo = "k@k.pl";
        //when - then
        Assertions.assertThrows(IllegalArgumentException.class, () -> service.transfer(emailFrom, emailTo, amount));
    }

    @Test
    public void transfer_transferToMyself_WrongClientDetailsException(){
        //given
        String email = "m@m.pl";
        final Client client = new Client("Maciek", email, Collections.singletonList(new Account(1000, "PLN")));
        when(repository.findByEmail(email)).thenReturn(client);
        Assertions.assertThrows(WrongClientDetailsException.class, () -> service.transfer(email,email,100));
    }

    @Test
    public void deleteClient_deleteClientWithNullEmail_IllegalArgumentException(){
        String email = "m@m.pl";
        final Client client = new Client("Maciek", email, Collections.singletonList(new Account(1000, "PLN")));
        when(repository.findByEmail(email)).thenReturn(client);
        Assertions.assertThrows(IllegalArgumentException.class, () -> service.deleteClient(null));
    }

    @Test
    public void deleteClient_deleteClientWithWrongEmail_NoSuchClientInRepositoryException(){
        String email = "m@m.pl";
        final Client client = new Client("Maciek", email, Collections.singletonList(new Account(1000, "PLN")));
        when(repository.findByEmail(email)).thenReturn(client);
        Assertions.assertThrows(NoSuchClientInRepositoryException.class, () -> service.deleteClient("k@k.pl"));

    }

    @Test
    public void deleteClient_deleteClientWithPositiveBalance_IllegalArgumentException(){
        String email = "m@m.pl";
        final Client client = new Client("Maciek", email, Collections.singletonList(new Account(1000, "PLN")));
        when(repository.findByEmail(email)).thenReturn(client);
        Assertions.assertThrows(IllegalArgumentException.class, () -> service.deleteClient("m@m.pl"));
    }

    @Test
    public void deleteClient_allParamsOk_clientDeleted(){
        String email = "m@m.pl";
        final Client client = new Client("Maciek", email, Collections.singletonList(new Account(0, "PLN")));
        when(repository.findByEmail(email)).thenReturn(client);
        final boolean b = service.deleteClient(email);
        Assertions.assertTrue(b);

    }

    @Test
    public void withdraw_validAmount_balanceChangedCorrectly(){
        //when
        String email = "m@m.pl";
        final Client client = new Client("Maciek", email, Collections.singletonList(new Account(1000, "PLN")));
        when(repository.findByEmail(email)).thenReturn(client);
        //when
        service.withdraw(email, 100);
        //then
        Client expectedClient = new Client("Maciek", email, Collections.singletonList(new Account(900, "PLN")));
        verify(repository).save(expectedClient);
    }

    @Test
    public void withdraw_correctFloatingPointAmount_balanceChangedCorrectly(){
        //when
        String email = "m@m.pl";
        final Client client = new Client("Maciek", email, Collections.singletonList(new Account(1000, "PLN")));
        when(repository.findByEmail(email)).thenReturn(client);
        //when
        service.withdraw(email, 100.5);
        //then
        Client expectedClient = new Client("Maciek", email, Collections.singletonList(new Account(899.5, "PLN")));
        verify(repository).save(expectedClient);
    }

    @Test
    public void withdraw_negativeAmount_IllegalArgumentException(){

        final String email = "m@m.pl";

        Assertions.assertThrows(IllegalArgumentException.class, () -> service.withdraw(email, -100));
    }

    @Test
    public void withdraw_amountGreaterThanBalance_NoSufficientFundsException(){

        //when
        String email = "m@m.pl";
        final Client client = new Client("Maciek", email, Collections.singletonList(new Account(1000, "PLN")));
        when(repository.findByEmail(email)).thenReturn(client);
        Assertions.assertThrows(NoSufficientFundsException.class, () -> service.withdraw(email, 1100));
    }



    @Test
    public void withdraw_nullEmailProvided_IllegalArgumentException(){

        final String email = null;
        Assertions.assertThrows(IllegalArgumentException.class, () -> service.withdraw(email, 100));
    }


}
