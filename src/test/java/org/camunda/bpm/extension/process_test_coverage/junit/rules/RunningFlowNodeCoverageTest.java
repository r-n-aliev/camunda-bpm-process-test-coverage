package org.camunda.bpm.extension.process_test_coverage.junit.rules;

import static org.camunda.bpm.extension.process_test_coverage.junit.rules.CoverageTestProcessConstants.PROCESS_DEFINITION_KEY;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.camunda.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;

/**
 * This test should register a still uncompleted/running flow node as such. The
 * color of the flow node should differ from completed flow nodes in the
 * coverage diagram.
 * 
 * @author z0rbas
 *
 */
@Deployment(resources = { RunningFlowNodeCoverageTest.BPMN_PATH })
public class RunningFlowNodeCoverageTest {

    public static final String BPMN_PATH = "processStillRunning.bpmn";

    @Rule
    public TestCoverageProcessEngineRule rule = TestCoverageProcessEngineRuleBuilder.create().build();

    // TODO implement incident coverage handling and refactor this test to a
    // separate class
    @Test(expected = ProcessEngineException.class)
    public void shouldCoverGatewayAsStillRunningWithException() {

        rule.getRuntimeService().startProcessInstanceByKey(PROCESS_DEFINITION_KEY);

    }

    @Test
    public void shouldCoverManualTaskAsStillRunning() {

        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("path", "B");
        final ProcessInstance processInstance = rule.getRuntimeService().startProcessInstanceByKey(
                PROCESS_DEFINITION_KEY, variables);

        assertFalse("The process instance should still be running!", processInstance.isEnded());

        final Task runningTask = rule.getTaskService().createTaskQuery().active().taskDefinitionKey(
                "UserTask_B").singleResult();
        assertNotNull("One task instance should still be running!", runningTask);

    }

}
