package it.nicods.debeziumdemo.data.geo.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.ZonedDateTime;

@Entity
@Table(name = "dummy_table_tracking", schema = "tracking")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DummyTable {
    @Id
    @GeneratedValue
    private Integer Id;
    private String username;
    private String address;
    private String operation;
    private ZonedDateTime ts;
}
