package com.qiniu.pandora.pipeline.service;


import com.qiniu.pandora.common.PandoraClient;
import com.qiniu.pandora.common.QiniuException;
import com.qiniu.pandora.http.Client;
import com.qiniu.pandora.http.Response;
import com.qiniu.pandora.pipeline.InputOutput.CreateRepoInput;
import com.qiniu.pandora.pipeline.InputOutput.CreateWorkflowInput;
import com.qiniu.pandora.pipeline.InputOutput.RepoSchemaEntry;
import com.qiniu.pandora.pipeline.InputOutput.UpdateWorkflowInput;
import com.qiniu.pandora.util.Json;
import com.qiniu.pandora.util.StringMap;

import java.nio.charset.Charset;
import java.util.List;


public class PipelineClient {
    private PandoraClient client;
    private String pipelineHost;

    public PipelineClient(PandoraClient client) {
        this(client, "https://pipeline.qiniu.com");
    }

    public PipelineClient(PandoraClient client, String pipelineHost) {
        this.pipelineHost = pipelineHost;
        this.client = client;
    }


    /**
     *  workflowclient
     */


    public void createWorkflow(CreateWorkflowInput workflowInput) throws QiniuException {
        String postUrl = String.format("%s/v2/workflows/%s", this.pipelineHost, workflowInput.workflowName);
        String postBody = Json.encode(workflowInput);
        this.client.post(postUrl, postBody.getBytes(Charset.forName("UTF-8")), new StringMap(), Client.JsonMime).close();
    }


    //update workflow
    // TODO: 2018/6/25
    public void updateWorkflow(UpdateWorkflowInput updateWorkflowInput) throws  QiniuException{
        String putUrl = String.format("%s/v2/workflows/%s",this.pipelineHost,updateWorkflowInput.workflowName);

    }



    //deleteworkflow
    public void deleteWorkflow(String workflowName) throws QiniuException {
        String deleteUrl =String.format("%s/v2/workflows/%s",this.pipelineHost,workflowName);
        this.client.delete(deleteUrl,new StringMap()).close();
    }

    //getWorkflow
    // return string
    public String  getWorkflow(String workflowName) throws  QiniuException{
        String getUrl =String.format("%s/v2/workflows/%s",this.pipelineHost,workflowName);
        Response response =this .client.get(getUrl,new StringMap());
        return response.bodyString();
    }



// get Workflow
// result :{"name":"ltqworkflow","appId":"1380435366","region":"nb","uuid":"bcoqhlpr3cs005ne99gg","nodes":[],"status":"Ready","changed":false}
    public String  getWorkflowStatus(String workflowName) throws  QiniuException{
        String getUrl=String.format("%s/v2/workflows/%s/status",this.pipelineHost,workflowName);
        Response response =this .client.get(getUrl,new StringMap());
        return response.bodyString();
    }

    public String  listWorkFlows() throws  QiniuException{
        String getUrl = String.format("%s/v2/workflows",this.pipelineHost);
        Response response= this.client.get(getUrl,new StringMap());
        return response.bodyString();
    }

    public void startWorkflow(String workflowName) throws QiniuException {
        String postUrl = String.format("%s/v2/workflows/%s/start", this.pipelineHost, workflowName);
        this.client.post(postUrl, null, new StringMap(), "").close();
    }

    public void stopWorkflow(String workflowName) throws  QiniuException{
        String postUrl= String.format("%s/v2/workflows/%s/stop",this.pipelineHost,workflowName);
        this.client.post(postUrl,null,new StringMap(),"").close();
    }



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


    public void createRepo(String repoName, CreateRepoInput repoInput) throws Exception {
        String postUrl = String.format("%s/v2/repos/%s", this.pipelineHost, repoName);
        String postBody = Json.encode(repoInput);
        this.client.post(postUrl, postBody.getBytes(Charset.forName("UTF-8")), new StringMap(), Client.JsonMime).close();
    }



    //old
    // list Repos
    public  String  listRepos() throws QiniuException{
        String getUrl =String.format("%s/v2/repos",this.pipelineHost);
        Response response =this.client.get(getUrl,new StringMap());
        return response.bodyString();
    }


    public String  deleteRepo(String repoName) throws  QiniuException{
        String deleteUrl =String.format("%s/v2/repos/%s",this.pipelineHost,repoName);
        Response response = this.client.delete(deleteUrl,new StringMap());
        return  response.bodyString();
    }

    public String listRepoWithDag()throws QiniuException{
        String getUrl =String.format("%s/v2/repos?withDag=true",this.pipelineHost);
        Response response= this.client.get(getUrl,new StringMap());
        return response.bodyString();

    }

    public String getRepo(String repoName) throws QiniuException{
        String getUrl= String.format("%s/v2/repos/%s",this.pipelineHost,repoName);
        Response response= this.client.get(getUrl,new StringMap());
        return response.bodyString();
    }


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

    public String getSampleData(String repoName,int count) throws  QiniuException{
        String getUrl=String.format("%s/v2/repos/%s/data?count=%d",this.pipelineHost,repoName,count);
        Response response=this.client.get(getUrl,new StringMap());
        return response.bodyString();
    }



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

    /**
     * Transform client
     *
     */

//    public void createTransfrom(){
//        String postUrl =String.format("%s/v2/repos/%s/transforms/%s/to/%s",this.pipelineHost,);
//
//
//    }

    public String ListTransfrom(String RepoName) throws  QiniuException {
        String getUrl = String.format("%s/v2/repos/%s/transforms", this.pipelineHost, RepoName);
        Response response = this.client.get(getUrl, new StringMap());
        return response.bodyString();
    }




}
