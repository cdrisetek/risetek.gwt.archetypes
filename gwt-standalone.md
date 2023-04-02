# GWT Standalone
> GWT Standalone archetype 构建最简单的 GWT 代码结构，仅仅包含Client支持的代码，适合开发无服务端的web app，也适合用来验证UI的技术技巧。  

> 构造项目参看以下命令，请自定义module名称

```
mvn archetype:generate -DarchetypeCatalog=local \
 -DarchetypeGroupId=com.risetek.archetypes \
 -DarchetypeVersion=HEAD-SNAPSHOT \
 -DarchetypeArtifactId=gwt-standalone \
 -DgroupId=com.risetek
```

> 调试命令：mvn gwt:devmode
