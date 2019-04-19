risetek.archetypes
====================
* This is from: https://github.com/tbroyer/gwt-maven-archetypes
* 在此表达对原作者的敬意，构造本项目的目的只是希望有一个自己需要的组合

### 下载并构造本maven包到本地repo中
* git clone https://github.com/kerongbaby/risetek.archetypes.git
* cd risetek.archetypes && mvn clean install

### 构造项目，缺省module名称为App
	mvn archetype:generate -DarchetypeCatalog=local \
	 -DarchetypeGroupId=com.risetek.archetypes \
	 -DarchetypeVersion=HEAD-SNAPSHOT \
	 -DgroupId=com.risetek \
	 -DarchetypeArtifactId=modular-webapp

### 构造项目，自定义module名称，比如demo
	mvn archetype:generate -DarchetypeCatalog=local \
	 -DarchetypeGroupId=com.risetek.archetypes \
	 -DarchetypeVersion=HEAD-SNAPSHOT \
	 -DgroupId=com.risetek \
	 -DarchetypeArtifactId=modular-webapp \
	 -Dmodule=demo

### archtypes
* modular-webapp:
* empty-webapp: GWT基本界面，以及RPC实现的Greeting服务
* empty-frame: requestFactory
* empty-frame-shiro: requestFactory, login/logout Place and Apache shiro


### 说明
* 如果server代码修改后，jetty没有重新开始（restart）,请检查Eclispe的设置，Project->Build Automatically应该为选中。
* -DarchetypeCatalog=local参数限制在本地搜寻archetypeGroupId，因此不会把时间浪费在不存在的网络资源搜寻上。

### 项目构造
* 在eclipse下import-> Maven -> Existing Maven Projects，可以同时将三个项目纳入eclipse集成开发环境中。
* 也可以在生成的项目目录下执行：mvn eclipse:eclipse构造eclipse项目文件.project

### 调试
* 在一个终端（窗口）中运行: `mvn gwt:codeserver`
* 在另一个终端（窗口）中运行: `mvn jetty:run -P env-dev`
* 在一个终端（窗口）中运行: `mvn gwt:codeserver -pl client -am`
* 在另一个终端（窗口）中运行: `mvn jetty:run -pl server -am -Denv=dev` 或者：`mvn jetty:run -pl server -am -P env-dev`
* Chrome浏览器打开jetty服务的地址，比如：`http://127.0.0.1:8080`，而不是gwt:codeserver的地址

### 调试脚本
	#!/bin/bash
	set -e
	mvn clean package
	git-bash.exe -c "mvn jetty:run -pl server -am -Penv-dev" &
	git-bash.exe -c "mvn gwt:codeserver -pl client -am" &

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
