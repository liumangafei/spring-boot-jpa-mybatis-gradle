package com.mm.dev;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by Lipengfei on 2015/6/24.
 */
@SpringBootApplication
@PropertySource(value = {"classpath:/application.properties", "classpath:/datasource.properties"})
public class DevApplication {

    public static void main(String[] args) {
        SpringApplication.run(DevApplication.class, args);
    }

}
