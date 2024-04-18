package it.nicods.debeziumdemo.data;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Objects;

public abstract class TrackingService<T> implements EventHandlingInterface<T>{


    public T deserialize(String sourceRecordValue, Class<T> clazz) throws IOException {
        if (Objects.nonNull(sourceRecordValue)) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return mapper.readValue(sourceRecordValue, clazz);
        }
        return null;
    }

}
