package com.inha.shift.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class BeanRegister {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
