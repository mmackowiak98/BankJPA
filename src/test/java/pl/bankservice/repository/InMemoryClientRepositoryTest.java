//package pl.bankservice.repository;
//
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import pl.bankproject.repository.entity.Client;
//import pl.bankproject.exceptions.ClientAlreadyExistsException;
//import pl.bankproject.exceptions.NoSuchClientInRepositoryException;
//import pl.bankproject.exceptions.WrongClientDetailsException;
//import pl.bankproject.repository.InMemory.InMemoryClientRepository;
//
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//public class InMemoryClientRepositoryTest {
//
//    private InMemoryClientRepository repository;
//    private ArrayList<Client> clients;
//
//    @BeforeEach
//    public void setUp() {
//        clients = new ArrayList<>();
//        repository = new InMemoryClientRepository(clients);
//    }
//
//    @Test
//    public void verifyIfClientIsAddingCorrectlyToTheRepository() {
//        //given
//        final Client kamil = new Client("Kamil", "kamil@gmail.com", 100);
//        final Client expectedClient = new Client("Kamil", "kamil@gmail.com", 100);
//        //when
//        repository.save(kamil);
//        //then
//        final Client actualClient = clients.stream().findFirst().get();
//        assertEquals(actualClient, expectedClient);
//
//    }
//
//    @Test
//    public void save_saveClientWithSameEmail_ClientAlreadyExistsException() {
//        final Client kamil = new Client("Kamil", "kamIl@gmail.com", 100);
//        final Client maciek = new Client("Maciek", "kAmil@gmail.com", 100);
//        repository.save(kamil);
//        Assertions.assertThrows(ClientAlreadyExistsException.class, () -> repository.save(maciek));
//    }
//
//    @Test
//    public void save_saveSameClient_ClientAlreadyExistsException() {
//        final Client kamil = new Client("Kamil", "kamil@gmail.com", 100);
//        repository.save(kamil);
//        Assertions.assertThrows(ClientAlreadyExistsException.class, () -> repository.save(kamil));
//    }
//
//    @Test
//    public void save_saveClientWithCapitalLettersEmail_returnEmailWithLowerCase() {
//        final Client kamil = new Client("Kamil", "K@K.pl", 100);
//        repository.save(kamil);
//        assertEquals(repository.findByEmail("K@k.pl").getEmail(), "k@k.pl");
//    }
//
//    @Test
//    public void save_saveClientWithNulls_WrongClientDetailsException() {
//        final Client client = new Client(null, null, 100);
//
//        Assertions.assertThrows(WrongClientDetailsException.class, () -> repository.save(client));
//
//    }
//
//    @Test
//    public void findByEmail_findByEmailNoCaseSensitive_clientFound() {
//        final Client kamil = new Client("Kamil", "k@k.pl", 100);
//        clients.add(kamil);
//        final Client byEmail = repository.findByEmail("K@K.pl");
//        assertEquals( kamil, byEmail );
//    }
//
//    @Test
//    public void findByEmail_noClientFound_NoSuchClientInRepositoryException() {
//        Assertions.assertThrows(NoSuchClientInRepositoryException.class, () -> repository.findByEmail("k@k.pl"));
//    }
//}
