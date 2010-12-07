package com.devx.naming;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

/**
 * MockDataSource wrapper, used to support transaction management
 * @author Alexandre Gandra Lages
 */
public class MockDataSource implements DataSource {
	private Logger log = Logger.getLogger(MockDataSource.class);
    private String name;
    private String connectionUrl;
    private String userName;
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getConnectionUrl() {
        return connectionUrl;
    }
    
    public void setConnectionUrl(String connectionUrl) {
        this.connectionUrl = connectionUrl;
    }
    
    public void setDriverClass(String driverClass) {
        try {
			Class.forName(driverClass);
		} catch (ClassNotFoundException e) {
			log.error("JDBC driver not found: " + e.toString());
		}
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(connectionUrl, userName, password);
    }

    public Connection getConnection(String username, String password)
            throws SQLException {

        return DriverManager.getConnection(connectionUrl, username, password);
    }

    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    public void setLogWriter(PrintWriter out) throws SQLException {
    }

    public void setLoginTimeout(int seconds) throws SQLException {
    }

    public int getLoginTimeout() throws SQLException {
        return 0;
    }
    
    public String toString() {
        return userName + ":" + password + "@" + connectionUrl;
    }

    public <T> T unwrap(Class<T> tClass) throws SQLException {
        return null;
    }

    public boolean isWrapperFor(Class<?> aClass) throws SQLException {
        return false;
    }
}
