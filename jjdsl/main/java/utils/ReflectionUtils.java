package utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.stream.Stream;

public class ReflectionUtils {
    private ReflectionUtils() {
    }


    public static void setField(Object targetClass, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        try {
            Field targetField = getClassField(targetClass, fieldName);
            targetField.set(targetClass, value);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static <T> T getField(Object targetClass, String fieldName, Class<T> responseClass) throws NoSuchFieldException, IllegalAccessException {
        try {
            return (T) getUntypedField(targetClass, fieldName);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static <T> T callPrivateStaticFunction(Class<?> targetClass, String functionName, Class<T> responseClass, Object... args) throws InvocationTargetException, IllegalAccessException {
        try {
            Method targetMethod = Stream.of(targetClass.getDeclaredMethods())
                    .filter(method -> method.getName().equals(functionName))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(String.format("Failed to find Method %s on Class %s", functionName, targetClass.getSimpleName())));
            targetMethod.setAccessible(true);
            return (T) targetMethod.invoke(targetClass, args);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static Object getUntypedField(Object targetClass, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        try {
            Field targetField = getClassField(targetClass, fieldName);
            return targetField.get(targetClass);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private static Field getClassField(Object targetClass, String fieldName) throws NoSuchFieldException {
        Field targetField = targetClass.getClass().getDeclaredField(fieldName);
        targetField.setAccessible(true);
        return targetField;
    }
}
