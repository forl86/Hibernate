package jm.task.core.jdbc;

import jm.task.core.jdbc.dao.UserDaoHibernateImpl;
import jm.task.core.jdbc.dao.UserDaoJDBCImpl;
import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserServiceImpl;

import java.sql.DriverManager;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        UserServiceImpl userService = new UserServiceImpl();
        userService.createUsersTable();
        userService.saveUser("Evgenii", "Simonov", (byte)38);
        userService.saveUser("Misha", "Sayapin", (byte)42);
        userService.saveUser("Sergey", "Mozolin", (byte)43);
        userService.saveUser("Denis", "Vetlugin", (byte)55);
        List<User> userList = userService.getAllUsers();
        for(User user : userList) {
            System.out.println(user.toString());
        }
        userService.cleanUsersTable();
        userService.dropUsersTable();
    }
}
