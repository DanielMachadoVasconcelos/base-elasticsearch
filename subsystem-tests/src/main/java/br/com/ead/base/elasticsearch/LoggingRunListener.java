package br.com.ead.base.elasticsearch;

import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.TestPlan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingRunListener implements TestExecutionListener {

    private static final Logger LOG = LoggerFactory.getLogger("TestRunner");

    public void testPlanExecutionStarted(TestPlan testPlan) {
        LOG.info("Started test run");
    }

    public void testPlanExecutionFinished(TestPlan testPlan) {
        LOG.info("Test finished.");
    }

    public void executionStarted(TestIdentifier testIdentifier) {
        LOG.info("Running {}", testIdentifier.getDisplayName());
    }

    public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
        if (testExecutionResult.getStatus().equals(TestExecutionResult.Status.SUCCESSFUL)) {
            LOG.info("Test finished {}", testIdentifier.getDisplayName());
        } else if (testExecutionResult.getStatus().equals(TestExecutionResult.Status.FAILED)){
            LOG.warn("FAILED: {} - {}",testIdentifier.getDisplayName(), testExecutionResult.getThrowable().map(Throwable::getMessage).orElse("[NO ERROR MESSAGE]"));
            if (testExecutionResult.getThrowable().isPresent()) {
                LOG.error("Test failed", testExecutionResult.getThrowable().get());
            }
        } else {
            LOG.info("Ignoring {}", testIdentifier.getDisplayName());
        }
    }

    public void executionSkipped(TestIdentifier testIdentifier, String reason) {
        LOG.info("Skipped {}. Reason {} ", testIdentifier.getDisplayName(), reason);
    }
}