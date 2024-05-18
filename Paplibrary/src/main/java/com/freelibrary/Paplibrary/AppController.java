package com.freelibrary.Paplibrary;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
@EnableWebSecurity
public class AppController implements WebMvcConfigurer{
 public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/index").setViewName("user/index");
        registry.addViewController("/").setViewName("book/home_page");
       // registry.addViewController("/").setViewName("user/index");
         registry.addViewController("/book").setViewName("book/home_page");
        registry.addViewController("/main").setViewName("user/main");
        registry.addViewController("/admin").setViewName("admin/main_admin");
    }
}
