package pl.bankproject.repository.JDBC;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import pl.bankproject.annotation.JDBCRepository;
import pl.bankproject.interfaces.ClientRepository;
import pl.bankproject.repository.entity.Client;

import java.sql.*;

@Repository
@JDBCRepository
public class JDBCClientRepository implements ClientRepository {

    public final String user;
    public final String password;
    public final String jdbc_url;

    public JDBCClientRepository(@Value("${jdbc.user}") String user,
                                @Value("${jdbc.password}")String password,
                                @Value("${jdbc.url}") String jdbc_url) {

        this.user = user;
        this.password = password;
        this.jdbc_url = jdbc_url;
    }

    @Override
    public void save(Client client) {
        try(Connection connection = DriverManager.getConnection(jdbc_url, user, password)){
            final String name = client.getName();
            final String email = client.getEmail();
            final PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO users(first_name,mail) VALUES(?,?)");
            preparedStatement.setString(1,name);
            preparedStatement.setString(2,email);
            preparedStatement.execute();


        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void remove(Client client) {

    }

    @Override
    public void listOfClient() {

    }

    @Override
    public Client findByEmail(String email) {
        try(Connection connection = DriverManager.getConnection(jdbc_url, user, password)){
            final PreparedStatement preparedStatement = connection.prepareStatement("SELECT first_name,mail FROM users WHERE mail=?");
            preparedStatement.setString(1,email);
            final ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                final String name = resultSet.getString("first_name");
                final String mail = resultSet.getString("mail");
                return new Client(name,mail,null);
            }


        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }
}
