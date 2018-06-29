package com.qiniu.pandora.pipeline;

import com.qiniu.pandora.common.Constants;
import com.qiniu.pandora.common.PandoraClient;
import com.qiniu.pandora.common.QiniuException;
import com.qiniu.pandora.http.Client;
import com.qiniu.pandora.http.Response;
import com.qiniu.pandora.pipeline.repo.*;
import com.qiniu.pandora.util.Json;
import com.qiniu.pandora.util.StringMap;


public class PipelineClient {
    private PandoraClient client;
    private String pipelineHost;

    public PipelineClient(PandoraClient client) {
        this(client, "https://pipeline.qiniu.com");
    }

    private PipelineClient(PandoraClient client, String pipelineHost) {
        this.pipelineHost = pipelineHost;
        this.client = client;
    }

    /***
     * 创建 workflow
     * @param workflowInput  create workflow params
     */
    public void createWorkflow(CreateWorkflowInput workflowInput) throws QiniuException {
        String postUrl = String.format("%s/v2/workflows/%s", this.pipelineHost, workflowInput.workflowName);
        String postBody = Json.encode(workflowInput);
        this.client.post(postUrl, postBody.getBytes(Constants.UTF_8), new StringMap(), Client.JsonMime).close();
    }

    /**
     * 更新 workflow
     *
     * @param updateWorkflowInput update workflow params
     */
    public void updateWorkflow(UpdateWorkflowInput updateWorkflowInput) throws QiniuException {
        String putUrl = String.format("%s/v2/workflows/%s", this.pipelineHost, updateWorkflowInput.workflowName);
        String putBody = Json.encode(updateWorkflowInput);
        this.client.put(putUrl, putBody.getBytes(Constants.UTF_8), new StringMap(), Client.JsonMime).close();
    }

    /**
     * 删除 workflow
     *
     * @param workflowName workflow name
     */
    public void deleteWorkflow(String workflowName) throws QiniuException {
        String deleteUrl = String.format("%s/v2/workflows/%s", this.pipelineHost, workflowName);
        this.client.delete(deleteUrl, new StringMap()).close();
    }

    /**
     * 获取 workflow 信息
     *
     * @param workflowName workflow name
     * @return GetWorkflowOutput
     */
    public GetWorkflowOutput getWorkflow(String workflowName) throws QiniuException {
        String getUrl = String.format("%s/v2/workflows/%s", this.pipelineHost, workflowName);
        Response response = this.client.get(getUrl, new StringMap());
        System.out.println(response.bodyString());
        return response.jsonToObject(GetWorkflowOutput.class);
    }

    /**
     * 获取 workflow 状态
     *
     * @param workflowName workflow name
     * @return WorkflowStatus
     */
    public GetWorkflowStatus getWorkflowStatus(String workflowName) throws QiniuException {
        String getUrl = String.format("%s/v2/workflows/%s/status", this.pipelineHost, workflowName);
        Response response = this.client.get(getUrl, new StringMap());
        return response.jsonToObject(GetWorkflowStatus.class);
    }

    /**
     * 获取 workflow 列表
     *
     * @return GetWorkflowOutput[]
     */
    public GetWorkflowOutput[] listWorkflows() throws QiniuException {
        String getUrl = String.format("%s/v2/workflows", this.pipelineHost);
        Response response = this.client.get(getUrl, new StringMap());
        return response.jsonToObject(GetWorkflowOutput[].class);
    }

    /**
     * 启动 workflow
     *
     * @param workflowName workflow name
     */
    public void startWorkflow(String workflowName) throws QiniuException {
        String postUrl = String.format("%s/v2/workflows/%s/start", this.pipelineHost, workflowName);
        this.client.post(postUrl, null, new StringMap(), "").close();
    }

    /**
     * 停止 workflow
     *
     * @param workflowName workflow name
     */
    public void stopWorkflow(String workflowName) throws QiniuException {
        String postUrl = String.format("%s/v2/workflows/%s/stop", this.pipelineHost, workflowName);
        this.client.post(postUrl, null, new StringMap(), "").close();
    }

    /**
     * 查看 workflow 是否存在
     *
     * @param workflowName workname
     * @return boolean
     */
    public boolean workflowExists(String workflowName) throws QiniuException {
        boolean exists = false;
        String getUrl = String.format("%s/v2/workflows/%s", this.pipelineHost, workflowName);
        try {
            this.client.get(getUrl, new StringMap()).close();
            exists = true;
        } catch (QiniuException e) {
            //pass
        }
        return exists;
    }

    /**
     * 创建 repo
     *
     * @param repoName  repo name
     * @param repoInput repo extra param
     */
    public void createRepo(String repoName, CreateRepoInput repoInput) throws Exception {
        String postUrl = String.format("%s/v2/repos/%s", this.pipelineHost, repoName);
        String postBody = Json.encode(repoInput);
        this.client.post(postUrl, postBody.getBytes(Constants.UTF_8), new StringMap(), Client.JsonMime).close();
    }

    /**
     * 检查repo是否存在
     *
     * @param repoName repo name
     */
    public boolean repoExists(String repoName) {
        boolean exists = false;
        String getUrl = String.format("%s/v2/repos/%s", this.pipelineHost, repoName);
        try {
            this.client.get(getUrl, new StringMap()).close();
            exists = true;
        } catch (QiniuException e) {
            //pass
        }
        return exists;
    }


    /**
     * 创建导出到LOGDB，TSDB，HTTP等
     *
     * @param repoName    repo name
     * @param exportName  export name
     * @param exportInput export extra param
     */
    public void createExport(String repoName, String exportName, CreateExportInput exportInput) throws Exception {
        String postUrl = String.format("%s/v2/repos/%s/exports/%s", this.pipelineHost, repoName, exportName);
        String postBody = Json.encode(exportInput);
        this.client.post(postUrl, postBody.getBytes(Constants.UTF_8), new StringMap(), Client.JsonMime).close();
    }

    /*
    * 检查导出是否存在
    * @param repoName repo name
    * @param exportName export name
    * */
    public boolean exportExists(String repoName, String exportName) {
        boolean exists = false;
        String getUrl = String.format("%s/v2/repos/%s/exports/%s", this.pipelineHost, repoName, exportName);
        try {
            this.client.get(getUrl, new StringMap()).close();
            exists = true;
        } catch (QiniuException e) {
            //pass
        }
        return exists;
    }

}
