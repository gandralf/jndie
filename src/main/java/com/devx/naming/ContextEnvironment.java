package com.devx.naming;

import java.util.Stack;
import java.util.Map;

/**
 * Base for an environment hierarchy
 * @author Alexandre Gandra Lages
 */
class ContextEnvironment {
    Stack<Map<String, Object>> namingStack;

    ContextEnvironment() {
        namingStack = new Stack<Map<String, Object>>();
    }

    void enter(Map<String, Object> environment) {
        namingStack.push(environment);
    }

    boolean leave() {
        namingStack.pop();
        return namingStack.empty();
    }
}
