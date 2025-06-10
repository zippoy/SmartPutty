package com.sp.dao;

import com.zippoy.zshell.entity.ConfigSession;
import com.zippoy.zshell.entity.SystemConfig;
import com.zippoy.zshell.model.Protocol;

import java.util.Collections;
import java.util.List;

/**
 * <br>
 *
 * @author zippoy
 * @date 2025-06-06
 */
public class SessionManager implements ISmartSessionManager {

    private final JdbcService jdbcService;

    public SessionManager() {
        this.jdbcService = new JdbcService("org.h2.Driver", "jdbc:h2:file:~/smartputty.db", "sa", "zippoy_9986");
    }


    @Override
    public List<Object> getAll(Class clazz) {
        return Collections.emptyList();
    }

    @Override
    public void save(Object entity) {
        if (entity instanceof ConfigSession) {
            //jdbcService.update()
        } else if (entity instanceof SystemConfig) {

        } else {

        }
    }

    @Override
    public void update(Object entity) {
        //jdbcService.update()
    }

    @Override
    public void delete(Object entity) {
        //jdbcService.update()
    }

    @Override
    public List<SystemConfig> getAllSystemConfigs() {
        return Collections.emptyList();
    }

    @Override
    public List<ConfigSession> getAllCSessions() {
        return Collections.emptyList();
    }

    @Override
    public void updateSystemConfig(String key, String newValue) {

    }

    @Override
    public List<ConfigSession> queryCSessionByHost(String host) {
        return Collections.emptyList();
    }

    @Override
    public List<ConfigSession> queryCSessionByHostUser(String host, String user) {
        return Collections.emptyList();
    }

    @Override
    public ConfigSession queryCSessionByHostUserProtocol(String host, String user, Protocol protocal) {
        return null;
    }

    @Override
    public ConfigSession queryCSessionBySession(ConfigSession csession) {
        return null;
    }

    @Override
    public void shutdown() {

    }
}
