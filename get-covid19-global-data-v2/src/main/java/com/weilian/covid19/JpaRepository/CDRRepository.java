package com.weilian.covid19.JpaRepository;

import com.weilian.covid19.dao.ConfirmedDeathsRecovered;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CDRRepository extends JpaRepository<ConfirmedDeathsRecovered, String> {
}
