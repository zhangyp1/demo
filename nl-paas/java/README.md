# paas-all
## microservice-manager：微服务管理
### config-center：配置中心
### eureka-server：服务注册和发现

## paas-3rd：第三方sdk
### sdkConnection：sdk连接

## paas-data-module：paas数据库访问模块
### activiti-flow-data-module：工作流管理-数据库访问模块
### appmgr-data-module：应用管理-数据库访问模块
### astmgr-data-module：资产管理-数据库访问模块
### clumgr-data-module：集群管理-数据库访问模块
### config-center-data-module：配置中心-数据库访问模块
### data-common-module：数据公共模块-数据库访问模块
### dev-astmgr-data-module：开发中心-数据库访问模块
### execengn-data-module：执行引擎-数据库访问模块
### framework-data-module：框架管理-数据库访问模块
### resmgr-data-module：资源管理-数据库访问模块
### svrmgr-data-module：服务管理-数据库访问模块
### sysmgr-data-module：系统管理-数据库访问模块

## paas-data-module-nj：南京相关模块

## paas-dev-service：开发中心相关模块

## paas-nj：南京相关模块

## paas-parent：父pom（公共pom）

## paas-service：平台服务
### activiti-flow：工作流管理
### appmgr：应用管理
### astmgr：资产管理
### auth：权限管理
#### auth-client：权限客户端
#### auth-common：权限公共模块
#### auth-server：权限服务端
### clumgr：集群管理
### controllerapi：服务之间调用vo类
### execute-engine-svr：执行引擎
### frwmgr：框架管理
### gateway：网关
### ingress-manager：ingress管理
### resmgr：资源管理
### svrmgr：服务管理
### sysmgr：系统管理

## paas-tool：paas工具
### testngmocktool：testng数据mock工具

## paas-service-nj：南京相关模块

## public-module：公共模块
### common：公共，工具类
### execute-engine：执行引擎（旧）
### log：日志模块
### prefab_sql：数据库预置查询模块（旧）
### prefab_sq_query：数据库预置查询模块（暂未使用）
### spring-advice：spring切面配置
### spring-boot-common：微服务公共配置

``` bash
# 1、install paas-parent（公共pom）
# 2、install paas-all

# 启动顺序
## 1、eureka-server
## 2、config-center
## 3、具体服务