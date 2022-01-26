package io.qiniu.dao.collector.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.qiniu.pandora.common.Constants;
import com.qiniu.pandora.service.storage.StorageDTO;
import io.qiniu.common.Constant;
import io.qiniu.common.entity.collector.CollectorTask;
import io.qiniu.common.entity.collector.CollectorTask.CollectorTaskDTO;
import io.qiniu.dao.collector.ICollectorTaskDao;
import io.qiniu.repository.AbstractDatabaseImpl;
import io.qiniu.service.PandoraService;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class CollectorTaskDaoImpl extends AbstractDatabaseImpl implements ICollectorTaskDao {

  @Autowired
  public CollectorTaskDaoImpl(PandoraService pandoraService) {
    super(pandoraService);
  }

  @Override
  public void insert(CollectorTask task) throws Exception {
    this.service
        .getStorageService()
        .insertData(Constant.APP_NAME, Constant.APP_TABLE_NAME_COLLECTOR_TASK, task.toInsertMap());
  }

  @Override
  public void deleteByTaskIds(List<String> taskIds) throws Exception {
    if (taskIds == null || taskIds.isEmpty()) {
      return;
    }

    Map<String, String> queryMap = new HashMap<>();
    queryMap.put(
        "query",
        URLEncoder.encode(
            String.format(
                "taskId in ('%s')", StringUtils.collectionToCommaDelimitedString(taskIds)),
            StandardCharsets.UTF_8));

    this.service
        .getStorageService()
        .deleteData(Constant.APP_NAME, Constant.APP_TABLE_NAME_COLLECTOR_TASK, queryMap);
  }

  @Override
  public void update(CollectorTask task) throws Exception {
    if (task == null) {
      return;
    }

    Map<String, String> queryMap = new HashMap<>();
    queryMap.put(
        "query",
        URLEncoder.encode(
            String.format("taskId = '%s'", task.getTaskId()), StandardCharsets.UTF_8));

    this.service
        .getStorageService()
        .updateData(
            Constant.APP_NAME,
            Constant.APP_TABLE_NAME_COLLECTOR_TASK,
            queryMap,
            task.toUpdateMap());
  }

  @Override
  public void updateStatusByTaskIds(List<String> taskIds, boolean status) throws Exception {
    if (taskIds == null || taskIds.isEmpty()) {
      return;
    }

    Map<String, String> queryMap = new HashMap<>();
    queryMap.put(
        "query",
        URLEncoder.encode(
            String.format(
                "taskId in ('%s')", StringUtils.collectionToCommaDelimitedString(taskIds)),
            StandardCharsets.UTF_8));

    Map<String, Object> updateMap = new HashMap<>();
    updateMap.put("enabled", status ? 1 : 0);
    updateMap.put("updateTime", new Timestamp(System.currentTimeMillis()).toString());

    this.service
        .getStorageService()
        .updateData(Constant.APP_NAME, Constant.APP_TABLE_NAME_COLLECTOR_TASK, queryMap, updateMap);
  }

  @Override
  public List<CollectorTask> getList() throws Exception {
    List<CollectorTaskDTO> taskDTOS =
        this.service
            .getStorageService()
            .getData(
                Constant.APP_NAME,
                Constant.APP_TABLE_NAME_COLLECTOR_TASK,
                new HashMap<>(),
                new TypeReference<StorageDTO<CollectorTaskDTO>>() {})
            .getData();

    List<CollectorTask> tasks = new ArrayList<>(taskDTOS.size());
    for (CollectorTaskDTO taskDTO : taskDTOS) {
      tasks.add(taskDTO.toCollectorTask());
    }
    return tasks;
  }

  @Override
  public List<CollectorTask> getListWithToken(String token) throws Exception {
    Map<String, String> headers = new HashMap<>();
    headers.put(Constants.AUTHORIZATION, token);

    List<CollectorTaskDTO> taskDTOS =
        this.service
            .getStorageService()
            .getData(
                Constant.APP_NAME,
                Constant.APP_TABLE_NAME_COLLECTOR_TASK,
                new HashMap<>(),
                headers,
                new TypeReference<StorageDTO<CollectorTaskDTO>>() {})
            .getData();

    List<CollectorTask> tasks = new ArrayList<>(taskDTOS.size());
    for (CollectorTaskDTO taskDTO : taskDTOS) {
      tasks.add(taskDTO.toCollectorTask());
    }
    return tasks;
  }

  @Override
  public List<CollectorTask> getListByTaskIds(List<String> taskIds) throws Exception {
    Map<String, String> queryMap = new HashMap<>();
    queryMap.put(
        "query",
        URLEncoder.encode(
            String.format(
                "taskId in ('%s')", StringUtils.collectionToCommaDelimitedString(taskIds)),
            StandardCharsets.UTF_8));

    List<CollectorTaskDTO> taskDTOS =
        this.service
            .getStorageService()
            .getData(
                Constant.APP_NAME,
                Constant.APP_TABLE_NAME_COLLECTOR_TASK,
                queryMap,
                new TypeReference<StorageDTO<CollectorTaskDTO>>() {})
            .getData();
    List<CollectorTask> tasks = new ArrayList<>(taskDTOS.size());
    for (CollectorTaskDTO taskDTO : taskDTOS) {
      tasks.add(taskDTO.toCollectorTask());
    }
    return tasks;
  }

  @Override
  public CollectorTask getByTaskId(String taskId) throws Exception {
    Map<String, String> queryMap = new HashMap<>();
    queryMap.put(
        "query",
        URLEncoder.encode(String.format("taskId = \"%s\"", taskId), StandardCharsets.UTF_8));

    List<CollectorTaskDTO> taskDTOS =
        this.service
            .getStorageService()
            .getData(
                Constant.APP_NAME,
                Constant.APP_TABLE_NAME_COLLECTOR_TASK,
                queryMap,
                new TypeReference<StorageDTO<CollectorTaskDTO>>() {})
            .getData();
    if (taskDTOS.size() > 0) {
      return taskDTOS.get(0).toCollectorTask();
    } else {
      return null;
    }
  }

  @Override
  public CollectorTask getByName(String taskName) throws Exception {
    Map<String, String> queryMap = new HashMap<>();
    queryMap.put(
        "query",
        URLEncoder.encode(String.format("name = \"%s\"", taskName), StandardCharsets.UTF_8));

    List<CollectorTaskDTO> taskDTOS =
        this.service
            .getStorageService()
            .getData(
                Constant.APP_NAME,
                Constant.APP_TABLE_NAME_COLLECTOR_TASK,
                queryMap,
                new TypeReference<StorageDTO<CollectorTaskDTO>>() {})
            .getData();
    if (taskDTOS.size() > 0) {
      return taskDTOS.get(0).toCollectorTask();
    } else {
      return null;
    }
  }
}
