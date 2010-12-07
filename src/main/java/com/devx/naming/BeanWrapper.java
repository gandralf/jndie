package com.devx.naming;

import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * Generic bean wrapper
 * @author Alexandre Gandra Lages
 */
public class BeanWrapper {
    private String name;
    private Object object;
    private Map<String, Object> properties = new HashMap<String, Object>();

    public BeanWrapper() {
        
    }
    
    public BeanWrapper(String name, Object object) {
        this.name = name;
        this.object = object;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String value) {
        name = value;
    }
    
    public void setType(String type) throws ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException {
        Class clazz = Class.forName(type);
        object = clazz.newInstance();
        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            String property = entry.getKey();
            Object value = entry.getValue();
            BeanUtils.setProperty(object, property, value);
        }
    }
    
    public void setProperty(String property, Object value) {
        properties.put(property, value);
    }

    public Object getObject() {
        return object;
    }
    
    public String toString() {
        return getName() + "=" + getObject();
    }
}