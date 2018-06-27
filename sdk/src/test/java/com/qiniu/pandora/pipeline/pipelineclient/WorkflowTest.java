package com.qiniu.pandora.pipeline.pipelineclient;

import com.qiniu.pandora.common.PandoraClient;
import com.qiniu.pandora.common.PandoraClientImpl;
import com.qiniu.pandora.common.QiniuException;
import com.qiniu.pandora.pipeline.common.TestConfig;
import com.qiniu.pandora.pipeline.service.PipelineClient;
import com.qiniu.pandora.pipeline.repo.*;
import com.qiniu.pandora.util.Auth;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;


public class WorkflowTest {
    protected PipelineClient pipelineClient;

    @Before
    public void setUP() throws Exception {
        Auth auth = Auth.create(TestConfig.ACCESS_KEY, TestConfig.SECRET_KEY);
        PandoraClient client = new PandoraClientImpl(auth);
        this.pipelineClient = new PipelineClient(client);
    }


    @Test
    public void testcreateWorkflow() throws QiniuException {
        CreateWorkflowInput createWorkflowInput = new CreateWorkflowInput();
        createWorkflowInput.comment = "javasdktest";
        createWorkflowInput.region = "nb";
        createWorkflowInput.workflowName = "javasdk";
        pipelineClient.createWorkflow(createWorkflowInput);
    }

    @Test
    public void testupdateWorkflow() throws QiniuException {
        UpdateWorkflowInput updateWorkflowInput = new UpdateWorkflowInput();
        updateWorkflowInput.region = "nb";
        updateWorkflowInput.workflowName = "javasdk";
        pipelineClient.updateWorkflow(updateWorkflowInput);
    }



    @Test
    public void testlistWorkflows() throws QiniuException {
        GetWorkflowOutput[] getWorkflowOutputs = pipelineClient.listWorkflows();
        for (GetWorkflowOutput i : getWorkflowOutputs) {
            System.out.println(i.toString());
        }
    }

    @Test
    public void testgetWorkflow() throws QiniuException {
        String workflowname = "javasdk";
        GetWorkflowOutput getWorkflowOutput = pipelineClient.getWorkflow(workflowname);
        System.out.println(getWorkflowOutput.toString());
    }

    @Test
    public void testgetWorkflowsStatus() throws QiniuException {
        String workflowname = "javasdk";
        GetWorkflowStatus getWorkflowStatus = pipelineClient.getWorkflowStatus(workflowname);
        System.out.println(getWorkflowStatus.toString());
    }


    @Test
    public void testworkflowExists() throws QiniuException {
        String workflowname = "javasdk";
        System.out.println(pipelineClient.getWorkflowStatus(workflowname));
    }

    @Test
    public void testdeleteWorkflow() throws QiniuException {
        String workflowname = "javasdk";
        pipelineClient.deleteWorkflow(workflowname);
    }

}
