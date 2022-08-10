package pl.bankproject.repository;

import pl.bankproject.Client;

import java.sql.*;

public class JDBCClientRepository implements ClientRepository {

    public static final String USER = "postgres";
    public static final String PASSWORD = "98564774";
    public static final String JDBC_URL = "jdbc:postgresql://127.0.0.1:5432/test";
    @Override
    public void save(Client client) {
        try(Connection connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD)){
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
        try(Connection connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD)){
            final PreparedStatement preparedStatement = connection.prepareStatement("SELECT first_name,mail FROM users WHERE mail=?");
            preparedStatement.setString(1,email);
            final ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                final String name = resultSet.getString("first_name");
                final String mail = resultSet.getString("mail");
                return new Client(name,mail,0);
            }


        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }
}
