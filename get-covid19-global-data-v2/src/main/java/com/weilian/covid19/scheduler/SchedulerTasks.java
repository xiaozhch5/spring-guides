package com.weilian.covid19.scheduler;

import com.alibaba.fastjson.JSONObject;
import com.weilian.covid19.baidutranslate.Decode;
import com.weilian.covid19.baidutranslate.TransApi;
import com.weilian.covid19.config.HandleGetRequest;
import com.weilian.covid19.dao.Covid;
import com.weilian.covid19.utils.JsoupHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.In;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import sun.plugin.javascript.navig.Array;
import com.alibaba.fastjson.JSON;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class SchedulerTasks {

    static String ORIGINAL = "https://www.arcgis.com";
    static String REFERER = "https://www.arcgis.com/apps/opsdashboard/index.html";
    static String USERAGENT  = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.162 Safari/537.36";
    static String URL = "https://services9.arcgis.com/N9p5hsImWXAccRNI/arcgis/rest/services/Nc2JKvYFoAEOFCG5JSI6/FeatureServer/2/query?f=json&where=1%3D1&returnGeometry=false&spatialRel=esriSpatialRelIntersects&outFields=*&orderByFields=Confirmed%20desc&resultOffset=0&resultRecordCount=190&cacheHint=true";

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Scheduled(fixedRate = 900000)
    public void downloadData() throws Exception {

        // 获取当前更新时间
        Long currentTime = System.currentTimeMillis();
        Date date1 = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        jdbcTemplate.execute("DROP TABLE IF EXISTS template_date");
        jdbcTemplate.execute("CREATE TABLE template_date(temp_date VARCHAR(512))");
        jdbcTemplate.update("INSERT INTO template_date(temp_date) VALUES(?)", simpleDateFormat.format(date1));

        String APP_ID = "20200406000413180";
        String SECURITY_KEY = "g4tuhCQbJdXQFdMF9Dhv";

        TransApi transApi = new TransApi(APP_ID, SECURITY_KEY);
        Decode decode = new Decode();

        List<Covid> list = new ArrayList<>();


        HandleGetRequest handleGetRequest = new HandleGetRequest();

        JSONObject jsonObject = JSON.parseObject(handleGetRequest.sendGet(URL, ORIGINAL, REFERER, USERAGENT));

        int i = 0;

        for(Object attributes: jsonObject.getJSONArray("features")){
//            i = i + 1;
//            if(i >10){
//                continue;
//            }
            JSONObject jsonObject1 = ((JSONObject) attributes).getJSONObject("attributes");

            Covid covid = new Covid();

            covid.setObjectId(jsonObject1.get("OBJECTID").toString());
            covid.setLongg(jsonObject1.get("Long_")==null? "null":jsonObject1.get("Long_").toString());
            covid.setRecovered(jsonObject1.get("Recovered").toString());

//            System.out.println(transApi.getTransResult(jsonObject1.get("Country_Region").toString(), "auto", "zh"));

            // 调用百度翻译api将国家名称翻译为中文
            String countryRegion =
                    String.valueOf(JSON.parse(transApi.getTransResult(jsonObject1.get("Country_Region").toString(), "auto", "zh")
                            .split(":")[5].split("}")[0]));
            covid.setCountryRegion(countryRegion=="台湾*"?"中国台湾":countryRegion);

            //现存确诊人数 确诊人数-死亡人数-治愈人数
            covid.setActive(String.valueOf(Long.valueOf(jsonObject1.get("Confirmed").toString()) -
                    Long.valueOf(jsonObject1.get("Deaths").toString()) -
                    Long.valueOf(jsonObject1.get("Recovered").toString())));

            // 将时间转换为"yyyy-MM-dd HH:mm:ss"格式
            String time = jsonObject1.get("Last_Update").toString();
            Date date = new Date(Long.valueOf(time));
//            System.out.println(simpleDateFormat.format(date));
            covid.setLastUpdate(simpleDateFormat.format(date));
            covid.setDeaths(jsonObject1.get("Deaths").toString());
            covid.setConfirmed(jsonObject1.get("Confirmed").toString());
            covid.setLat(jsonObject1.get("Lat")==null?"null":jsonObject1.get("Lat").toString());

            System.out.println(covid);
            list.add(covid);
        }

        jdbcTemplate.execute("DROP TABLE IF EXISTS covid");
        jdbcTemplate.execute("CREATE TABLE covid(" +
                "object_id VARCHAR(512), longg VARCHAR(512), recovered VARCHAR(512), " +
                "country_region VARCHAR(512), active VARCHAR(512), last_update VARCHAR(512), " +
                "deaths VARCHAR(512), confirmed VARCHAR(128), lat VARCHAR(512))");

        jdbcTemplate.execute("ALTER TABLE covid CONVERT TO CHARACTER SET utf8mb4");


        jdbcTemplate.batchUpdate("INSERT INTO covid(" +
                        "object_id, longg, recovered, country_region, active, last_update, deaths, confirmed, lat) " +
                        "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)",
                            list.stream().map(name ->name.toString().split("\t")).collect(Collectors.toList()));

        // 修改台湾为中国台湾
        jdbcTemplate.execute("update covid set country_region = \"中国台湾\" where object_id = 166;");

        // 查询全球确诊人数
        jdbcTemplate.execute("DROP TABLE IF EXISTS confirmed_deaths_recovered");
        jdbcTemplate.execute("CREATE TABLE confirmed_deaths_recovered(confirmed VARCHAR(512), active VARCHAR(512), deaths VARCHAR(512), recovered VARCHAR(512))");
        jdbcTemplate.execute("INSERT INTO confirmed_deaths_recovered(confirmed, active, deaths, recovered) SELECT sum(confirmed), sum(active), sum(deaths), sum(recovered) from covid");
    }
}
