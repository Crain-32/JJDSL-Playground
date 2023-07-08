package core.handlers;

import core.fields.FieldSupport;
import core.fields.interfaces.GetField;
import core.fields.interfaces.GetSetField;
import core.fields.interfaces.SetField;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class FieldInvocationHandler implements InvocationHandler {
    @SuppressWarnings("FieldCanBeLocal")
    private final FieldSupport SUPPORT_LEVEL;

    private final Map<String, Object> proxyFields = new HashMap<>();

    public FieldInvocationHandler(Class<?> proxyClass) {
        if (proxyClass == null) {
            throw new IllegalArgumentException("Proxy Class cannot be null");
        }
        if (GetSetField.class.isAssignableFrom(proxyClass)) {
            this.SUPPORT_LEVEL = FieldSupport.BOTH;
        } else if (GetField.class.isAssignableFrom(proxyClass)) {
            this.SUPPORT_LEVEL = FieldSupport.GET;
        } else if (SetField.class.isAssignableFrom(proxyClass)) {
            this.SUPPORT_LEVEL = FieldSupport.SET;
        } else {
            throw new IllegalArgumentException("Proxy Class doesn't support Field Proxy");
        }
    }

    private Object getField(String fieldName) throws IllegalAccessException {
        if (this.SUPPORT_LEVEL == FieldSupport.SET) {
            throw new IllegalAccessException("Target Class doesn't extend GetField");
        }
        return proxyFields.get(fieldName);
    }

    private void setField(String field, Object value) throws NoSuchFieldException {
        if (this.SUPPORT_LEVEL == FieldSupport.GET) {
            throw new NoSuchFieldException("Target Class doesn't extend SetField");
        }
        proxyFields.put(field, value);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (!method.getReturnType().getName().equals("void")) {
            return getField(method.getName());
        } else if (args == null) {
            throw new IllegalArgumentException(String.format("Cannot Set for Method: %s with a null Argument Array", method.getName()));
        } else if (args.length >= 1) {
            setField(method.getName(), args[0]);
        } else {
            throw new IllegalArgumentException(String.format(
                    "Failed to invoke Get/Set for Method: %s on Class %s",
                    method.getName(),
                    method.getDeclaringClass()
            ));
        }
        return null;
    }
}
