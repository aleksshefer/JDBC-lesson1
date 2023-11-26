package ru.shefer;

import org.h2.jdbcx.JdbcDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


import javax.sql.DataSource;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;

@Configuration
@ComponentScan(basePackages = "ru.shefer")
public class ApplicationConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/css/");
    }

    @Bean
    public DataSource dataSource(
            @Value("${jdbcUrl}") String jdbcUrl,
            @Value("${userName}") String userName,
            @Value("${password}") String password
    ) {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL(jdbcUrl);
        dataSource.setUser(userName);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean
    CommandLineRunner cmd(DataSource dataSource) {
        return args -> {
            try (InputStream isr = this.getClass().getResourceAsStream("/data.sql")) {

                if (isr != null && isr.available() > 0) {
                    String initialSql = new String(isr.readAllBytes());

                    try (
                            Connection connection = dataSource.getConnection();
                            Statement statement = connection.createStatement();
                    ) {
                        statement.executeUpdate(initialSql);
                    }
                }
            }
        };
    }
}
