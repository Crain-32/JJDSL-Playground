package core.handlers;

import javax.script.Invocable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class EngineInvocationHandler implements InvocationHandler {
    private static Invocable scriptEngine;

    public static void setInvocable(Invocable invocable) {
        scriptEngine = invocable;
    }

    /**
     * @param command   Nashorn function to call
     * @param arguments arguments[0] is the source class if called from the private "runCommand"
     * @return result of the function, or "null" if function returns null/errors
     */
    public Object runCommand(String command, Object... arguments) {
        try {
            if (arguments.length == 0) {
                return scriptEngine.invokeFunction(command, "");
            }
            return scriptEngine.invokeFunction(command, arguments);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Object runCommand(String command, Object sourceClass, Object... arguments) {
        try {
            final boolean providedArguments = arguments != null;
            int argLength = providedArguments ? arguments.length : 0;
            Object[] adjustedArgs = new Object[argLength + 1];
            if (providedArguments) {
                System.arraycopy(arguments, 0, adjustedArgs, 1, argLength);
            }
            adjustedArgs[0] = sourceClass;
            return runCommand(command, adjustedArgs);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return runCommand(method.getName(), proxy, args);
    }
}
