# GWT、GWTP and Shiro combination
> A combination of GWT, GWTP and Shiro  
> 
> [GWT](https://github.com/gwtproject/gwt) Development toolkit for building and optimizing complex browser-based applications  
> [GWTP](https://github.com/ArcBees/GWTP) Model-view-presenter framework  
> [Shiro](https://shiro.apache.org/) Java security framework  

## User Subject and Project
- User: 用户
- Subject: The currently executing user, called a Subject.
- Project: 项目

## Host Project
- 项目本身的设定
- 固定的角色设置，开发者需要在源码级别扩展

## 构造gwtp-frame-shiro项目
需要按照提示输入自定义module名称，比如risetek.tools
```
mvn archetype:generate -DarchetypeCatalog=local -DarchetypeGroupId=com.risetek.archetypes \
 -DarchetypeArtifactId=gwtp-frame-shiro  \
 -DarchetypeVersion=HEAD-SNAPSHOT -DgroupId=com.risetek
```

## 技术要点
### PresenterModuleMavenProcesser
> client中的module.gwt.xml有 \<set-configuration-property name="gin.ginjector.modules" value="${package}.entry.MavenProcessedPresenterModuleLoader"/\>设置， 该设置表明在构建运行时（GWT compiler 运行时）采用google gin进行各个模块（标注了@AutoLoadPresenterModule）的依赖注入。<br/>该方法是通过**maven-processor-plugin**用**PresenterModuleMavenProcesser**（源码参看：com.risetek.bindery.generator.PresenterModuleMavenProcesser）实现的。<br/>
设计PresenterModuleMavenProcesser的目的是为了减少项目对这些模块的耦合度，自动化加载用户编写的各种PresenterModule，这使得用户构建的新项目可以通过不修改其它代码的情况下，直接删除不需要的PresenterModule代码来达到自由组合的需要。<br/>
**TODO**: 项目需要至少有一个AutoLoadPresenterModule标注（annotation）的模块，否则没有机会生成 MavenProcessedPresenterModuleLoader，造成构建失败。

### MyBootstrapper
> 用户信息前后台同步处理：\<set-configuration-property name="gwtp.bootstrapper" value="${package}.entry.MyBootstrapper" /\>使得MyBootstrapper在浏览器载入界面运行的初期得以运行在浏览器中，它会启动与后台的用户授权信息同步过程。<br/>
这个过程中，CurrentUser作为一个Singleton被及早实例化，以提供其它需求。

### MyAuthorizingRealm
> 服务端shiro/MyAuthorizingRealm是管理用户权限的DAO（数据访问目标），如果后台需要实现不同的管理提供，需要修改这个部分的代码。

### UI:Infinity Card List
* 实现了一个无止境的卡片布局容器，InfinityCardList只能实现一列的卡片布局方式。
* Card需要按照CSS响应式设计方式，当width发生变化的时候，通过变更height来适应外部Panel的width变化。这种单个Card的height变化造成的List布局变化会由InfinityCardList通过调整TOP值消化。

### Client handler Exception from Server
> Action是GWTP用来联系前后端的
* 服务端执行Action产生的ActionException需要通过ActionExceptionMapper转换成可序列化的ActionException才能通过RPC过程传递到客户端。
* 客户端调用Action执行服务端程序如果出现异常，会回调onFailuer函数，ServerExceptionHandler帮助对这些服务端异常的通常处理，比如onFailure得到的服务端异常类型是ActionAuthenticationException，那么就前往UnauthorizedPlace，通常这是一个Login界面。
* 可序列化的ActionException在xxx.share.exception包中。
* ServerExceptionHandler类在client的utils.ServerExceptionHandler。

### Service side initialization
* DevOpsTask提供了一种跟踪，记录服务端初始化的办法。
* 特别地，如果服务端没有提供合适的账户进行后续的作业，可以进入/services页面处理。

## 部署
### 用Docker进行部署
* 如果用Docker进行部署，部署服务器需要安装docker.io和docker-compose
* 用于部署的服务器账户需要具有sudo能力
* 用于部署的服务器账户执行sudo时如果需要输入密码，那么在部署期间需要多次输入这个密码。请阅读以下<strong>参考</strong>的内容以选择合适的方案。
* 部署完成后，在部署的工作区（WORKSPACE，由环境变量.env定义），存留完整的docker-compose.yml，可以用于管理。
```
如果使用Docker，Dockerfile需要用上两个卷：
VOLUME /config
VOLUME /.database
```
### 用nginx进行反向代理
* 数据服务通过不同的location组合在一起。
* 服务端需要得到浏览器访问的URL，因此部署时要考虑将这些信息真实地传递到服务端，比如在使用nginx的情况下，一个实际的配置例子：

```
server {
  listen 80;
  server_name devops.yun74.com;
  location / {
    proxy_pass http://localhost:19000;
    port_in_redirect off;
    proxy_set_header X-Real-IP $remote_addr;
    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    proxy_set_header Host $http_host;
  }
  location /iotms/ {
    proxy_pass http://localhost:190020/;
    port_in_redirect off;
    proxy_set_header X-Real-IP $remote_addr;
    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    proxy_set_header Host $http_host;
  }
}
```
### 部署时的配置
* 我们需要用上/.database目录，这是用于存放数据库文件的目录
* /config目录，是用于提供给microprofile额外（用户自定义）配置文件的目录
* 在没有创建账户的情况下，需要配置文件 /config/deploy.properties 的介入。用部署账户登录后，请及时创建有效的管理账户，然后删除配置文件中的deploy账户，以保障安全。
* 如果出现忘记管理账户的情况，也可以通过临时加入deploy的方式加以挽救。

```
deploy.account=deploy
deploy.password=deploy
```
## 参考
* 关于ssh remote 的sudo需要密码问题，请参考https://www.shell-tips.com/linux/sudo-no-tty-present-and-no-askpass-program-specified/

## TODO
* 服务端AotoLoadModule的处理在增加一个新的模块时会失效，开发者必须重新编译整个项目，而不能依赖于jetty的restart过程。
* 如何实现一种模板配置的方式，通过声明需要的package来组织成初始代码。
