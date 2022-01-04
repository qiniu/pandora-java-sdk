package io.qiniu.service.collector;

import com.qiniu.pandora.common.QiniuException;
import com.qiniu.pandora.common.QiniuExceptions;
import io.qiniu.common.entity.Pagination;
import io.qiniu.common.entity.ResourceList;
import io.qiniu.common.entity.collector.CollectorConfig;
import io.qiniu.dao.collector.ICollectorConfigDao;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CollectorConfigService {

  private final ICollectorConfigDao configDao;

  @Autowired
  public CollectorConfigService(ICollectorConfigDao configDao) {
    this.configDao = configDao;
  }

  public List<CollectorConfig> getConfigs() throws QiniuException {
    try {
      return configDao.getList();
    } catch (Exception e) {
      throw QiniuExceptions.InternalServerError(e.getMessage());
    }
  }

  public ResourceList<CollectorConfig> getConfigs(Pagination pagination) throws QiniuException {
    try {
      return configDao.getList(pagination);
    } catch (Exception e) {
      throw QiniuExceptions.InternalServerError(e.getMessage());
    }
  }

  public CollectorConfig getConfigByNameAndOwner(String name, String owner) throws QiniuException {
    if (name == null || name.isEmpty()) {
      throw QiniuExceptions.InvalidArgument("name is null or empty");
    }
    if (owner == null || owner.isEmpty()) {
      throw QiniuExceptions.InvalidArgument("owner is null or empty");
    }

    try {
      return configDao.getByNameAndOwner(name, owner);
    } catch (Exception e) {
      throw QiniuExceptions.InternalServerError(e.getMessage());
    }
  }

  public List<CollectorConfig> getConfigListByIds(List<Long> ids) throws QiniuException {
    try {
      return configDao.getListByIds(ids);
    } catch (Exception e) {
      throw QiniuExceptions.InternalServerError(e.getMessage());
    }
  }

  public void addConfig(CollectorConfig config) throws QiniuException {
    if (config == null) {
      throw QiniuExceptions.InvalidArgument("config is null");
    }

    try {
      configDao.insert(config);
    } catch (Exception e) {
      throw QiniuExceptions.InternalServerError(e.getMessage());
    }
  }

  public void deleteConfigListByIds(List<Long> ids) throws QiniuException {
    try {
      configDao.deleteByIds(ids);
    } catch (Exception e) {
      throw QiniuExceptions.InternalServerError(e.getMessage());
    }
  }

  public void updateConfigsStatus(List<Long> ids, boolean status) throws QiniuException {
    try {
      configDao.updateStatusByIds(ids, status);
    } catch (Exception e) {
      throw QiniuExceptions.InternalServerError(e.getMessage());
    }
  }

  public void updateConfig(long id, CollectorConfig config) throws QiniuException {
    try {
      configDao.update(id, config);
    } catch (Exception e) {
      throw QiniuExceptions.InternalServerError(e.getMessage());
    }
  }

  public List<String> getDistinctOwners() throws QiniuException {
    try {
      return configDao.getDistinctOwners();
    } catch (Exception e) {
      throw QiniuExceptions.InternalServerError(e.getMessage());
    }
  }

  public List<String> getDistinctSourceTypes() throws QiniuException {
    try {
      return configDao.getDistinctSourceTypes();
    } catch (Exception e) {
      throw QiniuExceptions.InternalServerError(e.getMessage());
    }
  }

  public List<String> getDistinctRepos() throws QiniuException {
    try {
      return configDao.getDistinctRepos();
    } catch (Exception e) {
      throw QiniuExceptions.InternalServerError(e.getMessage());
    }
  }

  public List<Boolean> getDistinctStatus() throws QiniuException {
    try {
      return configDao.getDistinctStatus();
    } catch (Exception e) {
      throw QiniuExceptions.InternalServerError(e.getMessage());
    }
  }
}
