# GWT、GWTP and Shiro combination
> A combination of GWT, GWTP and Shiro  
> 
> [GWT](https://github.com/gwtproject/gwt) Development toolkit for building and optimizing complex browser-based applications  
> [GWTP](https://github.com/ArcBees/GWTP) Model-view-presenter framework  
> [Shiro](https://shiro.apache.org/) Java security framework  

## gwtp-frame-shiro archetype 目标
> 提供一个构造项目的基本框架，用该框架构造的项目具有如下基本特征

- 拥有基础的用户权限管理能力，能够创建用户，授权用户角色
- 能够通过基于角色的权限控制（Role-Based Access Control）提供服务能力
- 界面具有管理控制台风格

## gwtp-frame-shiro项目使用方式
使用gwtp-frame-shiro archetypes构造一个新项目，参照以下方式。构建新项目的时候，用户可以修改groupId，并需要按照提示输入自定义module名称，比如risetek.tools。
```
mvn archetype:generate -DarchetypeCatalog=local -DarchetypeGroupId=com.risetek.archetypes \
 -DarchetypeArtifactId=gwtp-frame-shiro  \
 -DarchetypeVersion=HEAD-SNAPSHOT -DgroupId=com.risetek
```

## 技术要点
### User、 Subject and Project概念
- User: 用户，拥有访问权限使用项目各种服务资源的主体，用户用用户名和密码进行鉴权，服务端的各种服务根据用户被分配的角色授权访问。
- Subject: The currently executing user, called a Subject.
- Project: 项目
- Host Project：正在提供服务的项目称为host project。固定的角色设置，开发者需要在源码级别扩展

### **Client**:PresenterModuleMavenProcesser
> client中的module.gwt.xml有 \<set-configuration-property name="gin.ginjector.modules" value="${package}.entry.MavenProcessedPresenterModuleLoader"/\>设置， 该设置表明在构建运行时（GWT compiler 运行时）采用google gin进行各个模块（标注了@AutoLoadPresenterModule）的依赖注入。<br/>该方法是通过**maven-processor-plugin**用**PresenterModuleMavenProcesser**（源码参看：com.risetek.bindery.generator.PresenterModuleMavenProcesser）实现的。<br/>
设计PresenterModuleMavenProcesser的目的是为了减少项目对这些模块的耦合度，自动化加载用户编写的各种PresenterModule，这使得用户构建的新项目可以通过不修改其它代码的情况下，直接删除不需要的PresenterModule代码来达到自由组合的需要。<br/>
**TODO**: 项目需要至少有一个AutoLoadPresenterModule标注（annotation）的模块，否则没有机会生成 MavenProcessedPresenterModuleLoader，造成构建失败。

### **Client**:MyBootstrapper
> 用户信息前后台同步处理：\<set-configuration-property name="gwtp.bootstrapper" value="${package}.entry.MyBootstrapper" /\>使得MyBootstrapper在浏览器载入界面运行的初期得以运行在浏览器中，它会启动与后台的用户授权信息同步过程。<br/>
这个过程中，CurrentUser作为一个Singleton被及早实例化，以提供其它需求。

### MyAuthorizingRealm
> 服务端shiro/MyAuthorizingRealm是管理用户权限的DAO（数据访问目标），如果后台需要实现不同的管理提供，需要修改这个部分的代码。

### **Client**:UI:Infinity Card List
* 实现了一个无止境的卡片布局容器，InfinityCardList只能实现一列的卡片布局方式。
* Card需要按照CSS响应式设计方式，当width发生变化的时候，通过变更height来适应外部Panel的width变化。这种单个Card的height变化造成的List布局变化会由InfinityCardList通过调整TOP值消化。

#### **Client**:CardWidget 抽象类
> 该抽象类用于耦合数据和布局间的关系。并提供点击/选择卡片时的事件接口。

#### **Client**:CardPresenterWidget and CardInfinityView
> CardPresenterWidget 提供了与搜索关键字提供方的结合方式，也提供了卡片点击（selected）后，与消费方的结合方式。
> CardInfinityView 实现了卡片的列表式布局，上下移动，以及数据的吸取等。

### **Client**:Menu
> Dock Menu：固定在workspace区域，提供脱离于workspace布局区的Function Panel以容纳其它应用需求呈现   
> Function Menu：呈现在Dock Menu的Function Panel区  

Dock Menu依附于workspace Layout的Header区域作为起点。dock menu用Icon标志其功用，用tips加以提示，用跟随该Icon位置的Function Panel提供可进一步开发的功能。因此，实现上，位于Header区域的Dock Menu拥有三个Layer，一个用于放置IconButton，一个用于显示tips，一个用于提供Function Panel。  
在UI布局上，除了Icon需要占据全局布局的位置，tips panel和function panel都浮动（脱离）于workspace，并具有优先的index。  

#### Simple Navigation menu
应用Function Panel区实现简单的引导各个Modules呈现的Dock Menu

#### Simple Login menu
基础的引导用户登录系统，并提供已登录账户信息的Dock Menu实现

### **Client**:UI:SheetField
> 设计表单的编辑功能，除了布局的考虑，很大工作量在Field的有效性验证，输入信息的顺序安排等事务上，SheetField为规范并简化这个设计做了一些尝试性的工作。

设计思路是将各个Filed串联起来，以处理输入顺序问题。每个Filed的输入都需要在其前序Filed有了有效输入信息后才可用，输入的焦点(Focus)因此不能随意变更。这种方式牺牲了自由度，但是为Filed的跳转提供了便利，由此在很多情况下，Tab键和RETURN键都可以实现将输入焦点转移到下一个需要输入的Field上。  
每个Filed可以加入一个或多个有效性验证的函数，并通过构造Filed时候提供类型的办法，为常用格式（比如：email， password等）的Filed创建提供了帮助。  
> asHeader，checkOnFocus，从headerField逐一检查能否留住焦点  


> set  
SheetField采用Builder设计模式，减少编写时候的困难。以下是创建用户账号的例子：
```
            // Build SheetField chain.
            new SheetField.Builder(boxAccount).asHeader().set(isStop -> {
                getUiHandlers().checkValidate(boxAccount.getValue(), (state) -> {
                    setValidateState(state);
                    if(state == AccountValidate.CHECKING)
                        return;
                    if(state == AccountValidate.VALIDATE)
                        isStop.accept(false);
                    else {
                        btnCommit.setEnabled(false);
                        isStop.accept(true);
                    }});
            }).checkKeyPress().build()
            .nextField(boxPassword).checkOnFocus().minLength(4).build()
            .nextField(boxPassword2).checkOnFocus().set(isStop -> {
                if(!boxPassword.getValue().equals(boxPassword2.getValue())) {
                    boxPassword2.setFocus(true);
                    isStop.accept(true);
                } else {
                    btnCommit.setEnabled(true);
                    isStop.accept(false);
                }
            }).build()
            .nextField(boxEmail).checkOnFocus().type(TYPE.EMAIL).build()
            .nextField(boxTelphone).checkOnFocus().type(TYPE.TELPHONE).build()
            .nextField(boxNotes).checkOnFocus().build()
            .nextField(btnCommit).build();
```
#### **Client**:Search input support
> 数据搜索需要提供搜索关键字，为了减少输入搜索关键字的输入界面与消费者的耦合度，通过为CardPresenterWidget提供Supplier接口函数，提供其子类获得搜索字的能力。
```
    cardPresenter.searchKeyProvider = () -> {
        return getView().getSearchKey();
    };
```

### **Client**:Client handler Exception from Server
> 这部分的设计目的是为了提供一个友好的错误提示和处理能力  
> Action是GWTP前后端通讯的基础。ActionException是服务后端出现异常后抛出来的，这个异常信息需要透过某种方式传递到客户端，并转换成客户端能使用的格式。

* 服务端执行Action产生的ActionException需要通过ActionExceptionMapper转换成可序列化的ActionException才能通过RPC过程传递到客户端。
* 客户端调用Action执行服务端程序如果出现异常，会回调onFailuer函数，ServerExceptionHandler帮助对这些服务端异常的通常处理，比如onFailure得到的服务端异常类型是ActionAuthenticationException，那么就前往UnauthorizedPlace，通常这是一个Login界面。
* 可序列化的ActionException在xxx.share.exception包中。
* ServerExceptionHandler类在client的utils.ServerExceptionHandler。

### **Server**:Service side initialization
* DevOpsTask提供了一种跟踪，记录服务端初始化的办法。
* 特别地，如果服务端没有提供合适的账户进行后续的作业，可以进入/services页面处理。

### **Server**:Shiro/Accounts
> 基于Shiro的功能，本项目支持账户/项目管理。本项目可以构造成一个OAuth服务，用于管理账户/项目，并提供给其它服务。  
> 为了达到这个目的，几个基本性质必须得到满足：

- 所有Accounts都是是本项目（Host project）的用户，并拥有基本的项目角色/权限，还能分配本项目的其它角色。
- Accounts可以指派成为其它项目的用户，并通过Roles设置来赋予操作权限。对Roles操作权限的诠释基于那个项目的设计，与Host project无关，Host project仅仅提供Roles名称信息。
- 小型的项目可以没有Projects支持，直接删除client/presentermodules/accounts/project目录就可以了。但是数据库中仍然保留对多项目的支持，这部分开销不大。
- 小型的项目甚至可以不需要accounts管理，直接删除client/presentermodules/accounts/就可以了。但是为了支持操作权限，需要与别的OAuth服务配合。如果使用其它服务提供OAuth，服务端服务程序和相关的数据库管理也可以删除。

### **Server**:Hibernate ORM
> [Hibernate ORM](https://hibernate.org/orm/) is an Object/Relational Mapping (ORM) framework  
> 引入该框架的目的是更好地适应不同的数据库服务。

服务端通过 bind(SessionFactory.class).toProvider(HibernateSessionFactoryProvider.class); Hibernate ORM提供了会话管理。

### **Server**:SmallRye Config
> SmallRye Config is a library that provides a way to configure applications  
> 部署初期，没有建立任何账号，本项目依赖SmallRye提供的配置方式，临时提供管理账号。

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
* 如果声明了系统属性hsqlmem，那么hsqldb将运行在内存数据库模式下，重启服务意味着数据会丢失。代码可以查看HibernateSessionFactoryProvider.java。
* /config目录，是用于提供给microprofile额外（用户自定义）配置文件的目录。
* 如果出现忘记管理账户的情况，也可以通过临时加入deploy.properties文件的方式加以挽救。该文件位于/config目录下。
* 在没有创建账户的情况下，也需要配置文件 deploy.properties 的介入。部署账户登录后，请及时创建有效的管理账户，然后删除deploy文件，以保障安全。

```
deploy.account=deploy
deploy.password=deploy
```
## 参考
* 关于ssh remote 的sudo需要密码问题，请参考https://www.shell-tips.com/linux/sudo-no-tty-present-and-no-askpass-program-specified/

## TODO
* 服务端AotoLoadModule的处理在增加一个新的模块时会失效，开发者必须重新编译整个项目，而不能依赖于jetty的restart过程。
* 如何实现一种模板配置的方式，通过声明需要的package来组织成初始代码。
