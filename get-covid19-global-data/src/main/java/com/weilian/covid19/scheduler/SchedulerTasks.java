package com.weilian.covid19.scheduler;

import com.weilian.covid19.dao.Covid;
import com.weilian.covid19.utils.JsoupHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import sun.plugin.javascript.navig.Array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class SchedulerTasks {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Scheduled(fixedRate = 500000)
    public void downloadData() throws Exception {

        List<Covid> list = new ArrayList<>();

        jdbcTemplate.execute("DROP TABLE IF EXISTS covid");
        jdbcTemplate.execute("CREATE TABLE covid(" +
                "country VARCHAR(255), confirmed VARCHAR(255), deaths VARCHAR(255), case_fatality VARCHAR(255), deaths100k VARCHAR(255))");

        Map<String, String> data = null;

        for (int i = 1; i < 101; i++) {
            Covid covid = new Covid();
            for (int j = 1; j < 6; j++) {
                data = JsoupHelper.fecthByMap("https://coronavirus.jhu.edu/data/mortality",
                        "//*[@id=\"root\"]/div/div/div/div[2]/div/div[3]/table/tbody/tr[" + i + "]/td[" + j + "]", i);

                if (j == 1) {
                    covid.setCountry(data.get(Integer.toString(i)));
                }
                if (j == 2) {
                    covid.setConfirmed(data.get(Integer.toString(i)));
                }
                if (j == 3) {
                    covid.setDeaths(data.get(Integer.toString(i)));
                }
                if (j == 4) {
                    covid.setCaseFatality(data.get(Integer.toString(i)));
                }
                if (j == 5) {
                    covid.setDeaths100k(data.get(Integer.toString(i)));
                }
            }
            System.out.println(covid.toString());
            list.add(covid);
        }
        jdbcTemplate.batchUpdate("INSERT INTO covid(" +
                        "country, confirmed, deaths, case_fatality, deaths100k) VALUES(?, ?, ?, ?, ?)",
                            list.stream().map(name ->name.toString().split("\t")).collect(Collectors.toList()));

    }
}
