# empty-webapp
> GIN based
> 构造项目，自定义module名称，比如demo
```
mvn archetype:generate -DarchetypeCatalog=local \
 -DarchetypeGroupId=com.risetek.archetypes \
 -DarchetypeVersion=HEAD-SNAPSHOT \
 -DgroupId=com.risetek \
 -DarchetypeArtifactId=empty-webapp \
 -DgroupId=com.risetek
```

```
mvn -q jetty:run -pl server -am -Penv-dev
mvn gwt:codeserver -pl client -am
```