package core.source;

import core.annnotation.Source;
import exception.GitException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class SourceGrabber {
    private final String targetRepo;
    private final String targetBranch;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private final AtomicInteger nextFileName = new AtomicInteger();

    private static final String cloneCommand = "/bin/git clone %s source";
    private static final String switchCommand = "/bin/git -C source switch %s";
    private static final String checkoutCommand = "/bin/git -C source checkout %s";
    private static final String mvCommands = "mv ./source/file.js ./scripts/%s.js";
    private static final String mkdirCommand = "/bin/mkdir %s";
    private static final String rmCommand = "rm -f -r %s";

    public SourceGrabber(String targetRepo, String targetBranch) {
        this.targetRepo = targetRepo;
        this.targetBranch = targetBranch;
    }


    public void cloneRepo() throws GitException {
        List<String> commands = new ArrayList<>();
        commands.add("pwd");
        commands.add(String.format(mkdirCommand, "scripts"));
        commands.add(String.format(cloneCommand, targetRepo));

        if (this.targetBranch != null) {
            commands.add(String.format(switchCommand, targetBranch));
        }

        runScript(commands);
    }

    public void clearRepo() {
        runScript(Collections.singletonList(String.format(rmCommand, "source")));
    }

    public void executeSource(Source source) throws GitException {
        List<String> commands = new ArrayList<>();
        commands.add("pwd");
        commands.add(String.format(checkoutCommand, source.value()));

        int nextFile = nextFileName.getAndIncrement();
        commands.add(String.format(mvCommands, nextFile));
        runScript(commands);
    }

    public void shutdownGrabbing() {
        executorService.shutdown();
    }


    private void runScript(List<String> commands) throws GitException {
        for (String script : commands) {
            try {
                script = String.format(script);
                Process process = Runtime.getRuntime().exec(script);
                System.out.println(script);
                StreamGobbler gobbler = new StreamGobbler(process.getInputStream(), process.getErrorStream(), System.out::println, System.err::println);
                Future<?> future = executorService.submit(gobbler);
                int exitCode = process.waitFor();
                future.get(10, TimeUnit.SECONDS);
                if (exitCode != 0) {
                    System.out.println("Oh No");
                }
                process.destroy();
            } catch (Exception e) {
                e.printStackTrace();
                throw new GitException("Failed to Execute: " + script, e);
            }
        }
    }


    private static class StreamGobbler implements Runnable {
        private final InputStream stdStream;
        private final InputStream errorStream;
        private final Consumer<String> consumer;
        private final Consumer<String> errorOutput;

        public StreamGobbler(InputStream stdStream, InputStream errorStream, Consumer<String> consumer, Consumer<String> errorOutput) {
            this.stdStream = stdStream;
            this.errorStream = errorStream;
            this.consumer = consumer;
            this.errorOutput = errorOutput;
        }

        @Override
        public void run() {
            new BufferedReader(new InputStreamReader(stdStream)).lines().forEach(consumer);
            new BufferedReader(new InputStreamReader(errorStream)).lines().forEach(errorOutput);
        }
    }
}
