package com.example.dividends.persist.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DividendRepository extends JpaRepository<DividendEntity, Long> {
}
