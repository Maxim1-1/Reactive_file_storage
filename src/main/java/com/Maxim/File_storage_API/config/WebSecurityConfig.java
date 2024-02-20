package com.Maxim.File_storage_API.config;





import com.Maxim.File_storage_API.security.BearerTokenServerAuthenticationConverter;
import com.Maxim.File_storage_API.security.JwtHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;



@Configuration
@EnableWebSecurity
@EnableReactiveMethodSecurity
public class WebSecurityConfig  {

    @Value("${jwt.secret}")
    private String secret;

    private final String [] publicRoutes = {"/api/v1/auth/register", "/api/v1/auth/login"};


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
      return   http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers("/api/v1/auth/login").fullyAuthenticated() ).build();



    }

    private AuthenticationWebFilter bearerAuthenticationFilter(ReactiveAuthenticationManager authenticationManager) {
        AuthenticationWebFilter bearerAuthenticationFilter = new AuthenticationWebFilter(authenticationManager);
        bearerAuthenticationFilter.setServerAuthenticationConverter(new BearerTokenServerAuthenticationConverter(new JwtHandler(secret)));
        bearerAuthenticationFilter.setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers("/**"));

        return bearerAuthenticationFilter;
    }
}
