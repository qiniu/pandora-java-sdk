## 关于

该项目是一个基于Pandora Python SDK的后端自定义触发项目。

### APP 的目录结构

```
.
├── appserver          #  前端静态文件
│     └── static
│         ├── font
│         ├── img
│         └── js
├── bins                  # APP 后端脚本目录
│     ├── __init__.py   
│     ├── api_example.py         # 自定义API的示例
│     ├── spl_example.py         # 自定义SPL的示例
│     ├── libs                         # 脚本依赖库存放地址
│     └── tests                       # 单元测试目录
│         ├── __init__.py
│         ├── test_api_example.py
│         └── test_spl_example.py
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
├── requirements.txt             # 需要打包的依赖库，pip freeze 可以更新
├── run.sh                           # 打包小工具
└── static                            # APP的图标
    └── icon.png
```

### 开发流程

####  下载样例项目

可以从[github](https://github.com/qiniu/pandora-python-sdk.v2) template 目录获得样例项目。下载到本地。

1. 修改 app.json 文件设置项目名称等信息
2. 执行`./run.sh init_local` 安装项目初始依赖


#### 修改自定义API代码逻辑

##### 配置设置

修改 resourecs/customapis/api.json 更改自定义API内容

```json
{
  "prefix": "/test",                      # API请求地址
  "env": "python3",                     # API 运行环境
  "filename": "tenants.py",    # API 对应脚本名字，位于 bins/ 目录下
  "method": [
    "GET",
    "PUT"
  ]                                           # 允许的HTTP 请求方法
}
```

##### 代码编写

接下来修改自定义API的响应方法，位于 bins/api_example.py 路径下，覆盖 `do_handle_data` 方法实现你自己的API业务逻辑。如果需要引入第三方依赖可以使用pip 进行安装，建议及时可以将依赖同步到requirements文件中

```shell
pip install thirdparty

pip freeze > requirements.txt
```


##### 单元测试

修改 bins/tests/test_api_example.py 路径下的单元测试文件，通过SDK提供的 mock 工具测试HTTP 请求逻辑。

##### 运行单元测试

在项目目录下执行 `./run.sh unittest` 运行单元测试，查看运行结果。


#### 修改自定义SPL代码逻辑

##### 配置设置

修改 resourecs/customoperators/spl.json 更改自定义SPL内容

```json
{
  "name": "sample",              # SPL 命令名称
  "type": "centralized",          # SPL 算子类型
  "env": "python3",               # SPL 脚本运行环境
  "filename": "spl_example.py"    # SPL 脚本，位于 bins 目录下
}
```

##### 代码编写

接下来修改自定义SPL的执行方法，位于 bins/spl_example.py 路径下，覆盖 `streaming_handle` 方法实现你自己的API业务逻辑。如果需要引入第三方依赖可以使用pip 进行安装，建议及时可以将依赖同步到requirements文件中

```shell
pip install thirdparty

pip freeze > requirements.txt
```


##### 单元测试

修改 bins/tests/test_spl_example.py 路径下的单元测试文件，测试数据处理逻辑。

##### 运行单元测试

在项目目录下执行 `./run.sh unittest` 运行单元测试，查看运行结果。


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


#### 打包

可以使用 `./run.sh` 工具进行打包。该工具功能如下：

```
./run.sh [help|init_local|update_deps|package_deps|unittest|package|all|clean|docker_build]

Usage:
    help: output this information
    init_local: install dependencies in requirements.txt
    update_deps: freeze dependencies from pip to requirements.txt
    package_deps: package dependencies from pip into bins/libs directory
    unittest: run unittest in bins/tests directory
    package: package all file into archive file
    all: run all process
    docker_build: package in docker environment
    clean: clean all build files
```

可以使用 package 命令进行简单的打包工作

```
./run.sh package
```

如果在windows/mac 环境下开发，需要向linux环境打包APP，可以使用 docker_build 环境，注意，如果项目依赖C扩展，需要在 Dockerfile 中指定目标的 Python 环境。
如果需要node环境的话，建议使用 [python-nodejs](https://hub.docker.com/r/nikolaik/python-nodejs) 镜像。

```
./run.sh docker_build
```

当打包成功的话，APP会出现在 dist 目录下。


#### 上传

可以进入 Pandora 的服务环境的应用管理，进行上传了。

或者可以执行sdk中提供的 upload_app 工具进行上传。

```shell
upload_app your-app.tar.gz --host "${PANDORA_HOST}" --scheme "${PANDORA_SCHEME}" --port "${PANDORA_PORT}" --token "${PANDORA_TOKEN}"
```

或者可以使用run.sh 中的upload 命令，上传 dist/ 目录下生成的APP文件包。

```shell
./run.sh upload
```