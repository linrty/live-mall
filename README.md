## 直播带货购物平台

这是一个微服务架构的直播带货购物平台

**项目特点：**

* 使用读写分离技术，提高性能
* 实现IM通讯系统，在线聊天室
* 直播间红包雨抢红包功能
* 直播带货秒杀功能\
**······**

### 技术栈

* 微服务注册中心：Nacos
* 网关： Spring Cloud Gateway
* Rpc远程调用： Dubbo
* 配置中心： Nacos
* 缓存： Redis
* 数据库： MySQL
* 消息队列： Kafka
* 分库工具： Sharding-Jdbc
* IM搭建： Netty
* 数据库版本管理： flyway

Spring Cloud Alibaba 版本： 2022.0.0.0-RC1 \
Spring Boot 版本： 3.0.4 \
JDK 版本： 17 

### 项目结构
* cloud-live-api : Rpc远程调用暴露接口
* cloud-live-common : 公共模块(包括公共配置、公共工具类、DTO/VO实体类、公用PO实体类、枚举类等)
* cloud-live-gateway : 网关
* cloud-live-im : IM通讯服务器模块
* cloud-live-living : 直播模块
* cloud-live-msg : IM消息处理中台
* cloud-live-pay : 支付中台
* cloud-live-router : IM消息路由模块
* cloud-live-shop : 购物模块
* cloud-live-user : 用户中台

### 重要功能介绍

#### 数据库读写分离实现

#### 延迟双删场景实现

#### IM通讯系统

#### 开播与直播列表功能实现 （读多写少场景）

#### 抢红包功能

#### 直播带货秒杀功能

#### 直播推拉流

#### Docker部署