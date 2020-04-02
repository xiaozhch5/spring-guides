# Securing a web application
本节指引你使用Spring Security来保护一个简单的web应用。
##指引
首先创建一个不安全的web应用

创建一个home.html模板
```html
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:th="https://www.thymeleaf.org" xmlns:sec="https://www.thymeleaf.org/thymeleaf-extras-springsecurity3">
    <head>
        <title>Spring Security Example</title>
    </head>
    <body>
        <h1>Welcome!</h1>

        <p>Click <a th:href="@{/hello}">here</a> to see a greeting.</p>
    </body>
</html>
```
创建一个hello.html模板
```html
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:th="https://www.thymeleaf.org"
      xmlns:sec="https://www.thymeleaf.org/thymeleaf-extras-springsecurity3">
    <head>
        <title>Hello World!</title>
    </head>
    <body>
        <h1>Hello world!</h1>
    </body>
</html>
```
由于本应用是基于Spring MVC的，所以需要配置Spring MVC以及view controllers来展示模板，新建一个MvcConfig.java
```java
package com.weilian.sawa.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    public void addViewControllers(ViewControllerRegistry viewControllerRegistry){
        viewControllerRegistry.addViewController("/home").setViewName("home");
        viewControllerRegistry.addViewController("/").setViewName("home");
        viewControllerRegistry.addViewController("/hello").setViewName("hello");
        viewControllerRegistry.addViewController("/login").setViewName("login");
    }

}
```
The addViewControllers() method (which overrides the method of the same name in WebMvcConfigurer) adds four view controllers. Two of the view controllers reference the view whose name is home (defined in home.html), and another references the view named hello (defined in hello.html). The fourth view controller references another view named login. You will create that view in the next section.

At this point, you could jump ahead to “Run the Application” and run the application without having to log in to anything.

至此，一个不安全的web应用建立好了。

## 创建一个安全的应用
创建安全配置
```java
package com.weilian.sawa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.authorizeRequests()
                .antMatchers("/", "home").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .logout()
                .permitAll();
    }

    @Override
    @Bean
    public UserDetailsService userDetailsService(){
        UserDetails userDetails = User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(userDetails);
    }

}
```
The `WebSecurityConfig` class is annotated with `@EnableWebSecurity` 
to enable Spring Security’s web security support and provide the Spring MVC integration. 
It also extends `WebSecurityConfigurerAdapter` and overrides a couple of its methods to set some specifics of the web security configuration.

The `configure(HttpSecurity)` method defines which URL paths should be secured and which should not.
 Specifically, the `/` and `/home` paths are configured to not require any authentication. All other paths must be authenticated.

When a user successfully logs in, 
they are redirected to the previously requested page that required authentication. 
There is a custom `/login` page (which is specified by `loginPage()`), and everyone is allowed to view it.

The `userDetailsService()` method sets up an in-memory user store with a single user. 
That user is given a user name of `user`, a password of `password`, and a role of `USER`.

创建安全认证页面:login.html
```html
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:th="https://www.thymeleaf.org"
      xmlns:sec="https://www.thymeleaf.org/thymeleaf-extras-springsecurity3">
    <head>
        <title>Spring Security Example </title>
    </head>
    <body>
        <div th:if="${param.error}">
            Invalid username and password.
        </div>
        <div th:if="${param.logout}">
            You have been logged out.
        </div>
        <form th:action="@{/login}" method="post">
            <div><label> User Name : <input type="text" name="username"/> </label></div>
            <div><label> Password: <input type="password" name="password"/> </label></div>
            <div><input type="submit" value="Sign In"/></div>
        </form>
    </body>
</html>
```
This Thymeleaf template presents a form that captures a 
username and password and posts them to `/login`. 
As configured, Spring Security provides a filter that intercepts that request and authenticates the user. If the user fails to authenticate, the page is redirected to `/login?error`, 
and your page displays the appropriate error message. 
Upon successfully signing out, your application is sent to `/login?logout`, and your page displays the appropriate success message.

更新hello.html文件
```html
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:th="https://www.thymeleaf.org"
      xmlns:sec="https://www.thymeleaf.org/thymeleaf-extras-springsecurity3">
    <head>
        <title>Hello World!</title>
    </head>
    <body>
        <h1 th:inline="text">Hello [[${#httpServletRequest.remoteUser}]]!</h1>
        <form th:action="@{/logout}" method="post">
            <input type="submit" value="Sign Out"/>
        </form>
    </body>
</html>
```
We display the username by using Spring Security’s 
integration with `HttpServletRequest#getRemoteUser()`. 
The “Sign Out” form submits a POST to `/logout`. 
Upon successfully logging out, it redirects the user to `/login?logout`.

## 程序与逆行
直接运行SawaApplication.java文件