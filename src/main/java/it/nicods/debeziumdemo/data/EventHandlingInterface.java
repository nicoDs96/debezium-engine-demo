package it.nicods.debeziumdemo.data;

import jakarta.transaction.Transactional;

public interface EventHandlingInterface <T> {

    @Transactional
    void handleEvent(T record);
}
