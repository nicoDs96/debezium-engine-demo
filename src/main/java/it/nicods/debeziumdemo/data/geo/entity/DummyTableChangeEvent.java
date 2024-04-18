package it.nicods.debeziumdemo.data.geo.entity;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class DummyTableChangeEvent {
    private DummyTable before;
    private DummyTable after;
    private String op;
    private Long ts_ms;
}
