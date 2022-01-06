package io.qiniu.controller.collector;

import com.qiniu.pandora.common.QiniuException;
import com.qiniu.pandora.common.QiniuExceptions;
import io.qiniu.common.entity.collector.CollectorConfig;
import io.qiniu.common.entity.collector.CollectorTask;
import io.qiniu.controller.collector.request.CollectorTaskActionRequest;
import io.qiniu.controller.collector.request.CollectorTaskBatchDeleteRequest;
import io.qiniu.controller.collector.request.CollectorTaskRequest;
import io.qiniu.controller.collector.request.CollectorTaskStatusRequest;
import io.qiniu.controller.collector.vo.CollectorTaskVo;
import io.qiniu.dao.collector.ICollectorTaskDao;
import io.qiniu.service.collector.CollectorConfigService;
import io.qiniu.service.collector.CollectorTaskService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/collector")
public class CollectorTaskController {

  public static final String REQUEST_PATH_ID = "id"; // 采集任务 id

  private final CollectorTaskService collectorTaskService;
  private final CollectorConfigService collectorConfigService;
  private final ICollectorTaskDao collectorTaskDao;

  @Autowired
  public CollectorTaskController(
      CollectorTaskService collectorTaskService,
      CollectorConfigService collectorConfigService,
      ICollectorTaskDao collectorTaskDao) {
    this.collectorTaskService = collectorTaskService;
    this.collectorConfigService = collectorConfigService;
    this.collectorTaskDao = collectorTaskDao;
  }

  // get all collector task runners status
  @GetMapping(value = "/tasks")
  public List<CollectorTaskVo> getTasks() throws QiniuException {
    return collectorTaskService.queryTasks().stream()
        .map(CollectorTaskVo::new)
        .collect(Collectors.toList());
  }

  // get collector task runners status by id
  @GetMapping(value = "/tasks/{ids}")
  public List<CollectorTaskVo> getTaskListByIds(@PathVariable("ids") List<String> taskIds)
      throws QiniuException {
    if (taskIds == null || taskIds.isEmpty()) {
      return new ArrayList<>();
    }

    // check taskIds
    return collectorTaskService.queryTasks(taskIds).stream()
        .map(CollectorTaskVo::new)
        .collect(Collectors.toList());
  }

  @PostMapping(value = "/tasks")
  public void addTask(@Valid @RequestBody CollectorTaskRequest reqBody) throws QiniuException {
    CollectorTask task = reqBody.toCollectorTask();

    CollectorTask exist;
    try {
      exist = this.collectorTaskDao.getByTaskId(task.getTaskId());
    } catch (Exception e) {
      throw QiniuExceptions.InternalServerError(e.getMessage());
    }
    if (exist != null) {
      throw QiniuExceptions.BadRequest("task is exist");
    }

    CollectorConfig config = collectorConfigService.getConfig(reqBody.getConfigId());
    if (config == null) {
      throw QiniuExceptions.BadRequest("config is not exist");
    }

    task.setConfig(config.getConfig());

    this.collectorTaskService.addTask(task);
  }

  @PostMapping(value = "/tasks:batchDelete")
  public void deleteTaskListByIds(@Valid @RequestBody final CollectorTaskBatchDeleteRequest reqBody)
      throws QiniuException {
    this.collectorTaskService.deleteTasks(reqBody.getIds());
  }

  @PostMapping(value = "/tasks:status")
  public void updateTaskStatus(@Valid @RequestBody final CollectorTaskStatusRequest reqBody)
      throws Exception {
    String status = reqBody.getStatus();
    List<String> taskIds = reqBody.getIds();

    List<CollectorTask> tasks = this.collectorTaskDao.getListByTaskIds(taskIds);
    if (tasks == null || tasks.isEmpty()) {
      throw QiniuExceptions.BadRequest("task id not found");
    }

    switch (status) {
      case "stop":
        this.collectorTaskService.stopTasks(taskIds);
        break;
      case "start":
        this.collectorTaskService.startTasks(taskIds);
        break;
      default:
        throw QiniuExceptions.BadRequest(String.format("not supported status %s", status));
    }
  }

  @PostMapping(value = "/tasks:action")
  public void updateTaskAction(@Valid @RequestBody final CollectorTaskActionRequest reqBody)
      throws QiniuException {
    String action = reqBody.getAction();
    List<String> taskIds = reqBody.getIds();

    List<CollectorTask> tasks = this.collectorTaskService.queryTasks(taskIds);
    if (tasks == null || tasks.isEmpty()) {
      throw QiniuExceptions.BadRequest("task id not found");
    }

    switch (action) {
      case "reset":
        this.collectorTaskService.resetTasks(taskIds);
        break;
      default:
        throw QiniuExceptions.BadRequest(String.format("not supported action %s", action));
    }
  }

  @PutMapping(value = "/tasks/{id}")
  public void updateTask(
      @PathVariable(value = REQUEST_PATH_ID) final String taskId,
      @Valid @RequestBody CollectorTaskRequest reqBody)
      throws QiniuException {
    CollectorTask exist = this.collectorTaskService.queryTask(taskId);
    if (exist == null) {
      throw QiniuExceptions.NotFound("task not exist");
    }

    CollectorConfig config = collectorConfigService.getConfig(reqBody.getConfigId());
    if (config == null) {
      throw QiniuExceptions.NotFound("config is not exist");
    }

    CollectorTask task = reqBody.toCollectorTask();
    task.setTaskId(taskId);
    task.setConfig(config.getConfig());

    this.collectorTaskService.updateTask(task);
  }
}
