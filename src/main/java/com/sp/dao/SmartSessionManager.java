package com.sp.dao;

import com.zippoy.zshell.entity.ConfigSession;
import com.zippoy.zshell.entity.SystemConfig;
import com.zippoy.zshell.model.Protocol;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings({ "deprecation", "unchecked" })
public class SmartSessionManager implements ISmartSessionManager {

    private SessionFactory sessionFactory;

    public SmartSessionManager() {
        if (sessionFactory == null) {
            sessionFactory = new Configuration().configure().buildSessionFactory();
        }
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /*
    DB Actions
     */
    @Override
    public List<Object> getAll(Class clazz) {
        return sessionFactory.openSession().createCriteria(clazz).list();
    }

    @Override
    public void save(Object entity) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(entity);
            tx.commit(); // Flush happens automatically
        } catch (RuntimeException e) {
            tx.rollback();
            throw e; // or display error message
        } finally {
            session.close();
        }
    }

    @Override
    public void update(Object entity) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(entity);
            tx.commit(); // Flush happens automatically
        } catch (RuntimeException e) {
            tx.rollback();
            throw e; // or display error message
        } finally {
            session.close();
        }
    }

    @Override
    public void delete(Object entity) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(entity);
            tx.commit(); // Flush happens automatically
        } catch (RuntimeException e) {
            tx.rollback();
            throw e; // or display error message
        } finally {
            session.close();
        }
    }

    /*
    API service
     */
    @Override
    public List<SystemConfig> getAllSystemConfigs() {
        return sessionFactory.openSession().createCriteria(SystemConfig.class).list();
    }

    @Override
    public List<ConfigSession> getAllCSessions() {
        return sessionFactory.openSession().createCriteria(ConfigSession.class).list();
    }

    @Override
    public void updateSystemConfig(String key, String newValue) {
        List<SystemConfig> list = getAllSystemConfigs();
        SystemConfig config = list.stream().filter(e -> StringUtils.equals(e.getKey(), key)).findAny().orElse(null);
        config.setValue(newValue);
        update(config);
    }

    @Override
    public List<ConfigSession> queryCSessionByHost(String host) {
        return getAllCSessions().stream().filter(e -> e.getHost().equals(host)).collect(Collectors.toList());
    }

    @Override
    public List<ConfigSession> queryCSessionByHostUser(String host, String user) {
        return getAllCSessions().stream().filter(e -> e.getHost().equals(host) && e.getUsername().equals(user)).collect(Collectors.toList());
    }

    @Override
    public ConfigSession queryCSessionByHostUserProtocol(String host, String user, Protocol protocal) {
        return getAllCSessions().stream()
                .filter(e -> e.getHost().equals(host) && e.getUsername().equals(user) && e.getProtocol() == protocal)
                .findAny()
                .orElse(null);
    }

    @Override
    public ConfigSession queryCSessionBySession(ConfigSession csession) {
        return getAllCSessions().stream().filter(e -> e.getId().equals(csession.getId())).findAny().orElse(null);
    }

    @Override
    public void shutdown() {
        // Close caches and connection pools
        getSessionFactory().close();
    }

}
