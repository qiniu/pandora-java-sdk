package io.qiniu.dao.collector.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.qiniu.pandora.service.storage.StorageDTO;
import io.qiniu.common.Constant;
import io.qiniu.common.entity.Pagination;
import io.qiniu.common.entity.ResourceList;
import io.qiniu.common.entity.collector.CollectorConfig;
import io.qiniu.common.entity.collector.CollectorConfig.CollectorConfigDTO;
import io.qiniu.dao.collector.ICollectorConfigDao;
import io.qiniu.repository.AbstractDatabaseImpl;
import io.qiniu.service.PandoraService;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class CollectorConfigDaoImpl extends AbstractDatabaseImpl implements ICollectorConfigDao {

  @Autowired
  public CollectorConfigDaoImpl(PandoraService pandoraService) {
    super(pandoraService);
  }

  @Override
  public void insert(CollectorConfig config) throws Exception {
    this.service
        .getStorageService()
        .insertData(
            Constant.APP_NAME, Constant.APP_TABLE_NAME_COLLECTOR_CONFIG, config.toInsertMap());
  }

  @Override
  public void update(long id, CollectorConfig config) throws Exception {
    this.service
        .getStorageService()
        .updateDataById(
            Constant.APP_NAME,
            Constant.APP_TABLE_NAME_COLLECTOR_CONFIG,
            Long.toString(id),
            config.toUpdateMap());
  }

  @Override
  public void deleteById(long configId) throws Exception {
    this.service
        .getStorageService()
        .deleteDataById(
            Constant.APP_NAME, Constant.APP_TABLE_NAME_COLLECTOR_CONFIG, Long.toString(configId));
  }

  @Override
  public void deleteByIds(List<Long> configIds) throws Exception {
    Map<String, String> queryMap = new HashMap<>();
    queryMap.put(
        "query",
        URLEncoder.encode(
            String.format("id in (%s)", StringUtils.collectionToCommaDelimitedString(configIds)),
            StandardCharsets.UTF_8.toString()));
    this.service
        .getStorageService()
        .deleteData(Constant.APP_NAME, Constant.APP_TABLE_NAME_COLLECTOR_CONFIG, queryMap);
  }

  @Override
  public CollectorConfig getById(long configId) throws Exception {
    CollectorConfigDTO config =
        this.service
            .getStorageService()
            .getDataById(
                Constant.APP_NAME,
                Constant.APP_TABLE_NAME_COLLECTOR_CONFIG,
                Long.toString(configId),
                CollectorConfigDTO.class);
    if (config != null) {
      return config.toCollectorConfig();
    }
    return null;
  }

  @Override
  public CollectorConfig getByNameAndOwner(String name, String owner) throws Exception {
    Map<String, String> queryMap = new HashMap<>();
    queryMap.put(
        "query",
        URLEncoder.encode(
            String.format("name=\"%s\" and owner = \"%s\"", name, owner),
            StandardCharsets.UTF_8.toString()));
    List<CollectorConfigDTO> configs =
        this.service
            .getStorageService()
            .getData(
                Constant.APP_NAME,
                Constant.APP_TABLE_NAME_COLLECTOR_CONFIG,
                queryMap,
                new TypeReference<StorageDTO<CollectorConfigDTO>>() {})
            .getData();
    if (configs.size() > 0) {
      return configs.get(0).toCollectorConfig();
    } else {
      return null;
    }
  }

  @Override
  public ResourceList<CollectorConfig> getList(Pagination pagination) throws Exception {
    Map<String, String> queryMap = new HashMap<>();
    queryMap.put("pageNo", Integer.toString(pagination.getPageNo()));
    queryMap.put("pageSize", Integer.toString(pagination.getPageSize()));
    queryMap.put("sort", pagination.getSort());
    queryMap.put("order", pagination.getOrder().value());
    queryMap.put("prefix", pagination.getPrefix());
    String filterStr = "";
    Map<String, Object> filter = pagination.getFilter();
    if (filter != null) {
      List<String> ownerList = (List<String>) filter.get("owner");
      if (ownerList != null && !ownerList.isEmpty()) {
        filterStr +=
            String.format("owner in (%s)", StringUtils.collectionToCommaDelimitedString(ownerList));
      }
      List<String> repoList = (List<String>) filter.get("repo");
      if (repoList != null && !repoList.isEmpty()) {
        if (!filterStr.isEmpty()) {
          filterStr += " and ";
        }
        filterStr +=
            String.format("repo in (%s)", StringUtils.collectionToCommaDelimitedString(repoList));
      }
      List<String> sourceTypeList = (List<String>) filter.get("sourceType");
      if (sourceTypeList != null && !sourceTypeList.isEmpty()) {
        if (!filterStr.isEmpty()) {
          filterStr += " and ";
        }
        filterStr +=
            String.format(
                "sourceType in (%s)", StringUtils.collectionToCommaDelimitedString(sourceTypeList));
      }
      List<Boolean> enableList = (List<Boolean>) filter.get("enable");
      if (enableList != null && !enableList.isEmpty()) {
        if (!filterStr.isEmpty()) {
          filterStr += " and ";
        }
        filterStr +=
            String.format(
                "enable in (%s)", StringUtils.collectionToCommaDelimitedString(enableList));
      }
    }

    queryMap.put("query", URLEncoder.encode(filterStr, StandardCharsets.UTF_8.toString()));

    StorageDTO<CollectorConfigDTO> configDTO =
        this.service
            .getStorageService()
            .getData(
                Constant.APP_NAME,
                Constant.APP_TABLE_NAME_COLLECTOR_CONFIG,
                queryMap,
                new TypeReference<>() {});
    List<CollectorConfig> configList = new ArrayList<>();
    for (CollectorConfigDTO configDTOItem : configDTO.getData()) {
      configList.add(configDTOItem.toCollectorConfig());
    }
    return new ResourceList<>(
        configDTO.getTotal(),
        configList,
        pagination.getSort(),
        pagination.getPageNo(),
        pagination.getPageSize());
  }

  @Override
  public List<CollectorConfig> getList() throws Exception {
    List<CollectorConfigDTO> configDTOS =
        this.service
            .getStorageService()
            .getData(
                Constant.APP_NAME,
                Constant.APP_TABLE_NAME_COLLECTOR_CONFIG,
                new HashMap<>(),
                new TypeReference<StorageDTO<CollectorConfigDTO>>() {})
            .getData();

    List<CollectorConfig> configs = new ArrayList<>(configDTOS.size());
    for (CollectorConfigDTO configDTO : configDTOS) {
      configs.add(configDTO.toCollectorConfig());
    }
    return configs;
  }

  @Override
  public List<CollectorConfig> getListByIds(List<Long> configIds) throws Exception {
    Map<String, String> queryMap = new HashMap<>();
    queryMap.put(
        "query",
        URLEncoder.encode(
            String.format("id in (%s)", StringUtils.collectionToCommaDelimitedString(configIds)),
            StandardCharsets.UTF_8.toString()));
    List<CollectorConfigDTO> configDTOS =
        this.service
            .getStorageService()
            .getData(
                Constant.APP_NAME,
                Constant.APP_TABLE_NAME_COLLECTOR_CONFIG,
                queryMap,
                new TypeReference<StorageDTO<CollectorConfigDTO>>() {})
            .getData();
    List<CollectorConfig> configs = new ArrayList<>(configDTOS.size());
    for (CollectorConfigDTO configDTO : configDTOS) {
      configs.add(configDTO.toCollectorConfig());
    }
    return configs;
  }

  @Override
  public List<Long> getIdListByOwner(String owner) throws Exception {
    Map<String, String> queryMap = new HashMap<>();
    queryMap.put(
        "query",
        URLEncoder.encode(
            String.format("owner = \"%s\"", owner), StandardCharsets.UTF_8.toString()));
    queryMap.put("column", "id");

    return this.service
        .getStorageService()
        .getData(
            Constant.APP_NAME,
            Constant.APP_TABLE_NAME_COLLECTOR_CONFIG,
            queryMap,
            new TypeReference<StorageDTO<String>>() {})
        .getData()
        .stream()
        .map(Long::parseLong)
        .collect(Collectors.toList());
  }

  @Override
  public List<String> getDistinctOwners() throws Exception {
    return getDistinctData("owner");
  }

  @Override
  public List<String> getDistinctSourceTypes() throws Exception {
    return getDistinctData("sourceType");
  }

  @Override
  public List<String> getDistinctRepos() throws Exception {
    return getDistinctData("repo");
  }

  @Override
  public List<Boolean> getDistinctStatus() throws Exception {
    List<Integer> statusList = getDistinctData("enabled");
    return statusList.stream().map(elem -> elem == 1).collect(Collectors.toList());
  }

  private <T> List<T> getDistinctData(final String column) throws Exception {
    Map<String, String> queryMap = new HashMap<>();
    queryMap.put("column", column);

    return this.service
        .getStorageService()
        .getData(
            Constant.APP_NAME,
            Constant.APP_TABLE_NAME_COLLECTOR_CONFIG,
            queryMap,
            new TypeReference<StorageDTO<Map<String, T>>>() {})
        .getData()
        .stream()
        .map(elem -> elem.get(column))
        .distinct()
        .collect(Collectors.toList());
  }

  @Override
  public void updateStatusByIds(List<Long> configIds, boolean status) throws Exception {
    Map<String, String> queryMap = new HashMap<>();
    queryMap.put(
        "query",
        URLEncoder.encode(
            String.format("id in (%s)", StringUtils.collectionToCommaDelimitedString(configIds)),
            StandardCharsets.UTF_8.toString()));

    Map<String, Object> updateMap = new HashMap<>();
    updateMap.put("enabled", status ? 1 : 0);
    updateMap.put("updateTime", System.currentTimeMillis());

    this.service
        .getStorageService()
        .updateData(
            Constant.APP_NAME, Constant.APP_TABLE_NAME_COLLECTOR_CONFIG, queryMap, updateMap);
  }
}
