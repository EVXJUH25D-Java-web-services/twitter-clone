package se.deved.twitter_clone.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import se.deved.twitter_clone.services.IUserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            IUserService userService,
            JwtService jwtService,
            OAuth2SuccessHandler oauth2SuccessHandler
    ) {
        http.csrf(AbstractHttpConfigurer::disable)
                .userDetailsService(userService)
                .oauth2Login(oauth ->
                        oauth.successHandler(oauth2SuccessHandler)
                )
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/user/register").permitAll()
                            .requestMatchers("/user/login").permitAll()
                            .requestMatchers(HttpMethod.GET, "/post/all").permitAll()
                            .requestMatchers(HttpMethod.GET, "/post/*").permitAll()
                            .anyRequest().authenticated();
                })
                .addFilterAfter(new CustomAuthenticationFilter(userService, jwtService), OAuth2LoginAuthenticationFilter.class);

        return http.build();
    }
}
