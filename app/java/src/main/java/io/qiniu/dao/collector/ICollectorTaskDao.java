package io.qiniu.dao.collector;

import io.qiniu.common.entity.collector.CollectorTask;
import java.util.List;

public interface ICollectorTaskDao {

  void insert(CollectorTask task) throws Exception;

  void deleteByTaskIds(List<String> taskIds) throws Exception;

  void update(CollectorTask task) throws Exception;

  void updateStatusByTaskIds(List<String> taskIds, boolean status) throws Exception;

  List<CollectorTask> getList() throws Exception;

  List<CollectorTask> getListWithToken(String token) throws Exception;

  List<CollectorTask> getListByTaskIds(List<String> taskIds) throws Exception;

  CollectorTask getByTaskId(String taskId) throws Exception;

  CollectorTask getByName(String taskName) throws Exception;
}
