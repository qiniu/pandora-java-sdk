Pandora-Java-SDK
=============

提供访问Pandora平台所需的接口以及服务端采集基础框架

平台接口
=============

1. app相关
2. 元数据管理
3. Token管理
4. 数据传输

服务端采集框架
=============
改造自flume EmbeddedAgent框架：
1. 增加自定义source、sink
2. 增加source meta管理

采集配置样例：
```json
{
    "source.type":"file",
    "source.input.file":"test.log",
    "channel.type":"memory",
    "channel.capacity": "200",
    "sinks":"sink1",
    "sink1.type":"pandora",
    "sink1.source_type": "json",
    "sink1.repo": "test",
    "processor.type": "default"
}
```

采集配置介绍：
1. source数据来源: file
    1. file: 文件按行采集，暂不支持增量采集
2. channel缓存管道: memory, file
    1. memory: 内存管道，数据暂存内存中，性能较高但程序崩溃时会导致数据丢失
    2. file: 使用本地文件作为缓存管道
3. sink发送源: pandora 
    1. pandora: 将数据发往pandora平台
4. processor类型: default、load_balance、failover
    1. default: 默认类型，不支持多sink。
    2. load_balance: 负载均衡，有round_robin(轮询)或random(随机)两种选择机制，默认round_robin。
    3. failover: 维护sink优先级列表，支持故障转移