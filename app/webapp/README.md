## 关于

该项目是一个基于 JAVA SDK 的后端自定义触发项目。

### APP 的目录结构

```
.
├── appserver          #  前端静态文件
│     └── static
│         ├── form.xml       #  配置普通配置项(重要)
│         ├── advance.xml    #  配置高级配置项(重要)
├── default                          # 视图资源配置目录
│     ├── nav.xml                   # 导航栏配置
│     └── views                      # 视图配置
│         └── overview.xml
├── resources                      # 对象资源配置
│     ├── customapis              # 自定义API 目录
│     │     └── api.json
│     ├── customoperators      # 自定义SPL 目录
│     │     └── spl.json
│     ├── extractions              # 解析规则目录
│     └── sourcetypes             # 来源类型目录
├── Dockerfile                     # Docker 打包文件
├── app.json                       # APP 的全局设置
├── env.ignore                     # 不打包的依赖库
└── static                            # APP的图标
    └── icon.png
```

### 配置自定义表单,系统保留字段

- sourcetype # 来源类型
- transforms # 转换规则
- config # 暂时保留
- advancedConfig # 暂时保留
- advanced # 是否启动高级
- name # 名称
- description # 描述
- repoName # 仓库
- origin # 数据来源名称
- host # 主机名

#### 修改视图格式

##### 修改导航栏文件

修改 default/nav.xml 文件，格式如下，包括聚合页面和视图页面：

```xml
<nav>
	<collection name="collection" label="聚合页面" asset="collection.xml">
		<view name="view" label="视图页面" asset="view.xml" />
	</collection>
</nav>
```

##### 修改视图文件

修改 views/overview.xml，格式参见[文档](https://developer.qiniu.com/express/6562/xml-references)。简单示意如图

```xml
<html>
	<h1>概览信息</h1>
</html>
```
