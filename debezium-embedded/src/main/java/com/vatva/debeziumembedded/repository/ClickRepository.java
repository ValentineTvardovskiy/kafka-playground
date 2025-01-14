package com.vatva.debeziumembedded.repository;

import com.vatva.debeziumembedded.domain.Click;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClickRepository extends JpaRepository<Click, Integer> {
}
