package com.duan.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class CorConfiguration implements WebMvcConfigurer{
  @Override
  public void addCorsMappings(CorsRegistry registry) {

    registry.addMapping("/**")
      .allowedOrigins("http://localhost:4200", "https://coffee-shop-fpoly.netlify.app")
      .allowedMethods("GET", "POST", "PUT", "DELETE");
  }
}
