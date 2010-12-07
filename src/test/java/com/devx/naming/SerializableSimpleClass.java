package com.devx.naming;

import java.io.Serializable;

class SerializableSimpleClass extends SimpleClass implements Serializable {
	private static final long serialVersionUID = 9106042461436319798L;
	
    public SerializableSimpleClass() {
    }

    public SerializableSimpleClass(int value) {
        super(value);
    }
}
