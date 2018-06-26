package com.qiniu.pandora.pipeline.repo;





public class CreateTransformInput {

    public String SrcRepoNmae;
    public String DestRepoName;
    public String TransfornName;

    public CreateTransformInput(String srcRepoNmae, String destRepoName, String transfornName) {
        SrcRepoNmae = srcRepoNmae;
        DestRepoName = destRepoName;
        TransfornName = transfornName;
    }
}
