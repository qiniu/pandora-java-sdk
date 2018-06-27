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
     * 创建repo
     *
     * @param repoInput
     * @throws Exception
     */
    public void createRepo(CreateRepoInput repoInput) throws Exception {
        String postUrl = String.format("%s/v2/repos/%s", this.pipelineHost, repoInput.repoName);
        String postBody = Json.encode(repoInput);
        this.client.post(postUrl, postBody.getBytes(Charset.forName("UTF-8")), new StringMap(), Client.JsonMime).close();
    }


    /**
     * 更新repo
     *
     * @param updataRepoInput
     * @throws QiniuException
     */
    public void updateRepo(UpdataRepoInput updataRepoInput) throws QiniuException {
        String putUrl = String.format("%s/v2/repos/%s", this.pipelineHost, updataRepoInput.repoName);
        String putBody = Json.encode(updataRepoInput);
        this.client.put(putUrl, putBody.getBytes(Charset.forName("UTF-8")), new StringMap(), Client.JsonMime).close();
    }


    /**
     * 获取 repo列表
     *
     * @return
     * @throws QiniuException
     */
    public ListWorkflowOutput listRepos(ListReposInput listReposInput) throws QiniuException {
        if (listReposInput.withDag) {
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
        String getUrl = String.format("%s/v2/repos?withDag=true", this.pipelineHost);
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
     * 查看数据 上限10条
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


    /**
     * 创建 transform
     *
     * @param createTransformInput
     * @throws QiniuException
     */
    public void createTransform(CreateTransformInput createTransformInput) throws QiniuException {
        String postUrl = String.format("%s/v2/repos/%s/transforms/%s/to/%s", this.pipelineHost,
                createTransformInput.srcRepoNmae,
                createTransformInput.transfornName,
                createTransformInput.destRepoName);
        String postBody = Json.encode(createTransformInput.spec);
        this.client.post(postUrl, postBody.getBytes(Charset.forName("UTF-8")), new StringMap(), Client.JsonMime).close();
    }

    /**
     * 更新 transform
     *
     * @param updateTransformInput
     * @throws QiniuException
     */
    public void updateTransform(UpdateTransformInput updateTransformInput) throws QiniuException {
        String putUrl = String.format("%s/v2/repos/%s/transforms/%s", this.pipelineHost,
                updateTransformInput.srcRepoName,
                updateTransformInput.transformName);
        String putBody = Json.encode(updateTransformInput.spec);
        this.client.put(putUrl, putBody.getBytes(Charset.forName("UTF-8")), new StringMap(), Client.JsonMime).close();
    }

    /**
     * 获取transform信息
     * @param  repoName
     * @param transformName
     * @return
     * @throws QiniuException
     */

    public TransformSpec getTransform(String repoName, String transformName) throws QiniuException {
        String getUrl = String.format("%s/v2/repos/%s/transforms/%s", this.pipelineHost, repoName, transformName);
        Response response = this.client.get(getUrl, new StringMap());
        return response.jsonToObject(TransformSpec.class);
    }

    /**
     * 获取 transform 列表
     *
     * @param repoName
     * @return
     * @throws QiniuException
     */
    public ListTransformsOutput listTransform(String repoName) throws QiniuException {
        String getUrl = String.format("%s/v2/repos/%s/transforms", this.pipelineHost, repoName);
        Response response = this.client.get(getUrl, new StringMap());
        return response.jsonToObject(ListTransformsOutput.class);
    }

    /**
     * 删除 transform
     *
     * @param repoName
     * @param transformName
     * @throws QiniuException
     */

    public void deleteRepo(String repoName, String transformName) throws QiniuException {
        String deleteUrl = String.format("%s/v2/repos/%s/transforms/%s", this.pipelineHost, repoName, transformName);
        this.client.delete(deleteUrl, new StringMap());
    }

    /**
     * transform 是否存在
     *
     * @param repoName
     * @param transformName
     * @return
     */
    public boolean transformExists(String repoName, String transformName) {
        boolean exists = false;
        String getUrl = String.format("%s/v2/repos/%s/transforms/%s/exists", this.pipelineHost, repoName, transformName);
        try {
            this.client.get(getUrl, new StringMap()).close();
            exists = true;
        } catch (QiniuException e) {
            //pass
        }
        return exists;
    }


    /**
     * 创建 export
     *
     * @param createExportInput
     * @throws QiniuException
     */
    public void createExport(CreateExportInput createExportInput) throws QiniuException {
        String postUrl = String.format("%s/v2/repos/%s/exports/%s", this.pipelineHost,
                createExportInput.repoName,
                createExportInput.exportName);
        String postBody = Json.encode(createExportInput.spec);
        this.client.post(postUrl, postBody.getBytes(Charset.forName("UTF-8")), new StringMap(), Client.JsonMime).close();
    }

    /**
     * 更新export
     * @param updateExportInput
     * @throws QiniuException
     */
    public void updateExport(UpdateExportInput updateExportInput) throws QiniuException {
        String putUrl = String.format("%s/v2/repos/%s/exports/%s", this.pipelineHost,
                updateExportInput.repoName,
                updateExportInput.exportName);
        String putBody = Json.encode(updateExportInput.spec);
        this.client.put(putUrl, putBody.getBytes(Charset.forName("UTF-8")), new StringMap(), Client.JsonMime).close();
    }

    /**
     * 列举export信息
     * @param repoName
     * @return
     * @throws QiniuException
     */
    public ListExportsOutput listExports(String repoName) throws QiniuException {
        String getUrl = String.format("%s/v2/repos/%s/exports", this.pipelineHost, repoName);
        Response response = this.client.get(getUrl, new StringMap());
        return response.jsonToObject(ListExportsOutput.class);
    }

    /**
     * 获取 export信息
     *
     * @param repoName
     * @param exportName
     * @return
     * @throws QiniuException
     */
    public ExportDesc getExport(String repoName, String exportName) throws QiniuException {
        String getUrl = String.format("%s/v2/repos/%s/exports/%s", this.pipelineHost, repoName, exportName);
        Response response = this.client.get(getUrl, new StringMap());
        return response.jsonToObject(ExportDesc.class);
    }

    /**
     * 删除 export
     *
     * @param repoName
     * @param exportName
     * @throws QiniuException
     */
    public void deleteExport(String repoName, String exportName) throws QiniuException {
        String deleteUrl = String.format("%s/v2/repos/%s/exports/%s", this.pipelineHost, repoName, exportName);
        this.client.delete(deleteUrl, new StringMap());
    }

    /**
     * 创建 Datasource
     *
     * @param createDatasourceInput
     * @throws QiniuException
     */
    public void createDatasource(CreateDatasourceInput createDatasourceInput) throws QiniuException {
        String postUrl = String.format("%s/v2/datasources/%s", this.pipelineHost, createDatasourceInput.datasourcename);
        String postBody = Json.encode(createDatasourceInput);
        this.client.post(postUrl, postBody.getBytes(Charset.forName("UTF-8")), new StringMap(), Client.JsonMime).close();
    }

    /**
     * 获取 Datasource 信息
     *
     * @param datasourceName
     * @return
     * @throws QiniuException
     */
    public GetDatasourceOutput getDatasource(String datasourceName) throws QiniuException {
        String getUrl = String.format("%s/v2/datasources/%s", this.pipelineHost, datasourceName);
        Response response = this.client.get(getUrl, new StringMap());
        return response.jsonToObject(GetDatasourceOutput.class);
    }

    /**
     * 查看 Datasource 是否存在
     *
     * @param datasourceName
     * @return
     */
    public boolean datesourceExists(String datasourceName) {
        boolean exists = false;
        String getUrl = String.format("%s/v2/datasources/%s/exists", this.pipelineHost, datasourceName);
        try {
            this.client.get(getUrl, new StringMap()).close();
            exists = true;
        } catch (QiniuException e) {
            //pass
        }
        return exists;
    }

    /**
     * 列举Datasource 信息
     *
     * @return
     * @throws QiniuException
     */
    public ListDatasourcesOutput listDatasources() throws QiniuException {
        String getUrl = String.format("%s/v2/datasources", this.pipelineHost);
        Response response = this.client.get(getUrl, new StringMap());
        return response.jsonToObject(ListDatasourcesOutput.class);
    }

    /**
     * 删除Datasource
     *
     * @param datasourceName
     * @throws QiniuException
     */
    public void deleteDatasource(String datasourceName) throws QiniuException {
        String deleteUrl = String.format("%s/v2/datasources/%s", this.pipelineHost, datasourceName);
        this.client.delete(deleteUrl, new StringMap());
    }



}
