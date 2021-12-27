package io.qiniu.controller.collector;

import com.qiniu.pandora.common.QiniuException;
import com.qiniu.pandora.common.QiniuExceptions;
import io.qiniu.common.entity.Pagination;
import io.qiniu.common.entity.ResourceList;
import io.qiniu.common.entity.collector.CollectorConfig;
import io.qiniu.controller.collector.request.CollectorConfigBatchDeleteRequest;
import io.qiniu.controller.collector.request.CollectorConfigRequest;
import io.qiniu.controller.collector.request.CollectorConfigUpdateStatusRequest;
import io.qiniu.controller.collector.vo.CollectorConfigPaginatedVo;
import io.qiniu.controller.collector.vo.CollectorConfigVo;
import io.qiniu.service.collector.CollectorConfigService;
import java.util.Arrays;
import java.util.Collections;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/collector")
public class CollectorConfigController {

  public static final String REQUEST_PATH_ID = "id"; // 采集任务 id

  public static final String REQUEST_PARAM_FIELD = "field"; // 采集任务筛选字段
  public static final String REQUEST_PARAM_SORT = "sort"; // 采集任务排序类型 可选值为name/updateTime
  public static final String REQUEST_PARAM_ORDER = "order"; // 采集任务排序类型 可选值为asc/desc
  public static final String REQUEST_PARAM_PAGE_NO = "pageNo"; // 采集任务分页显示页码
  public static final String REQUEST_PARAM_PAGE_SIZE = "pageSize"; // 采集任务分页单页显示数量
  public static final String REQUEST_PARAM_PREFIX = "prefix"; // 采集任务名称前缀 可筛选
  public static final String REQUEST_PARAM_ENABLED = "enabled"; // 采集任务的状态 可筛选
  public static final String REQUEST_PARAM_OWNER = "owner"; // 采集任务的所有者 可筛选
  public static final String REQUEST_PARAM_SOURCE_TYPE = "sourceType"; // 采集任务的来源类型 可筛选
  public static final String REQUEST_PARAM_REPO = "repo"; // 采集任务的仓库 可筛选

  public static final String FIELD_PARAM_OWNER = "owner";
  public static final String FIELD_PARAM_SOURCE_TYPE = "sourceType";
  public static final String FIELD_PARAM_REPO = "repo";
  public static final String FIELD_PARAM_ENABLED = "enabled";

  private final CollectorConfigService collectorConfigService;

  @Autowired
  public CollectorConfigController(CollectorConfigService collectorConfigService) {
    this.collectorConfigService = collectorConfigService;
  }

  @GetMapping(value = "/configs/{id}")
  public @ResponseBody List<CollectorConfigVo> getConfigListByIds(
      @PathVariable(value = REQUEST_PATH_ID) final List<Long> ids) throws QiniuException {
    return collectorConfigService.getConfigListByIds(ids).stream()
        .map(CollectorConfigVo::new)
        .collect(Collectors.toList());
  }

  // TODO
  @GetMapping(value = "/configs/list")
  public @ResponseBody CollectorConfigPaginatedVo getPaginatedConfigs(
      @RequestParam(value = REQUEST_PARAM_FIELD, required = false) final String field,
      @RequestParam(value = REQUEST_PARAM_SORT, required = false) final String sort,
      @RequestParam(value = REQUEST_PARAM_ORDER, required = false) final String order,
      @RequestParam(value = REQUEST_PARAM_PAGE_NO, required = false) final String pageNoStr,
      @RequestParam(value = REQUEST_PARAM_PAGE_SIZE, required = false) final String pageSizeStr,
      @RequestParam(value = REQUEST_PARAM_PREFIX, required = false) final String prefix,
      @RequestParam(value = REQUEST_PARAM_ENABLED, required = false) final List<Boolean> enabled,
      @RequestParam(value = REQUEST_PARAM_OWNER, required = false) final List<String> owner,
      @RequestParam(value = REQUEST_PARAM_SOURCE_TYPE, required = false)
          final List<String> sourceType,
      @RequestParam(value = REQUEST_PARAM_REPO, required = false) final List<String> repo)
      throws QiniuException {
    // field双空时走分页逻辑
    if (field == null) {
      if (pageNoStr == null || pageSizeStr == null) {
        throw QiniuExceptions.InvalidArgument("pageNo and pageSize must be specified");
      }
      Pagination pagination =
          new Pagination(Integer.parseInt(pageNoStr), Integer.parseInt(pageSizeStr), sort, order);
      if (prefix != null) {
        pagination.setPrefix(prefix);
      }
      // 根据enabled, sourceType, repo, owner, type进行筛选
      if (enabled != null && !enabled.isEmpty()) {
        pagination
            .getFilter()
            .put(
                "enabled", enabled.stream().map(elem -> elem ? 1 : 0).collect(Collectors.toList()));
      }
      if (owner != null && !owner.isEmpty()) {
        pagination.getFilter().put("owner", owner);
      }
      if (sourceType != null && !sourceType.isEmpty()) {
        pagination.getFilter().put("sourceType", sourceType);
      }
      if (repo != null && !repo.isEmpty()) {
        pagination.getFilter().put("repo", repo);
      }
      ResourceList<CollectorConfig> configs = collectorConfigService.getConfigs(pagination);
      return new CollectorConfigPaginatedVo(configs.getTotalCount(), configs.getResourceList());
    } else {
      if (!Arrays.asList(
              FIELD_PARAM_OWNER, FIELD_PARAM_SOURCE_TYPE, FIELD_PARAM_REPO, FIELD_PARAM_ENABLED)
          .contains(field)) {
        throw QiniuExceptions.InvalidArgument("不支持根据该字段筛选");
      }
      return new CollectorConfigPaginatedVo(
          Collections.singletonList(getConfigListFilterValues(field)));
    }
  }

