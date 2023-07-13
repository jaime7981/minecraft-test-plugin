mvn clean compile assembly:single -f /mine_plugin/pom.xml
cp /mine_plugin/target/testplugin-1.0-SNAPSHOT-jar-with-dependencies.jar /mine_server/plugins