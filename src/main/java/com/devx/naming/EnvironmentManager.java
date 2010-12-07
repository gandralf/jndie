package com.devx.naming;

import java.util.Map;

/**
 * Represents something that has enviroment entries. Used to build the environment stack 
 * @author Alexandre Gandra Lages
 */
public interface EnvironmentManager {
    Map<String, Object> getEnvironment();
}
