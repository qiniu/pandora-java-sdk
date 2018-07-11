package com.qiniu.pandora.pipeline;

import com.qiniu.pandora.common.Constants;
import com.qiniu.pandora.common.PandoraClient;
import com.qiniu.pandora.common.QiniuException;
import com.qiniu.pandora.http.Client;
import com.qiniu.pandora.http.Response;
import com.qiniu.pandora.pipeline.repo.*;
import com.qiniu.pandora.util.Json;
import com.qiniu.pandora.util.StringMap;

/**
 * 定义 Workflow 和 Pipeline Repo 的管理功能
 */
public class PipelineClient {
    private PandoraClient client;
    private String pipelineHost;

    public PipelineClient(PandoraClient client) {
        this(client, Constants.PIPELINE_HOST);
    }

    public PipelineClient(PandoraClient client, String pipelineHost) {
        this.pipelineHost = pipelineHost;
        this.client = client;
    }

    /***
     * 创建 workflow
     * @param workflowInput 创建 workflow 的参数
     * @throws QiniuException 异常
     */
    public void createWorkflow(CreateWorkflowInput workflowInput) throws QiniuException {
        String postUrl = String.format("%s/v2/workflows/%s", this.pipelineHost, workflowInput.workflowName);
        String postBody = Json.encode(workflowInput);
        this.client.post(postUrl, postBody.getBytes(Constants.UTF_8), new StringMap(), Client.JsonMime).close();
    }

    /**
     * 更新 workflow
     *
     * @param updateWorkflowInput 更新 workflow 的参数
     * @throws QiniuException 异常
     */
    public void updateWorkflow(UpdateWorkflowInput updateWorkflowInput) throws QiniuException {
        String putUrl = String.format("%s/v2/workflows/%s", this.pipelineHost, updateWorkflowInput.workflowName);
        String putBody = Json.encode(updateWorkflowInput);
        this.client.put(putUrl, putBody.getBytes(Constants.UTF_8), new StringMap(), Client.JsonMime).close();
    }

    /**
     * 删除 workflow
     *
     * @param workflowName workflow 名称
     * @throws QiniuException 异常
     */
    public void deleteWorkflow(String workflowName) throws QiniuException {
        String deleteUrl = String.format("%s/v2/workflows/%s", this.pipelineHost, workflowName);
        this.client.delete(deleteUrl, new StringMap()).close();
    }

    /**
     * 获取 workflow 信息
     *
     * @param workflowName workflow 名称
     * @return GetWorkflowOutput
     * @throws QiniuException 异常
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
     * @param workflowName workflow 名称
     * @return WorkflowStatus
     * @throws QiniuException 异常
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
     * @throws QiniuException 异常
     */
    public GetWorkflowOutput[] listWorkflows() throws QiniuException {
        String getUrl = String.format("%s/v2/workflows", this.pipelineHost);
        Response response = this.client.get(getUrl, new StringMap());
        return response.jsonToObject(GetWorkflowOutput[].class);
    }

    /**
     * 启动 workflow
     *
     * @param workflowName workflow 名称
     * @throws QiniuException 异常
     */
    public void startWorkflow(String workflowName) throws QiniuException {
        String postUrl = String.format("%s/v2/workflows/%s/start", this.pipelineHost, workflowName);
        this.client.post(postUrl, null, new StringMap(), "").close();
    }

    /**
     * 停止 workflow
     *
     * @param workflowName workflow 名称
     * @throws QiniuException 异常
     */
    public void stopWorkflow(String workflowName) throws QiniuException {
        String postUrl = String.format("%s/v2/workflows/%s/stop", this.pipelineHost, workflowName);
        this.client.post(postUrl, null, new StringMap(), "").close();
    }

    /**
     * 查看 workflow 是否存在
     *
     * @param workflowName workname
     * @return 当 workflow 存在时返回 true
     * @throws QiniuException 异常
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
     * 创建 pipeline repo
     *
     * @param repoName  repo 名称
     * @param repoInput 额外参数
     * @throws QiniuException 异常
     */
    public void createRepo(String repoName, CreateRepoInput repoInput) throws Exception {
        String postUrl = String.format("%s/v2/repos/%s", this.pipelineHost, repoName);
        String postBody = Json.encode(repoInput);
        this.client.post(postUrl, postBody.getBytes(Constants.UTF_8), new StringMap(), Client.JsonMime).close();
    }

    /**
     * 删除 pipeline repo
     *
     * @param repoName repo 名称
     * @throws QiniuException 异常
     */
    public void deleteRepo(String repoName) throws QiniuException {
        String deleteUrl = String.format("%s/v2/repos/%s", this.pipelineHost, repoName);
        this.client.delete(deleteUrl, new StringMap()).close();
    }

    /**
     * 检查 pipeline repo 是否存在
     *
     * @param repoName repo 名称
     * @return 当 repo 存在返回 true
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
     * 创建到 LogDB，TSDB，HTTP等的导出
     *
     * @param repoName    repo 名称
     * @param exportName  导出名称
     * @param exportInput 额外参数
     * @throws QiniuException 异常
     */
    public void createExport(String repoName, String exportName, CreateExportInput exportInput) throws Exception {
        String postUrl = String.format("%s/v2/repos/%s/exports/%s", this.pipelineHost, repoName, exportName);
        String postBody = Json.encode(exportInput);
        this.client.post(postUrl, postBody.getBytes(Constants.UTF_8), new StringMap(), Client.JsonMime).close();
    }

    /**
     * 删除到 LogDB，TSDB，HTTP等的导出
     *
     * @param repoName   repo 名称
     * @param exportName 导出名称
     * @throws QiniuException 异常
     */
    public void deleteExport(String repoName, String exportName) throws QiniuException {
        String deleteUrl = String.format("%s/v2/repos/%s/exports/%s", this.pipelineHost, repoName, exportName);
        this.client.delete(deleteUrl, new StringMap()).close();
    }

    /*
    * 检查导出是否存在
    * @param repoName repo 名称
    * @param exportName 导出名称
    * @return 当 repo 存在时返回 true
    * @throws QiniuException 异常
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
