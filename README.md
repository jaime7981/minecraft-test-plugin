# Minecraft Plugin

Plugin for buying items with levels, manage users and factions

## Compilation

Compile dependencies into jar

Windows:
 - `& "C:\Program Files\apache-maven-3.9.0\bin\mvn.cmd" clean compile assembly:single -f "c:\Users\jaime\OneDrive\Documents\MinePlugins\TestPlugin\testplugin\pom.xml"`
Linux:
 - `mvn clean compile assembly:single -f /mine_plugin/pom.xml`

Copy compiled jar into plugins folder

Windows:
 - `COPY C:\Users\jaime\OneDrive\Documents\MinePlugins\TestPlugin\testplugin\target\testplugin-1.0-SNAPSHOT.jar .\plugins`
Linux:
 - `cp /mine_plugin/target/testplugin-1.0-SNAPSHOT-jar-with-dependencies.jar /mine_server/plugins`

## Server

url: `developmentcl.xyz`
