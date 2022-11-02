# risetek.archetypes
>
> This is based on: https://github.com/tbroyer/gwt-maven-archetypes， 在此表达对原作者的敬意。<br/>
> 构造本项目的目的只是希望有一个自己需要的组合

## Supported archtypes
* modular-webapp:
* empty-webapp: GWT基本界面，以及RPC实现的Greeting服务
* empty-frame: requestFactory
* empty-frame-shiro: requestFactory, login/logout Place and Apache shiro
* dagger-guice-rf-activities：[A combination of dagger, Guice and GWT requestFactory, activities](https://github.com/cdrisetek/risetek.archetypes/blob/master/dagger-guice-rf-activities.md)
* gwtp-frame-shiro: [GWTP and Shiro combination](https://github.com/cdrisetek/risetek.archetypes/blob/master/gwtp.frame.shiro.md)

## 使用方法
> 由于没有发布到maven官方库中，因此需要**下载并构造本maven包到本地repo中**。
```
git clone https://github.com/cdrisetek/risetek.archetypes.git
cd risetek.archetypes && mvn clean install
```
## 使用Eclipse IDE作为开发工具
- 由于client和server依赖于shared模块，因此新项目需要首先执行mvn install，以提供模块之间的依赖关系。
- 在eclipse下import-> Maven -> Existing Maven Projects，可以同时将三个项目纳入eclipse开发环境中。
- 也可以在生成的项目目录下执行：mvn eclipse:eclipse构造eclipse项目文件.project。
- 如果server代码修改后，jetty没有重新开始（restart）,请检查Eclispe的设置，Project->Build Automatically应该为选中。

## 调试
* 在一个终端（窗口）中运行: 
```
mvn gwt:codeserver -pl client -am
```
* 在另一个终端（窗口）中运行:
```
mvn -Djetty.port=8081 jetty:run -pl server -am -Denv=dev
或者：
mvn -Djetty.port=8081 jetty:run -pl server -am -P env-dev
```

* Chrome，Edge浏览器打开jetty服务的地址，比如：`http://127.0.0.1:8081`，而不是gwt:codeserver的地址
* 需要注意的是，gwt:codeserver存在退出后没有释放java进程的问题。比如关闭了bash窗口，java进程还存在，而且占用了调试端口，这个时候需要在`任务管理器`里找到这个java进程，手动清除，否则下一次运行gwt:codeserver会存在端口占用问题而无法执行。<br/>在windows下的PowerShell，可以使用：__Get-Process java | Stop-Process -Confirm:$false__ 命令。

> bash调试脚本:
```
#!/bin/bash
set -e
mvn clean package
git-bash.exe -c "mvn jetty:run -pl server -am -Penv-dev" &
git-bash.exe -c "mvn gwt:codeserver -pl client -am" &
```

> PowerShell调试脚本：
```
$curDir = Get-Location
mvn clean package
if (-not $?) {throw "Failed to build package."}
Start-Process powershell.exe -WorkingDirectory $curDir -ArgumentList "mvn -q jetty:run -Dhsqlmem -pl server -am -Penv-dev"
Start-Process powershell.exe -WorkingDirectory $curDir -ArgumentList "mvn gwt:codeserver -pl client -am"
```
## 提示
> -DarchetypeCatalog=local参数限制在本地搜寻archetypeGroupId，因此不会把时间浪费在不存在的网络资源搜寻上。

## modular-webapp
构造项目，自定义module名称，比如demo
```
mvn archetype:generate -DarchetypeCatalog=local \
 -DarchetypeGroupId=com.risetek.archetypes \
 -DarchetypeVersion=HEAD-SNAPSHOT \
 -DgroupId=com.risetek \
 -DarchetypeArtifactId=modular-webapp \
 -Dmodule=demo
```

## empty-webapp
构造项目，自定义module名称，比如demo
```
mvn archetype:generate -DarchetypeCatalog=local \
 -DarchetypeGroupId=com.risetek.archetypes \
 -DarchetypeVersion=HEAD-SNAPSHOT \
 -DgroupId=com.risetek \
 -DarchetypeArtifactId=empty-webapp \
 -DgroupId=com.risetek
```
