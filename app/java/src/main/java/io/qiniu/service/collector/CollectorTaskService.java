package io.qiniu.service.collector;

import com.qiniu.pandora.collect.Collector;
import com.qiniu.pandora.collect.CollectorConfig;
import com.qiniu.pandora.collect.CollectorContext;
import com.qiniu.pandora.collect.DefaultCollector;
import com.qiniu.pandora.collect.State;
import com.qiniu.pandora.collect.runner.config.RunnerConfig;
import com.qiniu.pandora.common.QiniuException;
import com.qiniu.pandora.common.QiniuExceptions;
import io.qiniu.common.entity.collector.CollectorTask;
import io.qiniu.configuration.CollectorProperties;
import io.qiniu.configuration.PandoraProperties;
import io.qiniu.dao.collector.ICollectorTaskDao;
import io.qiniu.service.PandoraService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CollectorTaskService {

  // 当前 app 对应的工作节点 id
  private final String workerId;
  private final Collector collector;
  private final ICollectorTaskDao collectorTaskDao;
  private final PandoraProperties properties;

  private static final Logger logger = LogManager.getLogger(CollectorTaskService.class.getName());

  @Autowired
  public CollectorTaskService(
      PandoraProperties properties,
      CollectorProperties collectorProperties,
      PandoraService pandoraService,
      ICollectorTaskDao collectorTaskDao) {
    this.workerId = properties.getId();
    this.collectorTaskDao = collectorTaskDao;
    this.properties = properties;
    this.collector =
        new DefaultCollector(
            new CollectorConfig(collectorProperties.getMetaPath()),
            new CollectorContext(
                pandoraService.getTokenService(), pandoraService.getPostDataService()));
  }

  @PostConstruct
  public void init() {
    this.collector.start();
    recoveryTask();
  }

  public List<CollectorTask> queryTasks() throws QiniuException {
    List<CollectorTask> tasks = new ArrayList<>();
    for (RunnerConfig config : this.collector.getAllRunners()) {
      CollectorTask task = convertConfigToTask(config);
      task.setWorkerId(this.workerId);
      tasks.add(task);
    }
    return tasks;
  }

  public List<CollectorTask> queryTasks(List<String> taskIds) throws QiniuException {
    List<CollectorTask> tasks = new ArrayList<>();
    for (RunnerConfig config : this.collector.getRunners(taskIds)) {
      CollectorTask task = convertConfigToTask(config);
      task.setWorkerId(this.workerId);
      tasks.add(task);
    }
    return tasks;
  }

  public CollectorTask queryTask(String taskId) throws QiniuException {
    RunnerConfig config = this.collector.getRunner(taskId);
    if (config == null) {
      return null;
    }
    CollectorTask task = convertConfigToTask(config);
    task.setWorkerId(this.workerId);
    return task;
  }

  public void addTask(CollectorTask task) throws QiniuException {
    if (task == null || task.getWorkerId() == null || !task.getWorkerId().equals(this.workerId)) {
      return;
    }

    task.setConfig(adapter(task.getConfig()));

    try {
      collectorTaskDao.insert(task);
    } catch (Exception e) {
      throw QiniuExceptions.InternalServerError(e.getMessage());
    }

    this.collector.addRunner(convertTaskToConfig(task));
  }

  public void deleteTasks(List<String> taskIds) throws QiniuException {
    if (taskIds == null || taskIds.isEmpty()) {
      return;
    }

    try {
      collectorTaskDao.deleteByTaskIds(taskIds);
    } catch (Exception e) {
      throw QiniuExceptions.InternalServerError(e.getMessage());
    }
    this.collector.deleteRunners(taskIds);
  }

  public void stopTasks(List<String> taskIds) throws QiniuException {
    if (taskIds == null || taskIds.isEmpty()) {
      return;
    }

    try {
      collectorTaskDao.updateStatusByTaskIds(taskIds, false);
    } catch (Exception e) {
      throw QiniuExceptions.InternalServerError(e.getMessage());
    }
    this.collector.stopRunners(taskIds);
  }

  public void startTasks(List<String> taskIds) throws QiniuException {
    if (taskIds == null || taskIds.isEmpty()) {
      return;
    }

    try {
      collectorTaskDao.updateStatusByTaskIds(taskIds, true);
    } catch (Exception e) {
      throw QiniuExceptions.InternalServerError(e.getMessage());
    }
    this.collector.startRunners(taskIds);
  }

  public void resetTasks(List<String> taskIds) {
    if (taskIds == null || taskIds.isEmpty()) {
      return;
    }
    this.collector.resetRunners(taskIds);
  }

  public void updateTask(CollectorTask task) throws QiniuException {
    if (task == null) {
      return;
    }

    task.setConfig(adapter(task.getConfig()));

    try {
      collectorTaskDao.update(task);
    } catch (Exception e) {
      throw QiniuExceptions.InternalServerError(e.getMessage());
    }
    this.collector.updateRunner(convertTaskToConfig(task));
  }

  // TODO update runner meta to kv store
  public void updateTaskMeta() {}

  private void recoveryTask() {
    logger.info("Recovery tasks from kv store");
    try {
      List<CollectorTask> tasks = collectorTaskDao.getListWithToken(properties.getPandoraToken());
      for (CollectorTask task : tasks) {
        if (task.getWorkerId() != null && task.getWorkerId().equals(this.workerId)) {
          logger.info(String.format("Recovery task: %s", task.getName()));
          collector.addRunner(convertTaskToConfig(task));
        }
      }
    } catch (Exception e) {
      logger.error("Get recovery data from kv store failed", e);
    }
  }

  private static RunnerConfig convertTaskToConfig(CollectorTask task) throws QiniuException {
    return new RunnerConfig(
        task.getTaskId(),
        task.getName(),
        task.isEnabled() ? State.STARTED : State.STOPPED,
        task.getMeta(),
        convertConfigToProperties(task.getConfig()));
  }

  private static CollectorTask convertConfigToTask(RunnerConfig config) throws QiniuException {
    CollectorTask task = new CollectorTask();

    task.setTaskId(config.getId());
    task.setName(config.getName());
    task.setStatus(config.getState().equals(State.STARTED.toString()));
    task.setConfig(convertPropertiesToConfig(config.getProperties()));

    return task;
  }

  private static Map<String, String> convertConfigToProperties(Map<String, Object> config)
      throws QiniuException {
    if (config == null) {
      throw QiniuExceptions.InvalidArgument("config convert to properties failed, config is null");
    }

    Map<String, String> map = new HashMap<>();

    config.forEach((k, v) -> map.put(k, v.toString()));

    return map;
  }

  private static Map<String, Object> convertPropertiesToConfig(Map<String, String> properties)
      throws QiniuException {
    if (properties == null) {
      throw QiniuExceptions.InvalidArgument(
          "properties convert to config failed, properties is null");
    }
    return new HashMap<>(properties);
  }

  private static Map<String, Object> adapter(Map<String, Object> config) {
    Map<String, Object> map = new HashMap<>();
    map.put("sinks", "sk1");
    map.put("channel.type", "memory");
    map.put("channel.capacity", "10000");
    map.put("channel.transactionCapacity", "1000");
    map.put("processor.type", "default");
    map.put("processor.backoff", "true");
    map.put("processor.selector", "round_robin");
    map.put("processor.selector.maxTimeOut", "10000");

    config.forEach(
        (k, v) -> {
          if (k.contains("sink")) {
            String nk = k.replace("sink", "sk1");
            map.put(nk, v.toString());
          } else {
            map.put(k, v.toString());
          }
        });
    return map;
  }
}
