package com.qiniu.pandora.pipeline.pipelineclient;

import com.qiniu.pandora.common.PandoraClient;
import com.qiniu.pandora.common.PandoraClientImpl;
import com.qiniu.pandora.common.QiniuException;
import com.qiniu.pandora.common.TestConfig;
import com.qiniu.pandora.pipeline.PipelineClient;
import com.qiniu.pandora.pipeline.repo.*;
import com.qiniu.pandora.util.Auth;
import org.junit.runners.MethodSorters;
import org.junit.FixMethodOrder;

import org.junit.Before;
import org.junit.Test;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WorkflowTest {
    protected PipelineClient pipelineClient;

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
        createWorkflowInput.workflowName = "javasdk";
        pipelineClient.createWorkflow(createWorkflowInput);
    }

    @Test
    public void test002updateWorkflow() throws QiniuException {
        UpdateWorkflowInput updateWorkflowInput = new UpdateWorkflowInput();
        updateWorkflowInput.region = "nb";
        updateWorkflowInput.workflowName = "javasdk";
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
        String workflowname = "javasdk";
        GetWorkflowOutput getWorkflowOutput = pipelineClient.getWorkflow(workflowname);
        System.out.println(getWorkflowOutput.toString());
    }

    @Test
    public void test005getWorkflowsStatus() throws QiniuException {
        String workflowname = "javasdk";
        GetWorkflowStatus getWorkflowStatus = pipelineClient.getWorkflowStatus(workflowname);
        System.out.println(getWorkflowStatus.toString());
    }


    @Test
    public void test006workflowExists() throws QiniuException {
        String workflowname = "javasdk";
        System.out.println(pipelineClient.getWorkflowStatus(workflowname));
    }

    @Test
    public void test007deleteWorkflow() throws QiniuException {
        String workflowname = "javasdk";
        pipelineClient.deleteWorkflow(workflowname);
    }

}
