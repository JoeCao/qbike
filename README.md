滴滴电动车
----
<u>本项目纯属虚构，如有雷同实属巧合</u>

### 项目背景

在嘀嘀打车的创业成功经验引领下你的创业团队发现了一个拥挤的大城市的电动车机会。
你们发现电动车由于其灵活性能够在拥挤的路面上穿行，从而能够帮助很多上班族避免早晚高峰，按时到达目的地，比滴滴打车更方便。
你们决定设计一个电动车的打车平台，能够在早晚高峰时匹配电动车司机和有需要的乘客。

### Quick Start
启动docker daemon进程  
mvn clean install -DskipTests=true    
docker-compose up mysql  
ctrl-c退出  
docker-compose up  

http://localhost:8050    

### 测试接口
安装postman，导入qbkie.postman_collection.json    
调用fetchUser接口，注意，spring cloud第一次调用可能会失败，需要预热一段时间。  
出现结果后，调用updateDriverPostion接口，将司机的坐标导入到redis中  
然后就可以使用placeIntention接口发起一个请求
然后再用confirmIntention接口，返回true就是成功匹配了，Order就会生成   
注意：confirm的接口中需要输入intentionId，如果是第一次，就是1，不清楚，可以到docker的MySQL的表t_intention中找  

在http://127.0.0.1:9411上可以找到zipkin对这几次调用的结果的追踪  
![zipkin](http://os8wjvykw.bkt.clouddn.com/2018-07-05-035127.png)

### Dev
请安装Lombok插件，Java8 Stream + Lombok + Spring boot， 你会拥有一个全新的Java开发体验。

### TODO
* [x] 完全基于领域驱动设计
* [x] 聚合根、领域对象、API的提炼按照`Event Storming`过程分析  
* [x] 升级到`Spring Boot 2.0` 和`Spring Cloud Finchley`
* [x] `Eureka`服务注册与发现  
* [ ] `Swagger2`管理`REST API`  
* [x] `Hystrix`服务的保护与容错  
* [x] `Zuul`网关应用  
* [x] `Zipkin`分布式追踪    


### 业务场景

初步设计的场景比较简单，主要是匹配有需求的乘客附近的电动车司机。经过前期的需求梳理，产出了下列的基本功能

- 司机发布自己的位置和状态（默认一辆电动车只能搭载一位乘客）
- 乘客提交行程需求（行程需要包含起点和终点，只支持实时提交，不支持预约）
- 系统自动为乘客匹配合适的司机，将需求发送至司机手中
- 司机确认是否接受行程
- 司机和乘客按订单完成行程
- 乘客行程途中要求改变行程
- 系统计费及完成订单
- 乘客对司机的评价


![主要场景.005](https://user-images.githubusercontent.com/3287183/50633723-54d8b280-0f87-11e9-84a9-c8727872a443.png)


### 业务流程

![业务流程.006](https://user-images.githubusercontent.com/3287183/50633730-5c985700-0f87-11e9-8232-7bf638bf70fc.png)


### 事件

![事件.008](https://user-images.githubusercontent.com/3287183/50633743-6a4ddc80-0f87-11e9-8c8b-f011c84d0314.png)


### 命令

![滴滴电动车需求.009](https://user-images.githubusercontent.com/3287183/50633750-70dc5400-0f87-11e9-9296-a731b7f3f899.png)


### 聚合识别


![滴滴电动车需求.010](https://user-images.githubusercontent.com/3287183/50633761-7afe5280-0f87-11e9-8156-bdd898c1e106.png)

![滴滴电动车需求.011](https://user-images.githubusercontent.com/3287183/50633776-881b4180-0f87-11e9-95ef-7286280e5af1.png)




### 系统架构



![image](https://user-images.githubusercontent.com/3287183/50633782-8e112280-0f87-11e9-9ac3-0f50941a2ae7.png)






