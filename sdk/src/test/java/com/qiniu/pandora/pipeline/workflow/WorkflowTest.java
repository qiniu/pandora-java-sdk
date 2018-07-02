package com.qiniu.pandora.pipeline.workflow;

import com.qiniu.pandora.common.PandoraClient;
import com.qiniu.pandora.common.PandoraClientImpl;
import com.qiniu.pandora.common.QiniuException;
import com.qiniu.pandora.common.TestConfig;
import com.qiniu.pandora.pipeline.PipelineClient;
import com.qiniu.pandora.pipeline.repo.CreateWorkflowInput;
import com.qiniu.pandora.pipeline.repo.GetWorkflowOutput;
import com.qiniu.pandora.pipeline.repo.GetWorkflowStatus;
import com.qiniu.pandora.pipeline.repo.UpdateWorkflowInput;
import com.qiniu.pandora.util.Auth;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WorkflowTest {
    private PipelineClient pipelineClient;
    private String workflowName = TestConfig.WORKFLOW_NAME;

    @Before
    public void setUp() throws Exception {
        Auth auth = Auth.create(TestConfig.ACCESS_KEY, TestConfig.SECRET_KEY);
        PandoraClient client = new PandoraClientImpl(auth);
        this.pipelineClient = new PipelineClient(client);
    }


    @Test
    public void test001createWorkflow() {
        CreateWorkflowInput createWorkflowInput = new CreateWorkflowInput();
        createWorkflowInput.comment = "javasdktest";
        createWorkflowInput.region = "nb";
        createWorkflowInput.workflowName = workflowName;
        try {
            pipelineClient.createWorkflow(createWorkflowInput);
        } catch (QiniuException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void test002updateWorkflow() {
        UpdateWorkflowInput updateWorkflowInput = new UpdateWorkflowInput();
        updateWorkflowInput.region = "nb";
        updateWorkflowInput.workflowName = workflowName;
        updateWorkflowInput.nodes = null;
        try {
            pipelineClient.updateWorkflow(updateWorkflowInput);
        } catch (QiniuException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }


    @Test
    public void test003listWorkflows() {
        GetWorkflowOutput[] getWorkflowOutputs = new GetWorkflowOutput[0];
        try {
            getWorkflowOutputs = pipelineClient.listWorkflows();
            for (GetWorkflowOutput i : getWorkflowOutputs) {
                System.out.println(i.toString());
            }
        } catch (QiniuException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void test004getWorkflow() {

        GetWorkflowOutput getWorkflowOutput = null;
        try {
            getWorkflowOutput = pipelineClient.getWorkflow(workflowName);
            System.out.println(getWorkflowOutput.toString());
        } catch (QiniuException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test005getWorkflowsStatus() {
        GetWorkflowStatus getWorkflowStatus = null;
        try {
            getWorkflowStatus = pipelineClient.getWorkflowStatus(workflowName);
            System.out.println(getWorkflowStatus.toString());
        } catch (QiniuException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void test006workflowExists() {
        try {
            System.out.println(pipelineClient.getWorkflowStatus(workflowName));
        } catch (QiniuException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void test007deleteWorkflow() {
        try {
            pipelineClient.deleteWorkflow(workflowName);
        } catch (QiniuException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

}
