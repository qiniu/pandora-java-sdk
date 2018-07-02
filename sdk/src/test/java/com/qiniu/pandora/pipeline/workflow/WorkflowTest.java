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
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WorkflowTest {
    private PipelineClient pipelineClient;
    private String workflowName = TestConfig.WORKFLOW_NAME;

    @Before
    public void setUP() throws Exception {
        Auth auth = Auth.create(TestConfig.ACCESS_KEY, TestConfig.SECRET_KEY);
        PandoraClient client = new PandoraClientImpl(auth);
        this.pipelineClient = new PipelineClient(client);
    }


    @Test
    public void test001createWorkflow() throws QiniuException {
        CreateWorkflowInput createWorkflowInput = new CreateWorkflowInput();
        createWorkflowInput.comment = "javasdktest";
        createWorkflowInput.region = "nb";
        createWorkflowInput.workflowName = workflowName;
        pipelineClient.createWorkflow(createWorkflowInput);
    }

    @Test
    public void test002updateWorkflow() throws QiniuException {
        UpdateWorkflowInput updateWorkflowInput = new UpdateWorkflowInput();
        updateWorkflowInput.region = "nb";
        updateWorkflowInput.workflowName = workflowName;
        updateWorkflowInput.nodes = null;
        pipelineClient.updateWorkflow(updateWorkflowInput);
    }


    @Test
    public void test003listWorkflows() throws QiniuException {
        GetWorkflowOutput[] getWorkflowOutputs = pipelineClient.listWorkflows();
        for (GetWorkflowOutput i : getWorkflowOutputs) {
            System.out.println(i.toString());
        }
    }

    @Test
    public void test004getWorkflow() throws QiniuException {

        GetWorkflowOutput getWorkflowOutput = pipelineClient.getWorkflow(workflowName);
        System.out.println(getWorkflowOutput.toString());
    }

    @Test
    public void test005getWorkflowsStatus() throws QiniuException {
        GetWorkflowStatus getWorkflowStatus = pipelineClient.getWorkflowStatus(workflowName);
        System.out.println(getWorkflowStatus.toString());
    }


    @Test
    public void test006workflowExists() throws QiniuException {
        System.out.println(pipelineClient.getWorkflowStatus(workflowName));
    }

    @Test
    public void test007deleteWorkflow() throws QiniuException {
        pipelineClient.deleteWorkflow(workflowName);
    }

}
