package com.devx.naming;

import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Map;
import java.util.Properties;

/**
 * MockConnection is a wrapper around a real JDBC <code>Connection</code>
 * It also provides a support for transaction management... and warns if 
 * you are leaving open connections around, printing a stack trace.
 * @author Alexandre Gandra Lages
 */
public class MockConnection implements Connection {
    private static Logger log = Logger.getLogger(MockConnection.class);
    private Connection connection;
    private DataSource dataSource;
    private int openCount;

    public MockConnection(Connection connection, DataSource dataSource) {
        this.connection = connection;
        this.dataSource = dataSource;
        openCount = 1;
    }

    public void incOpenCount() {
        openCount++;
    }

    public Statement createStatement() throws SQLException {
        assertValid();
        return connection.createStatement();
    }

    public PreparedStatement prepareStatement(String sql) throws SQLException {
        assertValid();
        return connection.prepareStatement(sql);
    }

    public CallableStatement prepareCall(String sql) throws SQLException {
        assertValid();
        return connection.prepareCall(sql);
    }

    public String nativeSQL(String sql) throws SQLException {
        assertValid();
        return connection.nativeSQL(sql);
    }

    public void setAutoCommit(boolean autoCommit) throws SQLException {
        assertValid();
        connection.setAutoCommit(autoCommit);
    }

    public boolean getAutoCommit() throws SQLException {
        assertValid();
        return connection.getAutoCommit();
    }

    public void commit() throws SQLException {
        assertValid();
        connection.commit();
    }

    public void rollback() throws SQLException {
        assertValid();
        connection.rollback();
    }

    public void close() throws SQLException {
        log.debug("Connection.close deferred to transaction manager");
        openCount--;
    }

    public boolean isClosed() throws SQLException {
        return connection.isClosed();
    }

    public DatabaseMetaData getMetaData() throws SQLException {
        assertValid();
        return connection.getMetaData();
    }

    public void setReadOnly(boolean readOnly) throws SQLException {
        assertValid();
        connection.setReadOnly(readOnly);
    }

    public boolean isReadOnly() throws SQLException {
        assertValid();
        return connection.isReadOnly();
    }

    public void setCatalog(String catalog) throws SQLException {
        assertValid();
        connection.setCatalog(catalog);
    }

    public String getCatalog() throws SQLException {
        assertValid();
        return connection.getCatalog();
    }

    public void setTransactionIsolation(int level) throws SQLException {
        assertValid();
        connection.setTransactionIsolation(level);
    }

    public int getTransactionIsolation() throws SQLException {
        assertValid();
        return connection.getTransactionIsolation();
    }

    public SQLWarning getWarnings() throws SQLException {
        assertValid();
        return connection.getWarnings();
    }

    public void clearWarnings() throws SQLException {
        assertValid();
        connection.clearWarnings();
    }

    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        assertValid();
        return connection.createStatement(resultSetType, resultSetConcurrency);
    }

    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        assertValid();
        return connection.prepareStatement(sql, resultSetType, resultSetConcurrency);
    }

    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        assertValid();
        return connection.prepareCall(sql, resultSetType, resultSetConcurrency);
    }

    public Map<String, Class<?>> getTypeMap() throws SQLException {
        assertValid();
        return connection.getTypeMap();
    }

    public void setTypeMap(Map<String, Class<?>> stringClassMap) throws SQLException {
        assertValid();
        connection.setTypeMap(stringClassMap);
    }

    public void setHoldability(int holdability) throws SQLException {
        assertValid();
        connection.setHoldability(holdability);
    }

    public int getHoldability() throws SQLException {
        assertValid();
        return connection.getHoldability();
    }

    public Savepoint setSavepoint() throws SQLException {
        assertValid();
        return connection.setSavepoint();
    }

    public Savepoint setSavepoint(String name) throws SQLException {
        assertValid();
        return connection.setSavepoint(name);
    }

    public void rollback(Savepoint savepoint) throws SQLException {
        assertValid();
        connection.rollback(savepoint);
    }

    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        assertValid();
        connection.releaseSavepoint(savepoint);
    }

    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        assertValid();
        return connection.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        assertValid();
        return connection.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        assertValid();
        return connection.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        assertValid();
        return connection.prepareStatement(sql, autoGeneratedKeys);
    }

    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        assertValid();
        return connection.prepareStatement(sql, columnIndexes);
    }

    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        assertValid();
        return connection.prepareStatement(sql, columnNames);
    }

    public Clob createClob() throws SQLException {
        assertValid();
        return connection.createClob();
    }

    public Blob createBlob() throws SQLException {
        assertValid();
        return connection.createBlob();
    }

    public NClob createNClob() throws SQLException {
        assertValid();
        return connection.createNClob();
    }

    public SQLXML createSQLXML() throws SQLException {
        assertValid();
        return connection.createSQLXML();
    }

    public boolean isValid(int i) throws SQLException {
        assertValid();
        return connection.isValid(i);
    }

    public void setClientInfo(String s, String s1) throws SQLClientInfoException {
        // assertValid();
        connection.setClientInfo(s, s1);
    }

    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        connection.setClientInfo(properties);
    }

    public String getClientInfo(String s) throws SQLException {
        assertValid();
        return connection.getClientInfo(s);
    }

    public Properties getClientInfo() throws SQLException {
        assertValid();
        return connection.getClientInfo();
    }

    public Array createArrayOf(String s, Object[] objects) throws SQLException {
        assertValid();
        return connection.createArrayOf(s, objects);
    }

    public Struct createStruct(String s, Object[] objects) throws SQLException {
        assertValid();
        return connection.createStruct(s, objects);
    }

    public Connection getSqlConnection() {
        return connection;
    }

    private void assertValid() throws SQLException {
        if (connection.isClosed()) {
            openCount = 1;
            if (log.isDebugEnabled()) {
                printStackTrace("Bad pratice: connection is closed. Reopening...", 4);
            }

            MockConnection mockConnection = (MockConnection) dataSource.getConnection();
            connection = mockConnection.getSqlConnection();
        }
    }

    public void assertClosed() {
        if (openCount != 0 && log.isDebugEnabled()) {
            printStackTrace("End of transaction: " + openCount + " opened connections", 11);
        }
    }

    private static void printStackTrace(String message, int stackStart) {
        StringBuffer buffer = new StringBuffer(message).append("\n");
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (int i = stackStart; i < stackTrace.length; i++) {
            buffer.append("\t").append(stackTrace[i]).append("\n");
        }

        log.warn(buffer);
    }

    public <T> T unwrap(Class<T> tClass) throws SQLException {
        return connection.unwrap(tClass);
    }

    public boolean isWrapperFor(Class<?> aClass) throws SQLException {
        return connection.isWrapperFor(aClass);
    }
}
