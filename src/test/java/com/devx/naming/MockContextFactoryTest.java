package com.devx.naming;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.sql.DataSource;

import junit.framework.TestCase;

/**
 * Test some jndi features, like basic JNDI lookup, transaction and sessions bean support.
 * Before running this test, make sure to define the following parameters to the JVM<code>
 * -Djava.naming.factory.initial=com.devx.naming.MockContextFactory
 */
public class MockContextFactoryTest extends TestCase {
    private InitialContext context;

    public void setUp() throws NamingException, ClassNotFoundException {
        assertEquals("com.devx.naming.MockContextFactory", System.getProperty("java.naming.factory.initial"));
        context = new InitialContext();
    }
    

    public void testDefaultEnvironment() throws NamingException {
        Object result = context.lookup("java:comp/env/jdbc/dataSource");
        assertTrue(result instanceof DataSource);
    }

    public void testString() throws NamingException {
        String actual = (String) context.lookup("greetings");
        assertEquals("Hello, world!", actual);
    }

    /**
     * Known bug: this method can't be called before testString.
     * Reason: {@link MockContextFactory} must be initialized
     * before a "java:" name lookup, wich is done during the
     * testString call.
     */
    @SuppressWarnings({"JavaDoc"})
    public void testPkgs2() throws NamingException {
        String actual = (String) context.lookup("java:/pkgs");
        assertEquals("Hello, pkgs!", actual);
    }

    public void testDataSource() throws NamingException, SQLException {
        Object o = context.lookup("testDS");
        assertNotNull(o);
        assertTrue(o instanceof DataSource);
        DataSource ds = (DataSource) o;
        Connection cn = ds.getConnection();
        try {
            Statement stm = cn.createStatement();
            // Just to use the DS/Connection a little bit...
            stm.execute("create table Blah(msg varchar)");
            stm.execute("insert into Blah values('Hello')");
            ResultSet rs = stm.executeQuery("select msg from Blah");
            assertTrue(rs.next());
            assertEquals("Hello", rs.getString(1).trim());
            rs.close();
            stm.close();
        } finally {
            cn.close();
        }
    }

    public void testBean() throws NamingException {
        Object o = context.lookup("testBean");
        assertNotNull(o);
        assertTrue(o instanceof SimpleBean);
        SimpleBean b = (SimpleBean) o;
        assertEquals("Hello, world!", b.getMessage());
    }

    public void testPath() throws NamingException {
        String s = (String) context.lookup("tfc/test/greetings");
        assertEquals("Hello, path!", s);
    }

    public void testNameNotFound() throws NamingException {
        try {
            context.lookup("schulambs");
            fail("NameNotFoundException expected");
        } catch(NameNotFoundException e) {
            // OK
        }
    }

    public void testEnterAndLeaveEnvironment() throws NamingException {
        MockContext.enterContext("anotherEnvironment");
        try {
            String actual = (String) new InitialContext().lookup("java:comp/env/greetings");
            assertEquals("Hello, env!", actual);
        } finally {
            MockContext.leaveContext();
        }

        try {
            new InitialContext().lookup("java:comp/env/greetings");
            fail ("NamingException expected");
        } catch(NamingException e) {
            // OK
        }
    }

    public void testEnvValue() throws NamingException {
        String value = (String) context.lookup("java.naming.factory.initial");
        assertEquals("com.devx.naming.MockContextFactory", value);
        value = (String) context.lookup("not-found-env");
        assertEquals("${not-found}", value);
    }

    public void testEnvMacroEscape() throws NamingException {
        String value = (String) context.lookup("macro-escape");
        assertEquals("${java.naming.factory.initial}", value);
        value = (String) context.lookup("another-macro-escape");
        assertEquals("\\${java.naming.factory.initial}", value);
    }
}
