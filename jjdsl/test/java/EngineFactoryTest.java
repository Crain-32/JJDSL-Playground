import core.engine.loader.EngineLoader;
import core.engine.loader.LoaderType;
import core.engine.loader.impl.DataClassEngineLoader;
import core.factory.EngineFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import utils.ReflectionUtils;

public class EngineFactoryTest {

    @Test
    void itShould_BeCallableThroughReflection() throws Exception {
        EngineLoader engineLoader = ReflectionUtils.callPrivateStaticFunction(EngineFactory.class, "getEngineLoader", EngineLoader.class, LoaderType.DATA);
        Assertions.assertEquals(DataClassEngineLoader.class, engineLoader.getClass());
    }
}
