package com.qiniu.pandora.pipeline.service;


import com.qiniu.pandora.common.PandoraClient;
import com.qiniu.pandora.common.QiniuException;
import com.qiniu.pandora.http.Client;
import com.qiniu.pandora.http.Response;
import com.qiniu.pandora.pipeline.repo.*;

import com.qiniu.pandora.util.Json;
import com.qiniu.pandora.util.StringMap;

import java.nio.charset.Charset;


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

    /**
     * workflowclinet
     */

    /***
     * 创建工作流
     * @param workflowInput
     * @throws QiniuException
     */
    public void createWorkflow(CreateWorkflowInput workflowInput) throws QiniuException {
        String postUrl = String.format("%s/v2/workflows/%s", this.pipelineHost, workflowInput.workflowName);
        String postBody = Json.encode(workflowInput);
        this.client.post(postUrl, postBody.getBytes(Charset.forName("UTF-8")), new StringMap(), Client.JsonMime).close();
    }

    /**
     * 更新工作流
     *
     * @param updateWorkflowInput
     * @throws QiniuException
     */
    public void updateWorkflow(UpdateWorkflowInput updateWorkflowInput) throws QiniuException {
        String putUrl = String.format("%s/v2/workflows/%s", this.pipelineHost, updateWorkflowInput.workflowName);
        String putBody = Json.encode(updateWorkflowInput);
        this.client.put(putUrl, putBody.getBytes(Charset.forName("UTF-8")), new StringMap(), Client.JsonMime).close();
    }


    /**
     * 删除工作流
     *
     * @param workflowName
     * @throws QiniuException
     */

    public void deleteWorkflow(String workflowName) throws QiniuException {
        String deleteUrl = String.format("%s/v2/workflows/%s", this.pipelineHost, workflowName);
        this.client.delete(deleteUrl, new StringMap()).close();
    }


    /**
     * 获取工作流信息
     *
     * @param workflowName
     * @return
     * @throws QiniuException
     */
    public GetWorkflowOutput getWorkflow(String workflowName) throws QiniuException {
        String getUrl = String.format("%s/v2/workflows/%s", this.pipelineHost, workflowName);
        Response response = this.client.get(getUrl, new StringMap());
        return response.jsonToObject(GetWorkflowOutput.class);
    }


    /**
     * 获取工作流状态信息
     *
     * @param workflowName
     * @return
     * @throws QiniuException
     */
    public GetWorkflowStatus getWorkflowStatus(String workflowName) throws QiniuException {
        String getUrl = String.format("%s/v2/workflows/%s/status", this.pipelineHost, workflowName);
        Response response = this.client.get(getUrl, new StringMap());
        return response.jsonToObject(GetWorkflowStatus.class);
    }


    /**
     * 获取工作流列表
     *
     * @return
     * @throws QiniuException
     */
    public ListWorkflowOutput listWorkFlows() throws QiniuException {
        String getUrl = String.format("%s/v2/workflows", this.pipelineHost);
        Response response = this.client.get(getUrl, new StringMap());
        return response.jsonToObject(ListWorkflowOutput.class);
    }


    /**
     * 启动工作流
     *
     * @param workflowName
     * @throws QiniuException
     */
    public void startWorkflow(String workflowName) throws QiniuException {
        String postUrl = String.format("%s/v2/workflows/%s/start", this.pipelineHost, workflowName);
        this.client.post(postUrl, null, new StringMap(), "").close();
    }

    /**
     * 停止工作流
     *
     * @param workflowName
     * @throws QiniuException
     */
    public void stopWorkflow(String workflowName) throws QiniuException {
        String postUrl = String.format("%s/v2/workflows/%s/stop", this.pipelineHost, workflowName);
        this.client.post(postUrl, null, new StringMap(), "").close();
    }

    /**
     * @param workflowName
     * @return 工作流是否存在
     * @throws QiniuException
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
 * repoclient
 */


    /**
     * 创建repo
     *
     * @param repoInput
     * @throws Exception
     */
    public void createRepo(CreateRepoInput repoInput) throws Exception {
        String postUrl = String.format("%s/v2/repos/%s", this.pipelineHost, repoInput.RepoName);
        String postBody = Json.encode(repoInput);
        this.client.post(postUrl, postBody.getBytes(Charset.forName("UTF-8")), new StringMap(), Client.JsonMime).close();
    }


    /**
     * 更新repo
     *
     * @param updataRepoInput
     * @throws QiniuException
     */
    //@todo UpdataRepoInput
    public void updateRepo(UpdataRepoInput updataRepoInput) throws QiniuException {
        String putUrl = String.format("%s/v2/repos/%s", this.pipelineHost, updataRepoInput.RepoName);
        String putBody = Json.encode(updataRepoInput);
        this.client.put(putUrl, putBody.getBytes(Charset.forName("UTF-8")), new StringMap(), Client.JsonMime).close();
    }


    /**
     * 获取 repo列表
     *
     * @return
     * @throws QiniuException
     */
    //@todo   Need to be test , need json
    public ListWorkflowOutput listRepos(ListReposInput listReposInput) throws QiniuException {
        if (listReposInput.WithDag) {
            this.listRepoWithDag(listReposInput);
        }
        String getUrl = String.format("%s/v2/repos", this.pipelineHost);
        Response response = this.client.get(getUrl, new StringMap());
        return response.jsonToObject(ListWorkflowOutput.class);
    }

    /**
     * repo withtag
     *
     * @return
     * @throws QiniuException
     */
    public ListWorkflowOutput listRepoWithDag(ListReposInput listReposInput) throws QiniuException {
        String getUrl = String.format("%s/v2/repos", this.pipelineHost);
        Response response = this.client.get(getUrl, new StringMap());
        return response.jsonToObject(ListWorkflowOutput.class);
    }


    /**
     * 删除repo
     *
     * @param repoName
     * @throws QiniuException
     */
    public void deleteRepo(String repoName) throws QiniuException {
        String deleteUrl = String.format("%s/v2/repos/%s", this.pipelineHost, repoName);
        this.client.delete(deleteUrl, new StringMap()).close();

    }

    /**
     * 获取repo信息
     *
     * @param repoName
     * @return
     * @throws QiniuException
     */
    public GetRepoOutput getRepo(String repoName) throws QiniuException {
        String getUrl = String.format("%s/v2/repos/%s", this.pipelineHost, repoName);
        Response response = this.client.get(getUrl, new StringMap());
        return response.jsonToObject(GetRepoOutput.class);
    }

    /**
     * repo 是否存在
     *
     * @param repoName
     * @return
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
     * 查看队列数据 上限10条
     *
     * @param repoName
     * @param count
     * @return
     * @throws QiniuException
     */
    // v4/repos/reponame/data?count=10
    public SampleDataOutput getSampleData(String repoName, int count) throws QiniuException {
        String getUrl = String.format("%s/v2/repos/%s/data?count=%d", this.pipelineHost, repoName, count);
        Response response = this.client.get(getUrl, new StringMap());
        return response.jsonToObject(SampleDataOutput.class);
    }


    public String listGroups() throws QiniuException {
        String geturl = String.format("%s/v2/groups");
        Response response = this.client.get(geturl, new StringMap());
        return response.bodyString();

    }


}
