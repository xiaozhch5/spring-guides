# Consuming a Restful Web Service
本节指引你使用spring构建一个应用来使用Restful web服务。

使用spring的`RestTemplate`来获取restful web，`https://gturnquist-quoters.cfapps.io/api/random`提供的springboot quotation。

## 指引
首先通过网页或者curl获取 `https://gturnquist-quoters.cfapps.io/api/random`提供的信息：
````json
{
  "type":"success",
  "value":{
    "id":8,
    "quote":"I don't worry about my code scaling. Boot allows the developer to peel back the layers and customize when it's appropriate while keeping the conventions that just work."
   }
}
````
在使用restful服务之前，你有必要知道从上述api获得到的信息以及它的格式。

首先创建Value类，
com/weilian/carws/consumingrest/Value.java
````java
package com.weilian.carws.consumingrest;

import lombok.Data;

@Data
public class Value {

    private Long id;
    private String quote;
     public Value(Long id, String quote){
         this.id = id;
         this.quote = quote;
     }

    @Override
    public String toString() {
        return "Value{" +
                "id=" + id +
                ", quote='" + quote + '\'' +
                '}';
    }
}
````
再创建Quote类：
com/weilian/carws/consumingrest/Quote.java
````java
package com.weilian.carws.consumingrest;

import lombok.Data;

@Data
public class Quote {

    private String type;
    private Value value;

    public Quote(String type, Value value){
        this.type = type;
        this.value = value;
    }

    @Override
    public String toString() {
        return "Quote{" +
                "type='" + type + '\'' +
                ", value=" + value +
                '}';
    }
}
````
接下来，为了在CarwsApplication.java中加入其他东西来从RestFul api获取数据，你需要：
* 使用logger（或者直接使用输出System.out.println()）来输出api信息。
* 使用`RestTemplate`，`RestTemplate`使用Jackson JSON processing library来处理api数据。
* 在程序运行时，使用`CommandLineRunner`来运行`RestTemplate`，并接受api数据。

代码如下：
````java
package com.weilian.carws;

import com.weilian.carws.consumingrest.Quote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class CarwsApplication {

    private Logger log = LoggerFactory.getLogger(CarwsApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(CarwsApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder){
        return builder.build();
    }

    @Bean
    public CommandLineRunner run(RestTemplate restTemplate) throws Exception{
        return args -> {
            Quote quote = restTemplate.getForObject("https://gturnquist-quoters.cfapps.io/api/random", Quote.class);

            System.out.println(quote.toString());
        };
    }

}
````
## 运行
直接在IDEA中运行CarwsApplication.java，可以看到输出：
````
Quote{type='success', value=Value{id=12, quote='@springboot with @springframework is pure productivity! Who said in #java one has to write double the code than in other langs? #newFavLib'}}
````
