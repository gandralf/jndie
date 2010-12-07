package com.devx.naming;

import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * GlobalContextLoader loads mock-jndi.xml file
 * @author Alexandre Gandra Lages
 */
public class GlobalContextLoader extends EnvironmentLoader {
    private static boolean parsed;
    private static GlobalContextLoader instance;
    private String defaultEnvironment = null;

    private GlobalContextLoader() {
    }
    
    public static synchronized GlobalContextLoader getInstance() {
        return instance;
    }
    
    public String getDefaultEnvironment() {
    	return defaultEnvironment;
    }

    public void setDefaultEnvironment(String value) {
    	defaultEnvironment = value;
    }
    
    static synchronized void init() throws IOException {
        if (!parsed) {
            instance = new GlobalContextLoader();
            instance.parseXML();
            parsed = true;
        }
    }
    
    private void parseXML() throws IOException {
        Digester digester = new Digester();

        digester.push(this);
        digester.addSetProperties("mock-jndi", "default-environment", "defaultEnvironment");
        
        digester.addCallMethod("mock-jndi/string", "newObject", 2);
        digester.addCallParam("mock-jndi/string", 0, "name");
        digester.addCallParam("mock-jndi/string", 1);
        
        digester.addCallMethod("mock-jndi/boolean", "newBoolean", 2);
        digester.addCallParam("mock-jndi/boolean", 0, "name");
        digester.addCallParam("mock-jndi/boolean", 1);
        
        digester.addObjectCreate("mock-jndi/data-source", MockDataSource.class);
        digester.addCallMethod("mock-jndi/data-source", "setName", 1);
        digester.addCallParam("mock-jndi/data-source", 0, "name");
        
        digester.addBeanPropertySetter("mock-jndi/data-source/connection-url", "connectionUrl");
        digester.addBeanPropertySetter("mock-jndi/data-source/driver-class", "driverClass");
        digester.addBeanPropertySetter("mock-jndi/data-source/user-name", "userName");
        digester.addBeanPropertySetter("mock-jndi/data-source/password");
        digester.addSetNext("mock-jndi/data-source", "newDataSource");

        digester.addObjectCreate("mock-jndi/bean", BeanWrapper.class);
        digester.addCallMethod("mock-jndi/bean", "setName", 1);
        digester.addCallParam("mock-jndi/bean", 0, "name");
        
        digester.addCallMethod("mock-jndi/bean", "setType", 1);
        digester.addCallParam("mock-jndi/bean", 0, "type");
        
        digester.addCallMethod("mock-jndi/bean/property", "setProperty", 2);
        digester.addCallParam("mock-jndi/bean/property", 0, "name");
        digester.addCallParam("mock-jndi/bean/property", 1);
        digester.addSetNext("mock-jndi/bean", "newBean");
        
        digester.addObjectCreate("mock-jndi/environment", EnvironmentLoader.class);
        digester.addSetProperties("mock-jndi/environment");

        digester.addCallMethod("mock-jndi/environment/string", "newObject", 2);
        digester.addCallParam("mock-jndi/environment/string", 0, "name");
        digester.addCallParam("mock-jndi/environment/string", 1);
        
        digester.addCallMethod("mock-jndi/environment/boolean", "newBoolean", 2);
        digester.addCallParam("mock-jndi/environment/boolean", 0, "name");
        digester.addCallParam("mock-jndi/environment/boolean", 1);

        digester.addCallMethod("mock-jndi/environment/resource-ref", "newReference", 2);
        digester.addCallParam("mock-jndi/environment/resource-ref", 0, "name");
        digester.addCallParam("mock-jndi/environment/resource-ref", 1, "ref-name");
        
        digester.addSetNext("mock-jndi/environment", "newEnvironment");
        
        InputStream input = getClass().getResourceAsStream("/mock-jndi.xml");
        if (input == null) {
            throw new FileNotFoundException("mock-jndi.xml not found on class path");
        }
        
        try {
            digester.parse(input);
            input.close();
        } catch (SAXException e) {
            throw (IllegalStateException) new IllegalStateException("Illegal mock-jndi.xml file").initCause(e);
        }
        
        buildObjectMap();
    }
    
    public void newDataSource(MockDataSource dataSource) {
        BeanWrapper beanWrapper = new BeanWrapper(null, dataSource) {
            public String getName() {
                return ((MockDataSource) getObject()).getName();
            }
        };
        
        newBean(beanWrapper);
    }

    public void newEnvironment(final EnvironmentLoader loader) {
        newObject(loader.getName(), new EnvironmentManager() {
			public Map<String, Object> getEnvironment() {
				return loader.getObjectMap();
			}
			
			public String toString() {
				return loader.getName() + " environment";
			}
        });
    }
}
