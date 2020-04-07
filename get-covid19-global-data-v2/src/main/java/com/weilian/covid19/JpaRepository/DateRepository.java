package com.weilian.covid19.JpaRepository;

import com.weilian.covid19.dao.TemplateDate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DateRepository extends JpaRepository<TemplateDate, Long> {
}
