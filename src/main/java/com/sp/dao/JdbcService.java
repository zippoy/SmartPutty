package com.sp.dao;

import lombok.Data;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zippoy
 * @date 2021-09-17
 */
@Slf4j
@Getter
public class JdbcService {

    private Connection connection;
    private DataSource dataSource;

    /**
     * Connect to database.
     *
     * @param driver   {@link Driver} driver implement class name.
     * @param url      db connection url
     * @param name     db authentication name
     * @param password db authentication password
     */
    public JdbcService(String driver, String url, String name, String password) {
        try {
            Class.forName(driver);
            this.connection = DriverManager.getConnection(url, name, password);
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
    }


    /**
     * Connect to database.
     *
     * @param dataSource    db dataSource url
     * @return {@link Connection}
     */
    public JdbcService(DataSource dataSource) {
        try {
            this.dataSource = dataSource;
            this.connection = dataSource.getConnection();
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
    }

    /**
     * Disconnect from a database.
     */
    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
    }

    /**
     * Execute command.
     *
     * @param cmd        Command
     * @return true/false
     */
    @SneakyThrows
    public boolean execute(String cmd) {
        if (connection == null || cmd == null || cmd.isEmpty()) {
            return false;
        }
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.execute(cmd);
            connection.commit();
            return true;
        } catch (Exception e) {
            connection.rollback();
            log.warn(e.getMessage(), e);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (Exception e) {
                    log.warn(e.getMessage(), e);
                }
            }
        }
        return false;
    }

    /**
     * Execute query sql.
     *
     * @param sql        Query sql statement
     * @param params     Query sql parameters
     * @return result rows [{"id":1}, {"id":2}, {"id":3}, {"id":4}]
     */
    public List<Map<String, Object>> query(String sql, Object... params) {
        return query(Command.build().sql(sql).params(params));
    }

    /**
     * Execute query sql.
     *
     * @param command    {@link Command}
     * @return result rows [{"id":1}, {"id":2}, {"id":3}, {"id":4}]
     */
    public List<Map<String, Object>> query(Command command) {
        List<Map<String, Object>> rows = new ArrayList<>(1000);
        query(command, (row, number) -> rows.add(row));
        return rows;
    }

    /**
     * Execute query sql.
     *
     * @param command            {@link Command}
     * @param resultRowProcessor {@link ResultRowProcessor}
     * @return result row count
     */
    @SneakyThrows
    public int query(Command command, ResultRowProcessor... resultRowProcessor) {
        if (connection == null || command == null || command.getSql() == null
                || resultRowProcessor == null || resultRowProcessor.length == 0) {
            return 0;
        }
        int dataCount = 0;
        ResultSet resultSet = null;
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(command.getSql(), ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            statement.setFetchSize(500);
            if (command.getParams() != null) {
                for (int i = 0; i < command.getParams().size(); i++) {
                    statement.setObject(i + 1, command.getParams().get(i));
                }
            }
            resultSet = statement.executeQuery();

            // 获得结果集结构信息（元数据）
            ResultSetMetaData md = resultSet.getMetaData();
            // ResultSet列数
            int columnCount = md.getColumnCount();
            while (resultSet.next()) {
                ++dataCount;
                Map<String, Object> row = new HashMap();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(md.getColumnLabel(i), resultSet.getObject(i));
                }
                for (ResultRowProcessor processor : resultRowProcessor) {
                    processor.process(row, dataCount);
                }
            }
            connection.commit();
        } catch (Exception e) {
            connection.rollback();
            log.warn(e.getMessage(), e);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (Exception e) {
                    log.warn(e.getMessage(), e);
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (Exception e) {
                    log.warn(e.getMessage(), e);
                }
            }
        }
        return dataCount;
    }

    /**
     * Execute update sql statement.
     *
     * @param sql        Update sql statement
     * @param params     Update sql parameters
     * @return affected rows
     */
    public int update(String sql, Object... params) {
        return update(Command.build().sql(sql).params(params));
    }

    /**
     * Execute update sql statement.
     *
     * @param command    {@link Command}
     * @return affected rows
     */
    @SneakyThrows
    public int update(Command command) {
        if (connection == null || command == null || command.getSql() == null) {
            return 0;
        }
        int result = 0;
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(command.getSql());
            if (command.getParams() != null) {
                for (int i = 0; i < command.getParams().size(); i++) {
                    statement.setObject(i + 1, command.getParams().get(i));
                }
            }
            result = statement.executeUpdate();
            connection.commit();
        } catch (Exception e) {
            connection.rollback();
            log.warn(e.getMessage(), e);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (Exception e) {
                    log.warn(e.getMessage(), e);
                }
            }
        }
        return result;
    }

    /**
     * Execute update sql transaction.
     *
     * @param commands   sql commands {@link Command}
     * @return true/false
     */
    public boolean transaction(Command... commands) {
        if (connection == null || commands == null || commands.length == 0) {
            return false;
        }
        List<PreparedStatement> statements = new ArrayList<>();
        try {
            connection.setAutoCommit(false);
            for (Command command : commands) {
                String sql = command.getSql();
                List<Object> params = command.getParams();
                PreparedStatement statement = connection.prepareStatement(sql);
                if (params != null) {
                    for (int i = 0; i < params.size(); i++) {
                        statement.setObject(i + 1, params.get(i));
                    }
                }
                statements.add(statement);
                if (statement.executeUpdate() <= 0) {
                    throw new RuntimeException("Rollback.");
                }
            }
            connection.commit();
            return true;
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
            try {
                connection.rollback();
            } catch (Exception ex) {
                log.warn(ex.getMessage(), ex);
            }
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (Exception e) {
                log.warn(e.getMessage(), e);
            }
            for (PreparedStatement statement : statements) {
                try {
                    statement.close();
                } catch (Exception e) {
                    log.warn(e.getMessage(), e);
                }
            }
        }
        return false;
    }

    /**
     * Build an empty sql command.
     *
     * @return {@link Command}
     */
    public static Command buildCommand() {
        return Command.build();
    }

    /**
     * Build a sql command with sql statement and parameters.
     *
     * @param sql    sql statement
     * @param params sql parameters
     * @return {@link Command}
     */
    public static Command buildCommand(String sql, Object... params) {
        return Command.build().sql(sql).params(params);
    }

    /**
     * SQL command.
     * <pre>
     *     sql statement
     *     sql parameters
     * </pre>
     */
    @Data
    public static class Command {
        private String sql;
        private List<Object> params;

        public static Command build() {
            return new Command();
        }

        public Command sql(String sql) {
            this.sql = sql;
            return this;
        }

        public Command params(Object... params) {
            this.params = Arrays.asList(params);
            return this;
        }

    }

    public interface ResultRowProcessor {
        /**
         * Process row.
         *
         * @param row       current row data
         * @param rowNumber current row number
         */
        void process(Map<String, Object> row, int rowNumber);
    }
}