package com.devx.naming;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import junit.framework.TestCase;

public class URLContextTest extends TestCase {
    private String oldInitial;
    private String oldPkgs;

    public void setUp() throws NamingException {
        oldInitial = System.setProperty("java.naming.factory.initial", "com.devx.naming.MockContextFactory");
        oldPkgs = System.getProperty("java.naming.factory.url.pkgs");
	}

	public void tearDown() {
        if (oldInitial != null) {
			System.setProperty("java.naming.factory.initial", oldInitial);
		}
        if (oldPkgs != null) {
            System.setProperty("java.naming.factory.url.pkgs", oldPkgs);
        }
	}
	
	public void testNullPkg() throws NamingException {
        System.clearProperty("java.naming.factory.url.pkgs");
        
		MockContextFactory.init();
        InitialContext context = new InitialContext();
        assertEquals("Hello, pkgs!", context.lookup("java:/pkgs"));
	}

	public void testNicePkg() throws NamingException {
        System.setProperty("java.naming.factory.url.pkgs", "com.devx.naming.url");
        
		MockContextFactory.init();
        InitialContext context = new InitialContext();
        assertEquals("Hello, pkgs!", context.lookup("java:/pkgs"));
	}

    public void testAdditionalPkg() throws NamingException {
        System.setProperty("java.naming.factory.url.pkgs", "com.devx.naming.test");

        MockContextFactory.init();
        InitialContext context = new InitialContext();
        assertEquals("Hello, pkgs!", context.lookup("java:/pkgs"));
    }

    public void testAnotherNamespace() throws NamingException {
        System.setProperty("java.naming.factory.url.pkgs", "com.devx.naming.test");

        MockContextFactory.init();
        InitialContext context = new InitialContext();

        assertEquals("Hello, foo!", context.lookup("foo:/foo"));
    }

    public void testSameNamespace() throws NamingException {
        System.setProperty("java.naming.factory.url.pkgs", "com.devx.naming.test");

        MockContextFactory.init();
        InitialContext context = new InitialContext();

        assertEquals("Hello, java:/test!", context.lookup("java:/test"));
    }
}
