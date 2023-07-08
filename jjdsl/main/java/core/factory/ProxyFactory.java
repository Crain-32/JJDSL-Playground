package core.factory;

import core.handlers.ProxyMethodHandler;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

public class ProxyFactory {
    private ProxyFactory() {
    }

    private static final List<Object> managedProxies = new ArrayList<>();


    public static <T> T getOrCreateManagedProxy(Class<T> classToGet) {
        for (Object obj : managedProxies) {
            if (obj.getClass().isAssignableFrom(classToGet)) {
                return (T) obj;
            }
        }
        // Just going to create and then manage the Proxy
        Object newClass = createUnmanagedProxy(classToGet);
        managedProxies.add(newClass);
        return (T) newClass;
    }

    public static <T> T createUnmanagedProxy(Class<T> classToGet) {
        return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class[]{classToGet},
                new ProxyMethodHandler(classToGet));
    }
}
