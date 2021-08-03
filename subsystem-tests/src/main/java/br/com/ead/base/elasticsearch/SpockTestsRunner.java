package br.com.ead.base.elasticsearch;

import org.junit.platform.engine.discovery.ClassSelector;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spock.lang.Specification;

import java.util.List;
import java.util.Set;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

public class SpockTestsRunner {

    private static final Logger LOG = LoggerFactory.getLogger("SpockTestsRunner");

    public static void main(String[] args) {
        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                .selectors(getSuite())
                .build();

        Launcher launcher = LauncherFactory.create();

        // Register a listener of your choice
        SummaryGeneratingListener listener = new SummaryGeneratingListener();
        launcher.registerTestExecutionListeners(listener);
        launcher.registerTestExecutionListeners(new LoggingRunListener());

        launcher.execute(request);

        TestExecutionSummary summary = listener.getSummary();
        // Do something with the TestExecutionSummary.
        LOG.info("Number of Executed tests {}",summary.getTestsFoundCount());
        LOG.info("Number of Failed tests {}",summary.getTestsFailedCount());
        LOG.info("Number of Skipped tests {}",summary.getTestsSkippedCount());
        System.exit(summary.getFailures().isEmpty() ? 0 : 1);
    }

//    public static void main(String[] args) throws IOException {
//        configureLogging();
//        if (args.length > 0) {
//            LOG.info("Loading properties from {}", args[0]);
//            try (FileInputStream propertiesFile = new FileInputStream(args[0])) {
//                Properties properties = System.getProperties();
//                properties.load(propertiesFile);
//            }
//        }
//        Computer computer = new Computer();
//
//        JUnitCore jUnitCore = new JUnitCore();
//        jUnitCore.addListener(new LoggingRunListener());
//
//        Result result = jUnitCore.run(computer, getSuite());
//        System.exit(result.wasSuccessful() ? 0 : 1);
//    }
//
//    private static void configureLogging() {
//        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
//        loggerContext.getLogger("root").setLevel(Level.INFO);
//        loggerContext.getLogger("org.apache.kafka").setLevel(Level.WARN);
//    }
//
    private static List<ClassSelector> getSuite() {
        Reflections reflections = new Reflections("br.com.ead.base.elasticsearch.subsystemtests.spock");
        Set<Class<? extends Specification>> spockTests = reflections.getSubTypesOf(Specification.class);
        return spockTests.stream().map(clazz -> selectClass(clazz)).toList();
    }
}