  @GetMapping(value = "/configs")
  public @ResponseBody List<CollectorConfigVo> getConfigs() throws QiniuException {
    return this.collectorConfigService.getConfigs().stream()
        .map(CollectorConfigVo::new)
        .collect(Collectors.toList());
  }

  @PostMapping(value = "/configs")
  public long addConfig(@Valid @RequestBody CollectorConfigRequest reqBody) throws QiniuException {
    CollectorConfig exist =
        this.collectorConfigService.getConfigByNameAndOwner(reqBody.getName(), reqBody.getOwner());
    if (exist != null) {
      throw QiniuExceptions.AlreadyExists("采集配置名称已存在");
    }

    this.collectorConfigService.addConfig(reqBody.toCollectorConfig());

    exist =
        this.collectorConfigService.getConfigByNameAndOwner(reqBody.getName(), reqBody.getOwner());
    if (exist == null) {
      throw QiniuExceptions.InternalServerError("添加采集任务失败");
    }
    return exist.getId();
  }

  @PostMapping(value = "/configs:batchDelete")
  public void deleteConfigListByIds(@Valid @RequestBody CollectorConfigBatchDeleteRequest reqBody)
      throws QiniuException {
    this.collectorConfigService.deleteConfigListByIds(reqBody.getIds());
  }

  @PostMapping(value = "/configs:status")
  public void updateConfigsStatus(@Valid @RequestBody CollectorConfigUpdateStatusRequest reqBody)
      throws QiniuException {
    List<CollectorConfig> exist = this.collectorConfigService.getConfigListByIds(reqBody.getIds());
    if (exist.isEmpty()) {
      throw QiniuExceptions.NotFound("采集配置不存在");
    }

    switch (reqBody.getStatus()) {
      case CollectorConfigUpdateStatusRequest.ENABLE:
        this.collectorConfigService.updateConfigsStatus(reqBody.getIds(), true);
        break;
      case CollectorConfigUpdateStatusRequest.DISABLE:
        this.collectorConfigService.updateConfigsStatus(reqBody.getIds(), false);
        break;
      default:
        throw QiniuExceptions.InvalidArgument("unsupported status type");
    }
  }

  @PutMapping(value = "/configs/{id}")
  public void updateConfig(
      @PathVariable(value = REQUEST_PATH_ID) final Long id,
      @Valid @RequestBody CollectorConfigRequest reqBody)
      throws QiniuException {
    List<CollectorConfig> configs = this.collectorConfigService.getConfigListByIds(List.of(id));
    if (configs.isEmpty()) {
      throw QiniuExceptions.NotFound("采集配置不存在");
    }

    CollectorConfig exist =
        this.collectorConfigService.getConfigByNameAndOwner(reqBody.getName(), reqBody.getOwner());
    if (exist != null && exist.getId() != id) {
      throw QiniuExceptions.AlreadyExists("采集配置名称已存在");
    }

    this.collectorConfigService.updateConfig(id, reqBody.toCollectorConfig());
  }

  private List<String> getConfigListFilterValues(String field) throws QiniuException {
    switch (field) {
      case FIELD_PARAM_OWNER:
        return this.collectorConfigService.getDistinctOwners();
      case FIELD_PARAM_SOURCE_TYPE:
        return this.collectorConfigService.getDistinctSourceTypes();
      case FIELD_PARAM_REPO:
        return this.collectorConfigService.getDistinctRepos();
      case FIELD_PARAM_ENABLED:
        return this.collectorConfigService.getDistinctStatus().stream()
            .map(elem -> elem ? "enabled" : "disabled")
            .collect(Collectors.toList());
      default:
        throw QiniuExceptions.InvalidArgument("不支持根据该字段筛选");
    }
  }
}
