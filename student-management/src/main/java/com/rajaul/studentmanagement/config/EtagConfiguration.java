package com.rajaul.studentmanagement.config;

import jakarta.servlet.Filter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
/**
 * @author Mohammad Rajaul Islam
 * @version 1.0.0
 * @since 1.0.0
 */
@Configuration
public class EtagConfiguration {
    @Bean
    public Filter shallowETagFilter() {
        return new ShallowEtagHeaderFilter();
    }
}
