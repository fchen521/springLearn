##### 使用方法
```text
打成jar包

mvn package

执行命令
java -jar dbmonster-1.0-SNAPSHOT.jar -c xxx/dbmonster.properties -s xxx/dbmonster/dbmonster-schema.xml

dbmonster.properties 是参数配置文件
dbmonster-schema.xml 是表文件，需要生成数据的表在这里面添加
```