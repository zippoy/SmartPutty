package com.sp.dao;

import com.zippoy.zshell.entity.ConfigSession;
import com.zippoy.zshell.entity.SystemConfig;
import com.zippoy.zshell.model.Protocol;

import java.util.List;

/**
 * <br>
 *
 * @author zippoy
 * @date 2025-06-06
 */
public interface ISmartSessionManager {
    /**
     * DB Actions
     */
    List<Object> getAll(Class clazz);

    void save(Object entity);

    void update(Object entity);

    void delete(Object entity);

    /**
     * API service
     */
    List<SystemConfig> getAllSystemConfigs();

    List<ConfigSession> getAllCSessions();

    void updateSystemConfig(String key, String newValue);

    List<ConfigSession> queryCSessionByHost(String host);

    List<ConfigSession> queryCSessionByHostUser(String host, String user);

    ConfigSession queryCSessionByHostUserProtocol(String host, String user, Protocol protocal);

    ConfigSession queryCSessionBySession(ConfigSession csession);

    void shutdown();
}
