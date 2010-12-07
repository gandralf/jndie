package com.devx.naming;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * EnvironmentLoader holds environment entries. 
 * Used to hold global environment entries ({@link GlobalContextLoader})
 * @author Alexandre Gandra Lages
 */
public class EnvironmentLoader {
    private Logger log = Logger.getLogger(EnvironmentLoader.class);
    private Map<String, Object> objectMap;
    private List<BeanWrapper> beans;
    private String name;

    public EnvironmentLoader() {
        beans = new ArrayList<BeanWrapper>();
    }
    
    public String getName() {
        return name;
    }

    public void setName(String value) {
        name = value;
    }

    public Map<String, Object> getObjectMap() {
        if (objectMap == null) {
            buildObjectMap();
        }
        
        return objectMap;
    }
    
    protected void buildObjectMap() {
        objectMap = new HashMap<String, Object>();
        for (BeanWrapper beanWrapper : beans) {
            try {
                if (beanWrapper.getName() != null) {
                    if (log.isDebugEnabled()) {
                        log.debug("new jndi object: name="
                                + beanWrapper.getName() + ", value="
                                + beanWrapper.getObject());
                    }

                    objectMap.put(beanWrapper.getName(), beanWrapper
                            .getObject());
                }
            } catch (Exception e) {
                log.error("Environment entry ignored", e);
            }
        }
    }

    public void newObject(String name, Object value) {
        newBean(new BeanWrapper(name, value));
    }
    
    public void newBoolean(String name, String value) {
        newObject(name, Boolean.valueOf(value));
    }
    
    public void newBean(BeanWrapper beanWrapper) {
        beans.add(beanWrapper);
    }
    
    public void newReference(String name, final String refName) {
        newBean(new BeanWrapper(name, null) {
            public Object getObject() {
                return GlobalContextLoader.getInstance().getObjectMap().get(refName);
            }
        });
    }
}
