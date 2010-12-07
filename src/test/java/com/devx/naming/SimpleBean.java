package com.devx.naming;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class SimpleBean {
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getJndiMessage() throws NamingException {
        InitialContext ctx = new InitialContext();

        return (String) ctx.lookup("greetings");
    }

    public String getEnvMessage() throws NamingException {
        InitialContext ctx = new InitialContext();

        return (String) ctx.lookup("java:comp/env/greetings");
    }

    public String getFancyEnvMessage() throws NamingException {
        InitialContext initialContext = new InitialContext();
        Context ctx = (Context) initialContext.lookup("java:comp/env");

        return (String) ctx.lookup("greetings");
    }

    public String getRefMessage() throws NamingException, SQLException {
        InitialContext ctx = new InitialContext();
        DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/dataSource");
        Connection cn = ds.getConnection();
        try {
            Statement stm = cn.createStatement();
            ResultSet rs = stm.executeQuery("select 'Hello, ref!' from dual");
            rs.next();
            String result = rs.getString(1).trim();
            stm.close();
            
            return result;
        } finally {
            cn.close();
        }
    }

    public void throwException() throws IOException {
        throw new IOException("Got milk?");
    }
    
    public void testArgument(SimpleClass value) {
        value.setAttribute(value.getAttribute() + 1);
    }
}
