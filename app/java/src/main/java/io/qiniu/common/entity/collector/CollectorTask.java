package io.qiniu.common.entity.collector;

import io.qiniu.utils.JsonHelper;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class CollectorTask {

  private long id; // kv store auto create

  private String taskId; // real id

  private String name; // task name, config name + task id

  private String workerId; // pandora instance id

  private long configId;

  private boolean enabled; // expect state

  private boolean status; // actual state

  private Map<String, Object> config; // collector config

  private String meta; // ex. runner meta data

  private long createTime;

  private long updateTime;

  public CollectorTask() {}

  public CollectorTask(
      String taskId,
      String name,
      String workerId,
      long configId,
      boolean enabled,
      Map<String, Object> config) {
    this.taskId = taskId;
    this.name = name;
    this.workerId = workerId;
    this.configId = configId;
    this.enabled = enabled;
    this.status = false;
    this.config = config;
    this.createTime = System.currentTimeMillis();
    this.updateTime = System.currentTimeMillis();
  }

  public CollectorTask(
      long id,
      String taskId,
      String name,
      String workerId,
      long configId,
      boolean enabled,
      boolean status,
      Map<String, Object> config,
      String meta,
      long createTime,
      long updateTime) {
    this.id = id;
    this.taskId = taskId;
    this.name = name;
    this.workerId = workerId;
    this.configId = configId;
    this.enabled = enabled;
    this.status = status;
    this.config = config;
    this.meta = meta;
    this.createTime = createTime;
    this.updateTime = updateTime;
  }

  public Map<String, Object> toInsertMap() {
    Map<String, Object> map = new HashMap<>();

    map.put("taskId", this.taskId);
    map.put("name", this.name);
    map.put("workerId", this.workerId);
    map.put("configId", this.configId);
    map.put("enabled", this.enabled ? 1 : 0);
    map.put("status", this.status ? 1 : 0);
    map.put("config", JsonHelper.writeValueAsString(this.config));
    map.put("meta", null);
    map.put("createTime", new Timestamp(createTime).toString());
    map.put("updateTime", new Timestamp(updateTime).toString());

    return map;
  }

  public Map<String, Object> toUpdateMap() {
    Map<String, Object> map = new HashMap<>();

    map.put("taskId", this.taskId);
    map.put("name", this.name);
    map.put("workerId", this.workerId);
    map.put("configId", this.configId);
    map.put("enabled", this.enabled ? 1 : 0);
    map.put("status", this.status ? 1 : 0);
    map.put("config", JsonHelper.writeValueAsString(this.config));
    map.put("meta", this.meta == null ? null : JsonHelper.writeValueAsString(this.meta));
    map.put("updateTime", new Timestamp(updateTime).toString());

    return map;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
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

  public long getConfigId() {
    return configId;
  }

  public void setConfigId(long configId) {
    this.configId = configId;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public boolean isStatus() {
    return status;
  }

  public void setStatus(boolean status) {
    this.status = status;
  }

  public Map<String, Object> getConfig() {
    return config;
  }

  public void setConfig(Map<String, Object> config) {
    this.config = config;
  }

  public String getMeta() {
    return meta;
  }

  public void setMeta(String meta) {
    this.meta = meta;
  }

  public long getCreateTime() {
    return createTime;
  }

  public void setCreateTime(long createTime) {
    this.createTime = createTime;
  }

  public long getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(long updateTime) {
    this.updateTime = updateTime;
  }

  public static class CollectorTaskDTO {

    private long id; // kv store auto create

    private String taskId; // real id

    private String name; // task name, config name + task id

    private String workerId; // pandora instance id

    private long configId;

    private int enabled; // expect state

    private int status; // actual state

    private String config; // collector config

    private String meta; // ex. runner meta data

    private String createTime;

    private String updateTime;

    public CollectorTaskDTO() {}

    public CollectorTask toCollectorTask() throws ParseException {
      DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");

      CollectorTask task = new CollectorTask();
      task.setId(id);
      task.setTaskId(taskId);
      task.setName(name);
      task.setWorkerId(workerId);
      task.setConfigId(configId);
      task.setEnabled(enabled == 1);
      task.setStatus(status == 1);
      task.setConfig(JsonHelper.readValueAsMap(config.getBytes()));
      if (this.meta != null) {
        task.setMeta(meta);
      }
      task.setCreateTime(df.parse(createTime).getTime());
      task.setUpdateTime(df.parse(updateTime).getTime());

      return task;
    }

    public long getId() {
      return id;
    }

    public void setId(long id) {
      this.id = id;
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

    public long getConfigId() {
      return configId;
    }

    public void setConfigId(long configId) {
      this.configId = configId;
    }

    public int getStatus() {
      return status;
    }

    public void setStatus(int status) {
      this.status = status;
    }

    public String getConfig() {
      return config;
    }

    public void setConfig(String config) {
      this.config = config;
    }

    public String getMeta() {
      return meta;
    }

    public void setMeta(String meta) {
      this.meta = meta;
    }

    public String getCreateTime() {
      return createTime;
    }

    public void setCreateTime(String createTime) {
      this.createTime = createTime;
    }

    public String getUpdateTime() {
      return updateTime;
    }

    public void setUpdateTime(String updateTime) {
      this.updateTime = updateTime;
    }
  }
}
