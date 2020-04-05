risetek.archetypes
====================
* This is from: https://github.com/tbroyer/gwt-maven-archetypes
* 在此表达对原作者的敬意，构造本项目的目的只是希望有一个自己需要的组合

### 下载并构造本maven包到本地repo中
* git clone https://github.com/kerongbaby/risetek.archetypes.git
* cd risetek.archetypes && mvn clean install

### 构造项目，缺省module名称为App
```
mvn archetype:generate -DarchetypeCatalog=local \
 -DarchetypeGroupId=com.risetek.archetypes \
 -DarchetypeVersion=HEAD-SNAPSHOT \
 -DgroupId=com.risetek \
 -DarchetypeArtifactId=modular-webapp
```

### 构造项目，自定义module名称，比如demo
```
mvn archetype:generate -DarchetypeCatalog=local \
 -DarchetypeGroupId=com.risetek.archetypes \
 -DarchetypeVersion=HEAD-SNAPSHOT \
 -DgroupId=com.risetek \
 -DarchetypeArtifactId=modular-webapp \
 -Dmodule=demo
```

### archtypes
* modular-webapp:
* empty-webapp: GWT基本界面，以及RPC实现的Greeting服务
* empty-frame: requestFactory
* empty-frame-shiro: requestFactory, login/logout Place and Apache shiro


### 说明
* 如果server代码修改后，jetty没有重新开始（restart）,请检查Eclispe的设置，Project->Build Automatically应该为选中。
* -DarchetypeCatalog=local参数限制在本地搜寻archetypeGroupId，因此不会把时间浪费在不存在的网络资源搜寻上。
* 由于GWTP使用的是GIN，因此没有在dagger上下功夫。

### 项目构造
* 在eclipse下import-> Maven -> Existing Maven Projects，可以同时将三个项目纳入eclipse集成开发环境中。
* 也可以在生成的项目目录下执行：mvn eclipse:eclipse构造eclipse项目文件.project

### 调试
* 在一个终端（窗口）中运行: `mvn gwt:codeserver -pl client -am`
* 在另一个终端（窗口）中运行: `mvn -Djetty.port=8081 jetty:run -pl server -am -Denv=dev` 或者：`mvn -Djetty.port=8081 jetty:run -pl server -am -P env-dev`
* Chrome浏览器打开jetty服务的地址，比如：`http://127.0.0.1:8081`，而不是gwt:codeserver的地址
* 需要注意的是，gwt:codeserver存在退出后没有释放java进程的问题。比如关闭了bash窗口，java进程还存在，而且占用了调试端口，这个时候需要在`任务管理器`里找到这个java进程，手动清除，否则下一次运行gwt:codeserver会存在端口占用问题而无法执行。

### 调试脚本
```
#!/bin/bash
set -e
mvn clean package
git-bash.exe -c "mvn jetty:run -pl server -am -Penv-dev" &
git-bash.exe -c "mvn gwt:codeserver -pl client -am" &
```

### 增添新的Place
* 实现一个Place的扩展
* ${module}PlaceHistortMapper需要更新，在@WithTokenizers中引入新增的Place对应的class
* MainActivityMapper需要增加处理该Place活动的代码

### 增添新的requestFactory服务
### server
* 实现Response对应的数据结构（Entity）
* 实现返回Response的服务

#### shared
* ${module}Factory中增加对应的服务上下文: RequestContext.
* ResponseProxy对应于服务端（server）代码中的Entity
* 实现RequestContext的extends，里面包含服务名，并返回Request<T>，<T>与ResponseProxy相关

### 几个特殊文件的作用
* (client) AuthAwareRequestTransport.java
* (server) AjaxAuthenticationFilter.java
* User.java CurrentUser.java & ServerUser.java

### gwtp-frame-shiro NOTES
* client中的module.gwt.xml有<set-configuration-property name="gin.ginjector.modules" value="com.risetek.entry.ClientModule"/> 构建运行时（GWT compiler 运行时）采用google gin进行依赖注入。
* ClientModule在GWT compiler运行时install各种GinModule。这些被安装的GinModule又会进行其自身领域的依赖注入。
* 除了GWTP必要的GinModule外，开发者的AbstractPresenterModule也需要进行install。这部分如何实现自动化的安装呢？
* bind MyBootstrapper使得GWTP项目在浏览器载入界面运行的初期得以运行在浏览器中，它会启动与后台的用户授权信息同步过程。
* CurrentUser作为一个Singleton被及早实例化，以提供其它需求。

### gwtp-frame-shiro PresenterModuleMavenProcesser NOTES
* 设计PresenterModuleMavenProcesser是为了自动化转载用户编写的各种PresenterModule，这使得用户构建的新项目可以通过删除不需要的PresenterModule代码来达到自由组合的需要。
* 至少有一个用户的PresidentModule 需要 AutoLoadPresenterModule annotation。否则没有机会生成 MavenProcessedPresenterModuleLoader，造成构建失败。 

==================================

#### 以下是原始文档
    mvn archetype:generate \
       -DarchetypeCatalog=https://oss.sonatype.org/content/repositories/snapshots/ \
       -DarchetypeGroupId=net.ltgt.gwt.archetypes \
       -DarchetypeArtifactId=<artifactId> \
       -DarchetypeVersion=1.0-SNAPSHOT

where the available `<artifactIds>` are:

* `modular-webapp`
* `modular-requestfactory`
* `guice-rf-activities`

This uses the snapshot deployed to Sonatype OSS. Alternatively, and/or if you want to
hack on / contribute to the archetypes, you can clone and install the project locally:

    git clone https://github.com/tbroyer/gwt-maven-archetypes.git
    cd gwt-maven-archetypes && mvn clean install

You'll then use the `mvn archetype:generate` command from above, except for the
`-DarchetypeCatalog` argument which you'll remove, as you now want to use your local
catalog.

### Start the development mode

Change directory to your generated project and issue the following commands:

1. In one terminal window: `mvn gwt:codeserver -pl *-client -am`
2. In another terminal window: `mvn tomcat7:run -pl *-server -am -Denv=dev`

The same is available with `tomcat6` instead of `tomcat7`.

Or if you'd rather use Jetty than Tomcat, use `cd *-server && mvn jetty:start -Denv=dev` instead of `mvn tomcat7:run`.

Note that the `-pl` and `-am` are not strictly necessary, they just tell Maven not to
build the client module when you're dealing with the server one, and vice versa.


### Profiles

There's a special profile defined in the POM file of server modules:
`env-dev`, which is used only when developping. It configures the Tomcat and Jetty
plugins and removes the dependency on the client module (declared in the `env-prod`
profile, active by default.)

To activate the `env-dev` profile you can provide the `-Denv=dev` system property, or
use `-Penv-dev`.
