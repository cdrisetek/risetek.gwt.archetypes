# dagger-guice-rf-activities
> 由于GWTP使用的是GIN，因此没有在dagger上下功夫。

> (A combination of dagger, Guice and GWT requestFactory, activities)

## 构造dagger-guice-rf-activities项目
> 需要输入自定义module名称，比如risetek.demo
```
mvn archetype:generate -DarchetypeCatalog=local -DarchetypeGroupId=com.risetek.archetypes \
  -DarchetypeVersion=HEAD-SNAPSHOT -DgroupId=com.risetek -DarchetypeArtifactId=dagger-guice-rf-activities
```
## 增添新的requestFactory服务
### server
* 实现Response对应的数据结构（Entity）
* 实现返回Response的服务

### shared
* ${module}Factory中增加对应的服务上下文: RequestContext.
* ResponseProxy对应于服务端（server）代码中的Entity
* 实现RequestContext的extends，里面包含服务名，并返回Request<T>，<T>与ResponseProxy相关

### 几个特殊文件的作用
* (client) AuthAwareRequestTransport.java
* (server) AjaxAuthenticationFilter.java
* User.java CurrentUser.java & ServerUser.java

## 增添新的Place
* 实现一个Place的扩展
* ${module}PlaceHistortMapper需要更新，在@WithTokenizers中引入新增的Place对应的class
* MainActivityMapper需要增加处理该Place活动的代码
