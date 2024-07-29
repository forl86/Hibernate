package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    public UserDaoHibernateImpl() {
    }


    @Override
    public void createUsersTable() {
        try (Session session = Util.getSessionFactory().openSession()) {
            Transaction tr = session.beginTransaction();
            try {
                session.createSQLQuery("CREATE TABLE IF NOT EXISTS User(id INTEGER NOT NULL, name VARCHAR(25), lastName VARCHAR(25), age INTEGER)")
                        .executeUpdate();
                tr.commit();
            } catch (Exception e) {
                tr.rollback();
                throw e;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dropUsersTable() {
        try (Session session = Util.getSessionFactory().openSession()) {
            Transaction tr = session.beginTransaction();
            try {
                session.createSQLQuery("DROP TABLE IF EXISTS User").executeUpdate();
                tr.commit();
            } catch (Exception e) {
                tr.rollback();
                throw e;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Session session = Util.getSessionFactory().openSession()) {
            Transaction tr1 = session.beginTransaction();
            User u = new User(name, lastName, age);
            try {
                session.save(u);
                tr1.commit();
            } catch (Exception e) {
                tr1.rollback();
                throw e;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Session session = Util.getSessionFactory().openSession()) {
            Transaction tr1 = session.beginTransaction();
            try {
                User u = session.get(User.class, id);
                if (u != null) {
                    session.delete(u);
                    tr1.commit();
                } else {
                    System.out.println("session.get(id) returned null!");
                }
            } catch (Exception e) {
                tr1.rollback();
                throw e;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<User> getAllUsers() {
        Query query = null;
        List<User> list = null;
        try( Session session = Util.getSessionFactory().openSession()) {
            query = session.createQuery("From User");
            list = query.list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = Util.getSessionFactory().openSession()) {
            List<User> users = (List<User>) session.createQuery("From User").list();
            Transaction tr = session.beginTransaction();
            try {
                for (User u : users) {
                    session.delete(u);
                }
                tr.commit();
            } catch (Exception e) {
                tr.rollback();
                throw e;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
