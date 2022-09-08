package pl.bankproject.repository.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.bankproject.repository.entity.Client;

import java.util.List;

public interface ClientSpringJpaRepository extends JpaRepository<Client, Long> {

    @Query("SELECT c FROM Client c WHERE c.email = :email")
    Client findByEmail(@Param("email") String email);
    void deleteByEmail(Client clientToDelete);
    List<Client> findByName(String name);
    Page<Client> findByName(String name, Pageable pageable);



}
