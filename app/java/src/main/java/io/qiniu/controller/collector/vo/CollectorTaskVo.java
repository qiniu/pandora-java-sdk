package io.qiniu.controller.collector.vo;

import io.qiniu.common.entity.collector.CollectorTask;
import java.util.Map;

public class CollectorTaskVo {

  public String taskId;

  public String name;

  public String workerId;

  public boolean status;

  public Map<String, Object> config;

  public CollectorTaskVo() {}

  public CollectorTaskVo(CollectorTask task) {
    this.taskId = task.getTaskId();
    this.name = task.getName();
    this.workerId = task.getWorkerId();
    this.status = task.isStatus();
    this.config = task.getConfig();
  }
}
