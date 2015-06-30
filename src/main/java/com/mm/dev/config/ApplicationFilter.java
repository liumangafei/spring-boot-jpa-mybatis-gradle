package com.mm.dev.config;

import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.ArrayList;
import java.util.List;


@Configuration
public class ApplicationFilter {

    @Bean
    public FilterRegistrationBean encodingFilter() {
        CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
        FilterRegistrationBean mappingEncodingFilter = new FilterRegistrationBean(encodingFilter);

        List<String> urlPatterns = new ArrayList<String>();
        urlPatterns.add("/*");
        mappingEncodingFilter.setUrlPatterns(urlPatterns);
        mappingEncodingFilter.setOrder(2);

        return mappingEncodingFilter;
    }

    @Bean
    public FilterRegistrationBean webStatFilter() {

        WebStatFilter webStatFilter = new WebStatFilter();
        FilterRegistrationBean mappingDruid = new FilterRegistrationBean(webStatFilter);

        List<String> urlPatterns = new ArrayList<String>();
        urlPatterns.add("/*");
        mappingDruid.setUrlPatterns(urlPatterns);
        mappingDruid.setOrder(3);

        return mappingDruid;
    }


}
