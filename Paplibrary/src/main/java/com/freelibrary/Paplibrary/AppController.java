package com.freelibrary.Paplibrary;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
@EnableWebSecurity
public class AppController implements WebMvcConfigurer{
 public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/user/").setViewName("user/home_page");
        registry.addViewController("/").setViewName("home_page");
        registry.addViewController("/book").setViewName("book/home_page");
        registry.addViewController("/admin").setViewName("admin/main_admin");
    }
}
