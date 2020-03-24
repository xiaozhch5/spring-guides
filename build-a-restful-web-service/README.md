# Build a Restful Web Service
本节指引你使用spring构建“hello world” Restful web 服务。

## 项目说明
服务从`/greeting`处理`GET`请求，同时，可选择地在查询语句中加入`name`参数。
`GET`请求应该返回`200 OK`的状态码和JSON格式的返回信息，像下面这样子：
````
{
    "id":1,
    "content": "hello world!"
}
````
## 指引
1. 创建一个Greeting类，用于返回数据:(lombok包中的Data注解在代码编译时给bean自动引入get、set方法)

/com/weilian/barws/restservice/Greeting
````java
package com.weilian.barws.restservice;

import lombok.Data;

@Data
public class Greeting {

    private long id;
    private  String content;

    public Greeting(long id, String content){
        this.id = id;
        this.content = content;
    }
}
````
2. 创建GreetingController，用于restful服务：

/com/weilian/barws/controller/GreetingController
````java
package com.weilian.barws.controller;


import com.weilian.barws.restservice.Greeting;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class GreetingController {

    private String template = "Hello, %s!";
    private AtomicLong counter = new AtomicLong();

    @GetMapping("/greeting")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "world") String name){
        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }



}
````
在用spring构建Restful web服务中，HTTP请求是通过controller处理的。

这个组件通过`@RestController`注解识别的。在上述代码`GreetingController.java`中,
`GreetingController`处理来自`/greeting`的GET请求，并返回`Greeting`类的实例。

## GreetingController类解析
This controller is concise and simple, but there is plenty going on under the hood.
 We break it down step by step.

The @GetMapping annotation ensures that HTTP GET requests to `/greeting` 
are mapped to the `greeting()` method.

There are companion annotations for other HTTP verbs 
(e.g. `@PostMapping` for POST). There is also a `@RequestMapping` annotation
 that they all derive from, and can serve as a synonym
  (e.g. `@RequestMapping(method=GET)`).
`@RequestParam` binds the value of the query string parameter `name`
into the `name` parameter of the `greeting()` method. 
If the `name` parameter is absent in the request, the `defaultValue` of `World` is used.

The implementation of the method body creates and returns a new `Greeting` object 
with `id` and `content` attributes based on the next value from the `counter` 
and formats the given `name` by using the greeting `template`.

A key difference between a traditional MVC controller 
and the RESTful web service controller shown earlier is the way 
that the HTTP response body is created. 
Rather than relying on a view technology to perform server-side 
rendering of the greeting data to HTML, 
this RESTful web service controller populates and returns a `Greeting` object. 
The object data will be written directly to the HTTP response as JSON.

This code uses Spring `@RestController` annotation, 
which marks the class as a controller where every method returns 
a domain object instead of a view. It is shorthand for including 
both `@Controller` and `@ResponseBody`.

The `Greeting` object must be converted to JSON. 
Thanks to Spring’s HTTP message converter support, 
you need not do this conversion manually. 
Because `Jackson 2` is on the classpath, 
Spring’s `MappingJackson2HttpMessageConverter` is automatically chosen 
to convert the Greeting instance to JSON.

`@SpringBootApplication` is a convenience annotation 
that adds all of the following:

`@Configuration`: Tags the class as a source of bean definitions for the application context.

`@EnableAutoConfiguration`: Tells Spring Boot to start adding 
beans based on classpath settings, other beans, and various property settings. 
For example, if spring-webmvc is on the classpath, 
this annotation flags the application as a web application 
and activates key behaviors, such as setting up a `DispatcherServlet`.

`@ComponentScan`: Tells Spring to look for other components, 
configurations, and services in the com/example package, 
letting it find the controllers.

The `main()` method uses Spring Boot’s `SpringApplication.run()` method 
to launch an application. 
Did you notice that there was not a single line of XML? 
There is no `web.xml` file, either. 
This web application is 100% pure Java 
and you did not have to deal with configuring any plumbing or infrastructure.\

## 运行
1. 直接在IDEA中运行BarwsApplication.java，可以看到服务开始监听本地8080端口
2. 打开浏览器：

输入localhost:8080/greeting
出现
````json
{
  "id": 1,
  "content": "Hello, world!"
}
````
输入localhost:8080/greeting?name=User
出现
````json
{
  "id": 2,
  "content": "Hello, User!"
}
````