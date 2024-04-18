package it.nicods.debeziumdemo.data.geo.entity;

import it.nicods.debeziumdemo.data.TrackingService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;


@Slf4j
public class DummyTableService extends TrackingService<DummyTableChangeEvent> {
    public DummyTableRepository repository;

    public DummyTableService(DummyTableRepository repository){
        this.repository =repository;
    }

    @Override
    @Transactional
    public void handleEvent(DummyTableChangeEvent record) {
        if(record!=null){
            DummyTable bl=null;
            DummyTable after = record.getAfter();
            DummyTable before = record.getBefore();

            if(after!=null) { //in case of delete after is null and before is valued
                bl = after;
            }
            else if (before!=null){ //in case of delete after is null and before is valued
                bl = before;
            }
            if(bl!= null){
                ZonedDateTime zdt = ZonedDateTime.ofInstant(Instant.ofEpochMilli(record.getTs_ms()), ZoneId.systemDefault());
                bl.setTs(zdt);
                bl.setOperation(record.getOp());
                log.info("Saving {}", record);
                repository.save(bl);
            }else {
                log.error("Before and After are both null {}", record);
            }
        }else{
            log.debug("Change record is null");
        }
    }
}
