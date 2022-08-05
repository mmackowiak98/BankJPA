package pl.bankproject.repository;

import pl.bankproject.Client;

import java.util.List;

public interface ClientRepository {
    void save(Client client);

    void remove(Client client);

    void listOfClient();

    Client findByEmail(String email);


}
