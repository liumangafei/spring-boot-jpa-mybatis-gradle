package com.mm.dev.config;

import com.alibaba.druid.support.http.StatViewServlet;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Servlet;

/**
 * Created by Administrator on 2015/6/29.
 */
@Configuration
public class ApplicationServlet {

    @Bean
    public ServletRegistrationBean mappingStatViewServlet(){

        StatViewServlet statViewServlet = new StatViewServlet();
        ServletRegistrationBean mappingStatViewServlet = new ServletRegistrationBean(statViewServlet,"/druid/*");

        return  mappingStatViewServlet;
    }



}
