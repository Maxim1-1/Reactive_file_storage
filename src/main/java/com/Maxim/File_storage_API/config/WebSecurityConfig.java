package com.Maxim.File_storage_API.config;

import com.Maxim.File_storage_API.security.AuthenticationManager;
import com.Maxim.File_storage_API.security.BearerTokenServerAuthenticationConverter;
import com.Maxim.File_storage_API.security.JwtHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;


@Configuration
@EnableReactiveMethodSecurity
@EnableWebFluxSecurity
public class WebSecurityConfig {

    @Value("${jwt.secret}")
    private String secret;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http, AuthenticationManager authenticationManager)  {
        return http.csrf(csrf -> csrf.disable())
                .authorizeExchange(authorize -> authorize
                                .pathMatchers(HttpMethod.GET, "/api/v1/users/*").hasAnyAuthority("MODERATOR","ADMIN")
                                .pathMatchers(HttpMethod.GET, "/api/v1/users").hasAnyAuthority("MODERATOR","ADMIN")
                                .pathMatchers(HttpMethod.POST, "/api/v1/users").hasAnyAuthority("ADMIN")
                                .pathMatchers(HttpMethod.PUT, "/api/v1/users/*").hasAnyAuthority("ADMIN")
                                .pathMatchers(HttpMethod.DELETE, "/api/v1/users/*").hasAnyAuthority("ADMIN")

                                .pathMatchers(HttpMethod.GET, "/api/v1/events/*").hasAnyAuthority("MODERATOR","ADMIN")
                                .pathMatchers(HttpMethod.GET, "/api/v1/events").hasAnyAuthority("MODERATOR","ADMIN")
                                .pathMatchers(HttpMethod.POST, "/api/v1/events").hasAnyAuthority("MODERATOR","ADMIN")
                                .pathMatchers(HttpMethod.PUT, "/api/v1/events/*").hasAnyAuthority("MODERATOR","ADMIN")
                                .pathMatchers(HttpMethod.DELETE, "/api/v1/events/*").hasAnyAuthority("MODERATOR","ADMIN")

                                .pathMatchers(HttpMethod.GET, "/api/v1/files/*").hasAnyAuthority("MODERATOR","ADMIN")
                                .pathMatchers(HttpMethod.GET, "/api/v1/files").hasAnyAuthority("MODERATOR","ADMIN")
                                .pathMatchers(HttpMethod.POST, "/api/v1/files").hasAnyAuthority("MODERATOR","ADMIN")
                                .pathMatchers(HttpMethod.PUT, "/api/v1/files/*").hasAnyAuthority("MODERATOR","ADMIN")
                                .pathMatchers(HttpMethod.DELETE, "/api/v1/files/*").hasAnyAuthority("MODERATOR","ADMIN")

                                .pathMatchers("/api/v1/auth/register", "/api/v1/auth/login").permitAll()
                                .pathMatchers("/api/**").hasAnyAuthority("ADMIN")
                )
                .addFilterAfter(bearerAuthenticationFilter(authenticationManager), SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }


    private AuthenticationWebFilter bearerAuthenticationFilter(AuthenticationManager authenticationManager) {
        AuthenticationWebFilter bearerAuthenticationFilter = new AuthenticationWebFilter(authenticationManager);
        bearerAuthenticationFilter.setServerAuthenticationConverter(new BearerTokenServerAuthenticationConverter(new JwtHandler(secret)));
        bearerAuthenticationFilter.setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers("/**"));
        return bearerAuthenticationFilter;
    }
}

