package utils;

import core.source.FunctionScanner;
import core.source.SourceGrabber;

public class SourceUtils {
    private SourceUtils() {
    }

    public static void getDefaultSource() throws Exception {
        getSource("https://github.com/Crain-32/JJDSL-Source", "js");
    }

    public static void getSource(String targetRepo, String targetBranch) throws Exception {
        SourceGrabber grabber = new SourceGrabber(targetRepo, targetBranch);
        grabber.cloneRepo();
        FunctionScanner.getSortedSourceList().forEach(grabber::executeSource);
        grabber.clearRepo();
        grabber.shutdownGrabbing();
    }
}
