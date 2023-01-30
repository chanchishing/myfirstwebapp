package com.in28minutes.springboot.myfirstwebapp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.function.Function;

@Configuration
public class SpringSecurityConfiguration {

    @Bean
    public InMemoryUserDetailsManager createUserDetailsManager(){
        Function<String, String> passwordEncoder=input->passwordEncoder().encode(input);

        UserDetails userDetails1 = createNewUser("in28minutes", "dummy");
        UserDetails userDetails2 = createNewUser("ranga", "dummy");

        return new InMemoryUserDetailsManager(userDetails1,userDetails2);
    }

    private UserDetails createNewUser(String username, String password) {
        Function<String, String> passwordEncoder=input->passwordEncoder().encode(input);

        UserDetails userDetails=User.builder()
                .passwordEncoder(passwordEncoder)
                .username(username)
                .password(password)
                .roles("USER","ADMIN")
                .build();
        return userDetails;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        //All URLs are protected
        httpSecurity.authorizeHttpRequests(
          auth -> auth.anyRequest().authenticated()
        );

        //A login form is show for unauthorized reuests
        httpSecurity.formLogin(Customizer.withDefaults());

        //for h2-console, needed to
        //(a) disable CSRF
        //(b) all Frame option
        httpSecurity.csrf().disable();
        httpSecurity.headers().frameOptions().disable();

        return httpSecurity.build();
    }
}
