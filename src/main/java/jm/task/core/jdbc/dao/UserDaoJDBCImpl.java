package jm.task.core.jdbc.dao;
import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private java.sql.Connection connection;
    public UserDaoJDBCImpl() {
        try {
            this.connection = Util.connectDB();
        } catch ( SQLException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        }
    }

    public void createUsersTable() {
        String createTable = "CREATE TABLE Users (id integer NOT NULL AUTO_INCREMENT, name varchar(30), lastName varchar(30), age integer, " +
                "PRIMARY KEY(id))";
        try {
            PreparedStatement myStmt = connection.prepareStatement(createTable);
            int result = myStmt.executeUpdate();
            System.out.println("create table returned " + result);
        } catch (java.sql.SQLSyntaxErrorException ignore) {

        }
        catch (SQLException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        }
    }

    public void dropUsersTable() {
        String dropTable = "DROP TABLE Users";
        try {
            PreparedStatement myStmt = connection.prepareStatement(dropTable);
            int result = myStmt.executeUpdate();
            System.out.println("drop table returned " + result);
        } catch (SQLSyntaxErrorException ignore) {

        }
        catch (SQLException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        }
     }

    public void saveUser(String name, String lastName, byte age) {
        try {
            PreparedStatement preparedStGetLastID = connection.prepareStatement("SELECT MAX(id) FROM Users");
//            "SELECT LAST_INSERT_ID()"
            java.sql.ResultSet resultSet = preparedStGetLastID.executeQuery();
            int lastId = 0;
            while ( resultSet.next() ) {
                String stringId = resultSet.getString(1);
                if (stringId != null) {
                    System.out.println("Last id: " + stringId);
                    lastId = Integer.parseInt(stringId);
                }
            }
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Users VALUES( ?, ?, ?, ?)");
            //add retrieving id
            preparedStatement.setInt(1, lastId+1);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, lastName);
            preparedStatement.setInt(4, age);
            preparedStatement.executeUpdate();
            System.out.println("User с именем " + name + " добавлен в базу данных");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        }
    }

    public void removeUserById(long id) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Users WHERE id = ?");
            preparedStatement.setInt(1, (int) id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        }
    }

    public List<User> getAllUsers() {
        List<User> usersList = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Users");
            java.sql.ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String strId = resultSet.getString(1);
                String strName = resultSet.getString(2);
                String strLastName = resultSet.getString(3);
                String strAge = resultSet.getString(4);
                User user = new User(strName, strLastName, Byte.parseByte(strAge));
                usersList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        }
        return usersList;
    }

    public void cleanUsersTable() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Users");
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        }
    }
}
