## 项目简介

基于鱼皮的 **API开放平台** 的思想，历时两个月，利用空闲时间完成了我自己的 **openapi开放平台** ,包含用户端、后台管理


在 **API开放平台** 基础的功能上新增了 **支付**、**订单**、**评论**、**文档**、**接口在线调试**、**OSS对象存储**、**短信/邮箱发送通知**等等...


把鱼皮没讲的防止请求重放攻击琢磨明白了（琢磨了好久 T_T）

用户端登录页：
![用户端登录页](https://zhangtianyou.oss-cn-chengdu.aliyuncs.com/openapi/%E7%94%A8%E6%88%B7%E7%AB%AF%E7%99%BB%E5%BD%95%E9%A1%B5.png)

用户端主页:
![用户端主页](https://zhangtianyou.oss-cn-chengdu.aliyuncs.com/openapi/%E7%94%A8%E6%88%B7%E7%AB%AF%E4%B8%BB%E9%A1%B5.png)

API详情页:
![API详情页](https://zhangtianyou.oss-cn-chengdu.aliyuncs.com/openapi/API%E8%AF%A6%E6%83%85%E9%A1%B5.png)

订单支付页:
![订单支付页](https://zhangtianyou.oss-cn-chengdu.aliyuncs.com/openapi/%E8%AE%A2%E5%8D%95%E6%94%AF%E4%BB%98%E9%A1%B5.png)

个人中心:
![个人中心](https://zhangtianyou.oss-cn-chengdu.aliyuncs.com/openapi/%E4%B8%AA%E4%BA%BA%E4%B8%AD%E5%BF%83.png)


## 技术选型

**前端**
~~~
    vue3 全家桶
    vite4
    element-plus, sass 组件库和css预处理器
    pinia,localforage,js-cookie 数据存储
    mavon-editor markdown编辑器
~~~
**后端**
~~~
    SpringCloud
    SpringBoot
    Mybatis-Plus
    Sentinel 
    Swagger 接口文档
    RabbitMQ 消息队列
    dubbo RPC调用
    Redis 非关系型数据库
    Nacos 服务注册、配置
    Mysql 关系型数据库
~~~

## 系统模块

~~~
openapi-cloud
      ┣━━━openapi-api-service       // api服务
      ┣━━━openapi-auth              // 认证中心服务
      ┣━━━openapi-client-sdk        // 客户端sdk，便于调用api
      ┣━━━openapi-common            // 公共模块
                 ┗━━━openapi-common-core                          //  核心模块
                 ┗━━━openapi-common-dubbo                         //  RPC调用模块
                 ┗━━━openapi-common-rabbitmq                      //   MQ消息模块
                 ┗━━━openapi-common-redis                         //   redis缓存模块
                 ┗━━━openapi-mvc-config                           //   MVC配置模块
      ┣━━━openapi-gateway           // 网关服务
      ┣━━━openapi-modules          
                 ┗━━━openapi-email                                //  邮箱服务
                 ┗━━━openapi-file                                 //  文件服务
                 ┗━━━openapi-interface                            //  接口服务
                 ┗━━━openapi-pay                                  //  支付服务
                 ┗━━━openapi-sms                                  //  短信服务
~~~

## 后台功能

~~~
用户管理：该功能主要完成系统用户配置。
接口管理：对接口信息、文档的配置
套餐管理：对接口套餐的配置
文档管理：对系统md格式文档的配置
订单管理：对订单的数据查询
~~~


## 技术亮点

~~~
1、防止请求重放攻击
2、MQ处理订单超时
3、MQ异步发送短信、邮箱
4、工厂模式、策略模式、枚举策略模式...
5、支付宝当面付
6、nacos动态配置
7、线程池异步编程
8、jwt Token实现用户登录鉴权
...
~~~


## 源代码

GitHub：[openapi-cloud](https://github.com/bluecat0419/openapi-cloud)

另外非常感谢 @[樊凡](https://gitee.com/fanxiao623)  @[KZ137onethe](https://github.com/KZ137onethe) 写的前端
