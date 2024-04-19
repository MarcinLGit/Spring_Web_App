package com.freelibrary.Paplibrary;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class AppController implements WebMvcConfigurer{
 public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/index").setViewName("user/index");
        registry.addViewController("/home").setViewName("user/home_page");
       // registry.addViewController("/").setViewName("user/index");
         registry.addViewController("/user").setViewName("user/home_page");
        registry.addViewController("/main").setViewName("user/main");
        registry.addViewController("/admin").setViewName("admin/main_admin");
    }
}
