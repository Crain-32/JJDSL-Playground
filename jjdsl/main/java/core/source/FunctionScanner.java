package core.source;

import core.annnotation.Source;
import monkey.functions.LexerFunctions;

import java.util.*;
import java.util.stream.Collectors;

public class FunctionScanner {

    private static final List<Class<?>> SOURCE_CLASSES = Collections.unmodifiableList(
            Arrays.asList(LexerFunctions.class)
    );

    private FunctionScanner() {
    }

    /**
     * Returns a list of all {@link Source Source Annotations}
     * Presorted by the execution order.
     */
    public static List<Source> getSortedSourceList() {
        List<Source> totalList = getSourceList();
        totalList.sort(Comparator.comparingInt(Source::order));
        return totalList;
    }

    public static List<Source> getSourceList() {
        List<Source> totalList = new ArrayList<>(SOURCE_CLASSES.size() * 3);
        SOURCE_CLASSES.stream().map(FunctionScanner::scanClassForSource).forEach(totalList::addAll);
        return totalList;
    }

    private static List<Source> scanClassForSource(Class<?> potentialClass) {
        return Arrays.stream(potentialClass.getMethods())
                .filter(method -> method.isAnnotationPresent(Source.class))
                .map(method -> method.getAnnotation(Source.class))
                .collect(Collectors.toList());
    }
}
