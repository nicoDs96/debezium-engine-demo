package it.nicods.debeziumdemo.listener;

import io.debezium.config.Configuration;
import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.format.Json;
import it.nicods.debeziumdemo.data.geo.entity.DummyTableChangeEvent;
import it.nicods.debeziumdemo.data.geo.entity.DummyTableRepository;
import it.nicods.debeziumdemo.data.geo.entity.DummyTableService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Slf4j
@Service
public class DebeziumSourceEventListener {

    //This will be used to run the engine asynchronously
    private final Executor executor;

    //DebeziumEngine serves as an easy-to-use wrapper around any Debezium connector
    private final DebeziumEngine<ChangeEvent<String, String>> debeziumEngine;

    @Autowired
    private DummyTableRepository dummyTableRepository;


    public DebeziumSourceEventListener(
            Configuration postgresConnector) {
        // Create a new single-threaded executor.
        this.executor = Executors.newSingleThreadExecutor();

        // Create a new DebeziumEngine instance.
        //TODO: understand Configuration origin
        this.debeziumEngine =
                DebeziumEngine.create(Json.class)
                        .using(postgresConnector.asProperties())
                        .notifying(this::handleChangeEvent)
                        .build();
    }

    private boolean handleChangeEvent(ChangeEvent<String, String> record) {
        log.debug("JSON Record {}", record);
        log.debug("JSON Key {}", record.key());
        log.debug("JSON Value {}", record.value());
        log.debug("JSON Headers {}", record.headers());
        log.debug("JSON Destination {}", record.destination());

        String sourceRecordValue = record.value();
        DummyTableService dummyTableService = new DummyTableService(dummyTableRepository);

        try{
            switch (record.destination()){
                case "cdc-demo-connector.public.dummy_table":
                    DummyTableChangeEvent dummyTableChangeEvent = dummyTableService.deserialize(sourceRecordValue, DummyTableChangeEvent.class);
                    log.debug("JSON DESER {}", dummyTableChangeEvent);
                    dummyTableService.handleEvent(dummyTableChangeEvent);
                    return true;
                default:
                    log.error("Ignoring unknown destination {}", record.destination());
                    break;
            }

        }catch (IOException e){
            log.error("Error deserializing source record value", e);
            return false;
        }
        return false;
    }

    @PostConstruct
    private void start() {
        this.executor.execute(debeziumEngine);
    }

    @PreDestroy
    private void stop() throws IOException {
        if (this.debeziumEngine != null) {
            this.debeziumEngine.close();
        }
    }
}
