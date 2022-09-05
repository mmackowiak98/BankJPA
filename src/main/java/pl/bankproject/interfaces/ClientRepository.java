package pl.bankproject.interfaces;

import pl.bankproject.repository.entity.Client;

public interface ClientRepository {
    void save(Client client);

    void remove(Client client);

    void listOfClient();

    Client findByEmail(String email);


}
