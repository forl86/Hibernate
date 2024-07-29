package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    private static long last_id;
    public UserDaoHibernateImpl() {
        this.last_id = 0L;
    }


    @Override
    public void createUsersTable() {
        Session session = Util.getSessionFactory().openSession();
        Transaction tr = session.beginTransaction();
        try {
            session.createSQLQuery("CREATE TABLE IF NOT EXISTS User(id INTEGER NOT NULL, name VARCHAR(25), lastName VARCHAR(25), age INTEGER)")
                .executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            tr.commit();
            session.close();
        }
    }

    @Override
    public void dropUsersTable() {
        Session session = Util.getSessionFactory().openSession();
        Transaction tr = session.beginTransaction();
        try {
            session.createSQLQuery("DROP TABLE IF EXISTS User").executeUpdate();
        } catch (Exception e) {
                e.printStackTrace();
        }
        finally {
            tr.commit();
            session.close();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Session session = Util.getSessionFactory().openSession();
        Transaction tr1 = session.beginTransaction();
        User u = new User(name, lastName, age);
        try {
            this.last_id = (long) session.save(u);
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            tr1.commit();
            session.close();
        }
    }

    @Override
    public void removeUserById(long id) {
        Session session = Util.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        try {
            User u = session.get(User.class, this.last_id);
            if (u != null) {
                session.delete(u);
            } else {
                System.out.println("session.get(id) returned null!");
            }
        }
        finally {
            tx1.commit();
            session.close();
        }
    }

    @Override
    public List<User> getAllUsers() {
        return (List<User>) Util.getSessionFactory().openSession().createQuery("From User").list();
    }

    @Override
    public void cleanUsersTable() {
        Session session = Util.getSessionFactory().openSession();
        List<User> users = (List<User>) session.createQuery("From User").list();
        Transaction tr = session.beginTransaction();
        for(User u : users) {
            session.delete(u);
        }
        tr.commit();
        session.close();
    }
}
