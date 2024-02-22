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
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.util.matcher.NegatedServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

@Configuration
@EnableReactiveMethodSecurity
@EnableWebFluxSecurity
public class WebSecurityConfig  {

    @Value("${jwt.secret}")
    private String secret;

    private final String[] publicRoutes = {"/api/v1/auth/register", "/api/v1/auth/login"};

//    @Bean
//    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) throws Exception {
//      return  http.csrf(csrf -> csrf.disable())
//              .securityMatcher(ServerWebExchangeMatchers.pathMatchers("/api/v1/users"))
//
//              .build();
//    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http, AuthenticationManager authenticationManager) throws Exception {


            return     http.csrf(csrf -> csrf.disable())
//                    .securityMatcher(ServerWebExchangeMatchers.pathMatchers("/api/v1/**"))
                    .authorizeExchange(authorize -> authorize
                        .pathMatchers("/api/v1/auth/register","/api/v1/auth/login","/api/v1/files")
                            .permitAll()
                                    .pathMatchers("/api/v1/users").hasAnyAuthority("ADMIN")
                        )
                    .addFilterAfter(bearerAuthenticationFilter(authenticationManager), SecurityWebFiltersOrder.AUTHENTICATION)
                    .build();

    }

//    @Bean
//    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
//
//
//        return     http.csrf(csrf -> csrf.disable())
//                    .securityMatcher(ServerWebExchangeMatchers.pathMatchers("/api/v1/**")).authorizeExchange(s -> s.)
//
//                .build();
//
//    }





    private AuthenticationWebFilter bearerAuthenticationFilter(ReactiveAuthenticationManager authenticationManager) {
        AuthenticationWebFilter bearerAuthenticationFilter = new AuthenticationWebFilter(authenticationManager);
        bearerAuthenticationFilter.setServerAuthenticationConverter(new BearerTokenServerAuthenticationConverter(new JwtHandler(secret)));
        bearerAuthenticationFilter.setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers("/**"));

        return bearerAuthenticationFilter;
    }
}

