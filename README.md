## Pandora Java SDK 说明文档

### 简介

Pandora Java SDK 包含七牛大数据服务平台Pipeline服务的数据打点API，支持多种方式进行Pandora平台数据接入。

### 使用方式

#### maven 依赖

在您的项目pom文件里面加入以下几行

```
<dependency>
    <groupId>com.qiniu</groupId>
    <artifactId>qiniu-pandora-sdk</artifactId>
    <version>2.1.0</version>
</dependency>
```

#### 使用范例

1. [顺次发送数据点](https://github.com/qiniu/pandora-java-sdk/blob/master/example/sender/src/main/java/SequenceSender.java)
2. [并发发送数据点](https://github.com/qiniu/pandora-java-sdk/blob/master/example/sender/src/main/java/ParallelSender.java)
3. [使用底层接口，攒成一批数据发送数据](https://github.com/qiniu/pandora-java-sdk/blob/master/example/sender/src/main/java/BatchPointsSender.java)
4. [自动重试失败数据点](https://github.com/qiniu/pandora-java-sdk/blob/master/example/sender/src/main/java/FaultTolerantDataSender.java)
5. [迭代生成数据点进行发送，避免把所有数据点缓存到内存中](https://github.com/qiniu/pandora-java-sdk/blob/master/example/sender/src/main/java/LazyPointsSender.java)
6. [从数据文件中读取内容进行发送](https://github.com/qiniu/pandora-java-sdk/blob/master/example/sender/src/main/java/FileSender.java)
7. [日志查询](https://github.com/qiniu/pandora-java-sdk/blob/master/example/logdb/src/main/java/Search.java)
8. [Elastic模式构建日志查询](https://github.com/qiniu/pandora-java-sdk/blob/master/sdk/src/test/java/com/qiniu/pandora/logdb/MultiSearchServiceTest.java)
