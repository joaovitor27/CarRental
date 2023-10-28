package org.example.controllers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;


public class HibernateController {
    private static SessionFactory sessionFactory;

    public HibernateController() {
        configureSessionFactory();
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
        Session session = sessionFactory.openSession();

        Transaction transaction = null;
        try (session) {
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


}
