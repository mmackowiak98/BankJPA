package pl.bankproject.repository.hibernate;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import pl.bankproject.annotation.HibernateRepository;
import pl.bankproject.interfaces.ClientRepository;
import pl.bankproject.repository.entity.Client;

@Repository
@HibernateRepository
public class HibernateClientRepository implements ClientRepository {
    @Override
    public void save(Client client) {
        try(final Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            client
                    .getAccounts()
                    .forEach(session::save);
            session.save(client);
            session.getTransaction().commit();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void remove(Client client) {
        final Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.remove(client);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void listOfClient() {
        final Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        final Query<Client> fromClient = session.createQuery("from Client", Client.class);
        fromClient.stream().forEach(System.out::println);
        session.close();

    }

    @Override
    public Client findByEmail(String email) {
        final Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        final Query<Client> query = session.createQuery("from Client where mail=:mail", Client.class);
        query.setParameter("mail", email);
        final Client client = query.uniqueResult();
        session.close();
        return client;
    }
}
