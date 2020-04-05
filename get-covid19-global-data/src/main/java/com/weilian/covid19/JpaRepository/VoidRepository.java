package com.weilian.covid19.JpaRepository;

import com.weilian.covid19.dao.Covid;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoidRepository extends JpaRepository<Covid, Long> {
}
