package io.qiniu.controller.collector.request;

import io.qiniu.common.entity.collector.CollectorTask;
import javax.validation.constraints.NotNull;

public class CollectorTaskRequest {

  @NotNull(message = "The task id is required.")
  private String taskId;

  @NotNull(message = "The name is required.")
  private String name;

  @NotNull(message = "The worker id is required.")
  private String workerId; // pandora instance id

  @NotNull(message = "The config id is required.")
  private Long configId;

  private Boolean enabled; // expect state, default true

  public CollectorTask toCollectorTask() {
    CollectorTask task = new CollectorTask();

    task.setTaskId(taskId);
    task.setName(name);
    task.setWorkerId(workerId);
    task.setConfigId(configId);
    task.setEnabled(enabled == null || enabled);
    task.setCreateTime(System.currentTimeMillis());
    task.setUpdateTime(System.currentTimeMillis());

    return task;
  }

  public String getTaskId() {
    return taskId;
  }

  public void setTaskId(String taskId) {
    this.taskId = taskId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getWorkerId() {
    return workerId;
  }

  public void setWorkerId(String workerId) {
    this.workerId = workerId;
  }

  public Long getConfigId() {
    return configId;
  }

  public void setConfigId(Long configId) {
    this.configId = configId;
  }

  public Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }
}
