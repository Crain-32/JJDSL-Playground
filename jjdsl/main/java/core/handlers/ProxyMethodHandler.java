package core.handlers;

import core.annnotation.Source;
import core.engine.interfaces.EngineBacked;
import core.fields.interfaces.ProxyField;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class ProxyMethodHandler implements InvocationHandler {
    private enum ProxyType {
        ENGINE,
        FIELD,
    }

    // Cannot be shared because FIELD has to be unique per class
    private final Map<ProxyType, InvocationHandler> handlerMap = new HashMap<>();
    private final Map<Method, ProxyType> methodMap = new HashMap<>();

    public ProxyMethodHandler(Class<?> proxyClass) {
        for (Method method : proxyClass.getMethods()) {
            if (EngineBacked.class.isAssignableFrom(proxyClass) && method.isAnnotationPresent(Source.class)) {
                this.methodMap.put(method, ProxyType.ENGINE);
            } else if (ProxyField.class.isAssignableFrom(proxyClass) && !method.isAnnotationPresent(Source.class)) {
                this.methodMap.put(method, ProxyType.FIELD);
            } else {
                throw new IllegalArgumentException(
                        String.format(
                                "Cannot Create Proxy Method for Class %s and Method %s",
                                proxyClass.getName(),
                                method.getName())
                );
            }
        }
        populateMap(proxyClass);
    }

    private void populateMap(Class<?> proxyClass) {
        for (ProxyType type : new HashSet<>(this.methodMap.values())) {
            switch (type) {
                case ENGINE:
                    this.handlerMap.put(ProxyType.ENGINE, new EngineInvocationHandler());
                    break;
                case FIELD:
                    this.handlerMap.put(ProxyType.FIELD, new FieldInvocationHandler(proxyClass));
                    break;
                default:
                    throw new IllegalStateException("Proxy Type for a function should never be null");
            }
        }
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (!this.methodMap.containsKey(method)) {
            throw new IllegalArgumentException(String.format("No Method named %s Exists on the Proxy Object", method.getName()));
        }
        return this.handlerMap.get(this.methodMap.get(method)).invoke(proxy, method, args);
    }
}
