mvn clean compile assembly:single -f /mine_plugin/pom.xml
cp /mine_plugin/target/testplugin-1.0-SNAPSHOT-jar-with-dependencies.jar /mine_server/plugins
cp /mine_plugin/target/classes/xyz/developmentcl/commands/ItemsCost.json /mine_server/plugins