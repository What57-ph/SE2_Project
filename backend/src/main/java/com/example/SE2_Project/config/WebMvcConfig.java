//package com.example.SE2_Project.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.ViewResolver;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//import org.springframework.web.servlet.view.InternalResourceViewResolver;
//@Configuration
//@EnableWebMvc
//public class WebMvcConfig implements WebMvcConfigurer {
//    @Bean
//    public ViewResolver viewResolver(){
//        final InternalResourceViewResolver bean=new InternalResourceViewResolver();
//        bean.setSuffix(".html");
//        bean.setPrefix("/");
//        return bean;
//    }
//    @Override
//    public void configureViewResolvers(ViewResolverRegistry registry){
//        registry.viewResolver(viewResolver());
//    }
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry){
//        registry.addResourceHandler("/css/**").addResourceLocations("/static/css/");
//        registry.addResourceHandler("/js/**").addResourceLocations("/static/js/");
//        registry.addResourceHandler("/img/**").addResourceLocations("/static/img/");
//    }
//
//}
