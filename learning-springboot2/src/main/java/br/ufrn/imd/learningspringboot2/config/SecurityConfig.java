package br.ufrn.imd.learningspringboot2.config;

import br.ufrn.imd.learningspringboot2.service.UserAnimeDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@EnableWebSecurity
@Slf4j
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserAnimeDetailsService userAnimeDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
//                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                .and()
                .authorizeRequests()
                .antMatchers("/animes/**").hasRole("USER")
                .antMatchers("/animes/admin/**").hasRole("ADMIN")
                .antMatchers("/actuator/**").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .and()
                .httpBasic();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        log.info("Testing encoder password {}", passwordEncoder.encode("academy"));
//
//        auth.inMemoryAuthentication()
//                .withUser("usuario")
//                .password(passwordEncoder.encode("academy"))
//                .roles("USER")
//                .and()
//                .withUser("samuel")
//                .password(passwordEncoder.encode("academy"))
//                .roles("USER","ADMIN");
        auth.userDetailsService(userAnimeDetailsService)
        .passwordEncoder(passwordEncoder);
    }
}
