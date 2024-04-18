package it.nicods.debeziumdemo.data.geo.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DummyTableRepository extends JpaRepository<DummyTable, Integer> {

}
