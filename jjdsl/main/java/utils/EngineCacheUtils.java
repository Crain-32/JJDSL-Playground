package utils;

import core.factory.EngineFactory;
import core.handlers.EngineInvocationHandler;
import core.source.constant.SourceCheckType;

import javax.script.Invocable;
import java.lang.ref.WeakReference;

public class EngineCacheUtils {
    private static WeakReference<Invocable> invocableWeakReference;

    private EngineCacheUtils() {
    }

    public static void checkSourceAndCreateSetEngine(SourceCheckType checkType) throws Exception {
        SourceCacheUtils.sourceCacheAction(checkType);
        if (SourceCacheUtils.checkCacheState()) {
            createAndSetEngine();
        } else {
            throw new IllegalStateException("Source Cache Check Failed, Check Console for more information");
        }
    }

    public static void createAndSetEngine() throws Exception {
        Invocable invocable = EngineFactory.createEngine();
        invocableWeakReference = new WeakReference<>(invocable);
        EngineInvocationHandler.setInvocable(invocable);
    }

    public static Invocable getOrCreateEngine() throws Exception {
        Invocable invocable = null;
        if (invocableWeakReference != null && invocableWeakReference.get() != null) {
            invocable = invocableWeakReference.get();
        }
        if (invocable == null) {
            invocable = EngineFactory.createEngine();
            invocableWeakReference = new WeakReference<>(invocable);
        }
        return invocable;
    }
}
