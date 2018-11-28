package update;

import com.qiniu.pandora.common.Constants;
import com.qiniu.pandora.common.PandoraClient;
import com.qiniu.pandora.common.PandoraClientImpl;
import com.qiniu.pandora.common.QiniuException;
import com.qiniu.pandora.http.Client;
import com.qiniu.pandora.logdb.LogDBClient;
import com.qiniu.pandora.util.Auth;
import com.qiniu.pandora.util.Json;
import com.qiniu.pandora.util.StringMap;
import java.util.Map;

public class CrudLogdbClient extends LogDBClient {

  public CrudLogdbClient(PandoraClient pandoraClient) {
    super(pandoraClient);
  }

  public CrudLogdbClient(PandoraClient pandoraClient, String logdbHost) {
    super(pandoraClient, logdbHost);
  }

  /**
   * LogDBClient工厂方法，使用官方LogDB Host
   *
   * @param accessKey 七牛accessKey
   * @param secretKey 七牛secretKey
   * @return LogDBClient
   */
  public static LogDBClient NewCrudLogdbClient(String accessKey, String secretKey) {
    Auth auth = Auth.create(accessKey, secretKey);
    PandoraClientImpl pandoraClient = new PandoraClientImpl(auth);
    return new CrudLogdbClient(pandoraClient);
  }

  /**
   * LogDBClient工厂方法，可自定义LogDB Host
   *
   * @param accessKey 七牛accessKey
   * @param secretKey 七牛secretKey
   * @param logdbHost 自定义logdb logdbHost
   * @return LogDBClient
   */
  public static CrudLogdbClient NewCrudLogdbClient(String accessKey, String secretKey, String logdbHost) {
    Auth auth = Auth.create(accessKey, secretKey);
    PandoraClientImpl pandoraClient = new PandoraClientImpl(auth);
    return new CrudLogdbClient(pandoraClient, logdbHost);
  }

  public void putDocument(String repoName, String dataIndex, String pk,
      Map<String, Object> object)
      throws QiniuException {
    String putUrl = String
        .format("%s/v5/repos/%s/data/%s/%s", this.getHost(), repoName, dataIndex, pk);
    String putBody = Json.encode(object);
    this.getPandoraClient()
        .put(putUrl, putBody.getBytes(Constants.UTF_8), new StringMap(), Client.JsonMime).close();
  }

  public void deleteDocument(String repoName, String dataIndex, String pk) throws QiniuException {
    String deleteUrl = String
        .format("%s/v5/repos/%s/data/%s/%s", this.getHost(), repoName, dataIndex, pk);
    this.getPandoraClient().delete(deleteUrl, new StringMap()).close();
  }

  public SearchService newSearchService() {
    return new SearchService(new LogDBClient(getPandoraClient(), this.getHost()));
  }
}
