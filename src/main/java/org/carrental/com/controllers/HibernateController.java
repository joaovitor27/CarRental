package org.carrental.com.controllers;

import org.carrental.com.enums.VehicleCategory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;


public class HibernateController {
    private static SessionFactory sessionFactory;

    public HibernateController() {
        configureSessionFactory();
    }

    public HibernateController(Boolean update) {
        configureSessionFactory();
        if (update) {
            updateDataBase();
        }
    }

    public static void updateDataBase() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("car_rental_unit");
        EntityManager em = emf.createEntityManager();
        em.close();
        emf.close();
    }

    private void configureSessionFactory() {
        try {
            Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
            sessionFactory = configuration.buildSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize Hibernate.");
        }
    }

    public void closeSessionFactory() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    public void save(Object objeto) {

        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(objeto);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void update(Object objeto) {
        Session session = sessionFactory.openSession();

        Transaction transaction = null;
        try (session) {
            transaction = session.beginTransaction();
            session.update(objeto);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void delete(Object objeto) {
        Session session = sessionFactory.openSession();

        Transaction transaction = null;
        try (session) {
            transaction = session.beginTransaction();
            session.delete(objeto);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }


    public <T> T selectGeneric(Class<T> entityType, String query, String parameter) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            String hql = "FROM " + entityType.getSimpleName() + " WHERE " + parameter + " = '" + query + "'";
            Query<T> hqlQuery = session.createQuery(hql, entityType);
            T entity = hqlQuery.uniqueResult();
            transaction.commit();
            return entity;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return null; // Ou lance uma exceção personalizada, se preferir
        }
    }

    public <T> T selectRentalUserActive(Class<T> entityType, int query, String parameter) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            String hql = "FROM " + entityType.getSimpleName() + " WHERE " + parameter + " = " + query + " AND is_active = true";
            Query<T> hqlQuery = session.createQuery(hql, entityType);
            T entity = hqlQuery.uniqueResult();
            transaction.commit();
            return entity;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return null; // Ou lance uma exceção personalizada, se preferir
        }
    }

    public <T> T selectGeneric(Class<T> entityType, int query, String parameter) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            String hql = "FROM " + entityType.getSimpleName();
            if (parameter != null) {
                hql += " WHERE " + parameter + " = " + query;
            }
            Query<T> hqlQuery = session.createQuery(hql, entityType);
            T entity = hqlQuery.uniqueResult();
            transaction.commit();
            return entity;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return null; // Ou lance uma exceção personalizada, se preferir
        }
    }

    public <T> T selectGeneric(Class<T> entityType, Long query, String parameter) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            String hql = "FROM " + entityType.getSimpleName() + " WHERE " + parameter + " = '" + query + "'";
            Query<T> hqlQuery = session.createQuery(hql, entityType);
            T entity = hqlQuery.uniqueResult();
            transaction.commit();
            return entity;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return null; // Ou lance uma exceção personalizada, se preferir
        }
    }

    public <T> List<T> selectAllGeneric(Class<T> entityType, String query, String parameter) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            String hql = "FROM " + entityType.getSimpleName() + " WHERE " + parameter + " = :paramValue";
            Query<T> hqlQuery = session.createQuery(hql, entityType);
            hqlQuery.setParameter("paramValue", query);
            List<T> resultList = hqlQuery.list();
            transaction.commit();
            return resultList;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return null; // Ou lance uma exceção personalizada, se preferir
        }
    }

    public <T> List<T> selectAllGeneric(Class<T> entityType, VehicleCategory query, String parameter) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            String hql = "FROM " + entityType.getSimpleName() + " WHERE " + parameter + " = :paramValue";
            Query<T> hqlQuery = session.createQuery(hql, entityType);
            hqlQuery.setParameter("paramValue", query);
            List<T> resultList = hqlQuery.list();
            transaction.commit();
            return resultList;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return null; // Ou lance uma exceção personalizada, se preferir
        }
    }

    public <T> List<T> selectAllGeneric(Class<T> entityType, int query, String parameter) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            String hql = "FROM " + entityType.getSimpleName() + " WHERE " + parameter + " = :paramValue";
            Query<T> hqlQuery = session.createQuery(hql, entityType);
            hqlQuery.setParameter("paramValue", query);
            List<T> resultList = hqlQuery.list();
            transaction.commit();
            return resultList;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return null; // Ou lance uma exceção personalizada, se preferir
        }

    }

    public <T> List<T> selectAllGeneric(Class<T> entityType, boolean query, String parameter) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            String hql = "FROM " + entityType.getSimpleName() + " WHERE " + parameter + " = :paramValue";
            Query<T> hqlQuery = session.createQuery(hql, entityType);
            hqlQuery.setParameter("paramValue", query);
            List<T> resultList = hqlQuery.list();
            transaction.commit();
            return resultList;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return null; // Ou lance uma exceção personalizada, se preferir
        }

    }

    public <T> List<T> selectAllGeneric(Class<T> entityType, float query, String parameter) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            String hql = "FROM " + entityType.getSimpleName() + " WHERE " + parameter + " = :paramValue";
            Query<T> hqlQuery = session.createQuery(hql, entityType);
            hqlQuery.setParameter("paramValue", query);
            List<T> resultList = hqlQuery.list();
            transaction.commit();
            return resultList;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return null; // Ou lance uma exceção personalizada, se preferir
        }

    }
}
