package com.qiniu.pandora.logdb;

import com.google.gson.internal.LinkedTreeMap;
import com.qiniu.pandora.common.QiniuException;
import com.qiniu.pandora.common.QiniuRuntimeException;
import com.qiniu.pandora.logdb.search.SearchService.SearchRequest;
import com.qiniu.pandora.logdb.search.SearchService.SearchResult;
import com.qiniu.pandora.logdb.search.SearchService.SearchResult.Row;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class Update {


  public static void main(String[] args) {
    LogDBClient client = LogDBClient.NewLogDBClient("c_u8Wvko2ZjC-XC02BajmLvyg1dHZwYUXNPLp-xu",
        "BdRzmCjbRekFj3qd_lplG4l6NFKQke4EKE_Pwznj", "http://cs20:9997");
    client = LogDBClient.NewLogDBClient("Q7ACjlnKMBrLNZIf0DTIreLYPOzz-2q5ZXmJZUNL",
        "oPkOBlKbW6XxeS52cRsgwZeaqqsmhRYFJlkuytfy");
    SearchRequest searchRequest = new SearchRequest();

    try {
      searchRequest.query = "traceID:b86e6b9ccf0bc65";
      SearchResult searchResult = client.NewSearchService().search("tracingspan", searchRequest);
      String[] objKeys = new String[]{"process", "tags", "key"};
      for (Row row : searchResult.data) {
        int arrIndex = findArray(row, objKeys, 0);
      }

      
    } catch (QiniuException e) {
      e.printStackTrace();
    }
  }


  public static int findArray(Map row, String[] objKeys, int index)
      throws QiniuRuntimeException {
    if (objKeys.length == 0) {
      return -1;
    }
    if (objKeys.length <= index) {
      throw new QiniuRuntimeException("wrong object keys");
    }

    if (!row.containsKey(objKeys[index])) {
      throw new QiniuRuntimeException(
          "wrong object keys, " + " con not find " + objKeys[index] + " in " + row);
    }
    Object tmp = row.get(objKeys[index]);

    if (tmp instanceof Map) {
      return findArray((LinkedTreeMap) tmp, Arrays.copyOfRange(objKeys, index, objKeys.length),
          index + 1);
    }

    if (tmp instanceof ArrayList) {
      if (index == objKeys.length) {
        throw new QiniuRuntimeException("object key 是用于过滤 object 数组，必须指向object中的一个字段");
      }
      return index;
    }

    throw new QiniuRuntimeException("object key 是用于过滤 object 数组，必须指向object中的一个字段");
  }
}
