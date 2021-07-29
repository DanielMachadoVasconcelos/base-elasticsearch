package br.com.ead.personsearch.security;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Log4j2
@Configuration
@EnableWebSecurity
public class CustomWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth, PasswordEncoder passwordEncoder) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("username")
                .password(passwordEncoder.encode("password"))
                .roles("USER");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new StandardPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        BasicAuthenticationEntryPoint basicAuthEntryPoint = new BasicAuthenticationEntryPoint();
        basicAuthEntryPoint.setRealmName("person-search-realm");

        http.cors().disable()
                .csrf().disable()
                .exceptionHandling().authenticationEntryPoint(basicAuthEntryPoint)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.TRACE).denyAll()
                .antMatchers("/actuator/**").permitAll()
                .antMatchers("/ping").permitAll()
                .antMatchers("/metrics").permitAll()
                .antMatchers("/healthcheck").permitAll()
                .antMatchers("/health").permitAll()
                .antMatchers(HttpMethod.POST, "/persons").authenticated()
                .antMatchers(HttpMethod.GET, "/persons").authenticated()
                .antMatchers(HttpMethod.DELETE, "/persons/**").denyAll()
                .anyRequest().authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(STATELESS)
                .and()
                .httpBasic();
    }
}