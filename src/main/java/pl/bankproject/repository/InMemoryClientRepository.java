package pl.bankproject.repository;

import pl.bankproject.Client;
import pl.bankproject.exceptions.ClientAlreadyExistsException;
import pl.bankproject.exceptions.NoSuchClientInRepositoryException;
import pl.bankproject.exceptions.WrongClientDetailsException;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;


public class InMemoryClientRepository implements ClientRepository {


    private final ArrayList<Client> clients;

    public InMemoryClientRepository(ArrayList<Client> clients) {
        this.clients = clients;
    }

    @Override
    public void save(Client client) {
        if (client.getName() == null || client.getEmail() == null || client.getBalance() < 0) {
            throw new WrongClientDetailsException();
        }

        if (clients.contains(client)) {
            throw new ClientAlreadyExistsException("Client already exists");
        }
        String clientName = client.getName();
        String clientEmail = client.getEmail().toLowerCase();
        if (emailExists(clients, clientEmail) && clients.size() > 0) {
            throw new ClientAlreadyExistsException("Email is already taken");
        }
        Client approvedClient = new Client(clientName, clientEmail, client.getBalance());
        clients.add(approvedClient);


    }

    @Override
    public void remove(Client client) {
        String email = client.getEmail();
        if(client.getBalance()!=0){
            throw new IllegalArgumentException("You can't remove client with positive balance");
        }
        if(emailExists(clients,email) && client.getBalance()==0) {
            clients.remove(client);
        } else {
            throw new WrongClientDetailsException("No such Client with provided email");
        }
    }

    @Override
    public void listOfClient() {
        System.out.println(clients.stream().toList());
    }


    public boolean emailExists(ArrayList<Client> clientSet, String email) {
        return clientSet
                .stream()
                .anyMatch(client -> Objects.equals(client.getEmail(), email.toLowerCase()));
    }




    @Override
    public Client findByEmail(String email) {
        return clients.stream()
                .filter(client -> Objects.equals(client.getEmail(), email.toLowerCase()))
                .findFirst()
                .orElseThrow(() -> new NoSuchClientInRepositoryException("There is no such client with provided email"));

    }
}
