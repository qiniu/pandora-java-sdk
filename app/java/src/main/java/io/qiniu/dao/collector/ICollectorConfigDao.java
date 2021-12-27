package io.qiniu.dao.collector;

import io.qiniu.common.entity.Pagination;
import io.qiniu.common.entity.ResourceList;
import io.qiniu.common.entity.collector.CollectorConfig;
import java.util.List;

public interface ICollectorConfigDao {

  void insert(CollectorConfig config) throws Exception;

  void update(long id, CollectorConfig config) throws Exception;

  void deleteById(long configId) throws Exception;

  void deleteByIds(List<Long> configIds) throws Exception;

  CollectorConfig getById(long configId) throws Exception;

  CollectorConfig getByNameAndOwner(String name, String owner) throws Exception;

  ResourceList<CollectorConfig> getList(Pagination pagination) throws Exception;

  List<CollectorConfig> getList() throws Exception;

  List<CollectorConfig> getListByIds(List<Long> configIds) throws Exception;

  List<Long> getIdListByOwner(String owner) throws Exception;

  List<String> getDistinctOwners() throws Exception;

  List<String> getDistinctSourceTypes() throws Exception;

  List<String> getDistinctRepos() throws Exception;

  List<Boolean> getDistinctStatus() throws Exception;

  void updateStatusByIds(List<Long> configIds, boolean status) throws Exception;
}
