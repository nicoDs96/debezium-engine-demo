package it.nicods.debeziumdemo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class DebeziumConnectorConfig {

  @Value("${spring.datasource.url}")
  private String postgresUrl;

  @Value("${debezium.database.hostname}")
  private String postgresHostname;

  @Value("${debezium.database.dbname}")
  private String postgresDBName;

  @Value("${debezium.database.port}")
  private String postgresPort;

  @Value("${spring.datasource.username}")
  private String postgresUsername;

  @Value("${spring.datasource.password}")
  private String postgresPassword;

  @Value("${debezium.schema.include.list}")
  private String databaseSchemaIncludeList;

  @Value("${debezium.table.include.list}")
  private String databaseTableIncludeList;

  @Value("${debezium.connector.name}")
  private String connectorName;

  @Value("${debezium.plugin.name}")
  private String pluginName;
  @Value("${debezium.snapshot.mode}")
  private String snapshotMode;


  @Bean
  public io.debezium.config.Configuration mongodbConnector() {

    Map<String, String> configMap = new HashMap<>();

    //This sets the name of the Debezium connector instance. It’s used for logging and metrics.
    configMap.put("name", connectorName);
    //This specifies the Java class for the connector. Debezium uses this to create the connector instance.
    configMap.put("connector.class", "io.debezium.connector.postgresql.PostgresConnector");
    configMap.put("database.user", postgresUsername);
    configMap.put("database.dbname", postgresDBName);
    configMap.put("database.hostname", postgresHostname);
    configMap.put("database.password", postgresPassword);
    configMap.put("database.port", postgresPort);
    //This sets the plugin to use
    configMap.put("plugin.name", pluginName);
    //This sets the Java class that Debezium uses to store the progress of the connector.
    // In this case, it’s using a JDBC-based store, which means it will store the offset in a relational database.
    configMap.put("offset.storage", "io.debezium.storage.jdbc.offset.JdbcOffsetBackingStore");
    //This is the JDBC URL for the database where Debezium stores the connector offsets (progress).
    configMap.put("offset.storage.jdbc.url", postgresUrl);
    configMap.put("offset.storage.jdbc.user", postgresUsername);
    configMap.put("offset.storage.jdbc.password", postgresPassword);
    // This writes offsets to plain file
    //configMap.put("offset.storage",  "org.apache.kafka.connect.storage.FileOffsetBackingStore");
    //configMap.put("offset.storage.file.filename", "./debezium-offset.dat");
    configMap.put("offset.flush.interval.ms", "2000");
    //This prefix is added to all Kafka topics that this connector writes to.
    configMap.put("topic.prefix", "cdc-demo-connector");
    //This is a comma-separated list of Postgres database names that the connector will monitor for changes.
    //configMap.put("schema.include.list", databaseSchemaIncludeList);
    configMap.put("table.include.list", databaseTableIncludeList);
    //this set the snapshot mode
    configMap.put("snapshot.mode", snapshotMode);
    // this includes/exclude the schema in the message
    configMap.put("converter.schemas.enable", "false");
    //When errors.log.include.messages set to true, then any error messages resulting from failed operations
    // are also written to the log.
    configMap.put("errors.log.include.messages", "true");

    return io.debezium.config.Configuration.from(configMap);
  }
}
