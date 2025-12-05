package com.gustavosdaniel.ecommerce_api.config;

import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final SecurityFilter securityFilter;

    public SecurityConfig(SecurityFilter securityFilter) {
        this.securityFilter = securityFilter;
    }

    public static final String[] PUBLIC_URLS = {

            "/api/v1/auth/**",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-ui.html",
            "/swagger-resources/**",
            "/webjars/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configure(http))
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
                                .requestMatchers(PUBLIC_URLS).permitAll()

                                //User
                                .requestMatchers(HttpMethod.GET, "/api/v1/users/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/api/v1/users/userUpdate/**")
                                .hasAnyRole("ADMIN", "CUSTOMER")
                                .requestMatchers(HttpMethod.PUT,"/api/v1/users/cpf/**")
                                .hasAnyRole("ADMIN", "CUSTOMER")
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/users/**")
                                .hasAnyRole("ADMIN", "CUSTOMER")

                                //Category
                                .requestMatchers(HttpMethod.POST, "/api/v1/categories/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/v1/categories/**")
                                .hasAnyRole("ADMIN", "CUSTOMER")
                                .requestMatchers(HttpMethod.PUT, "/api/v1/categories/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/categories/**").hasRole("ADMIN")

                                //Product
                                .requestMatchers(HttpMethod.POST, "/api/v1/products/**")
                                .hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/v1/products/allProducts")
                                .hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/v1/products/allProductsAtivos")
                                .hasAnyRole("ADMIN", "CUSTOMER")
                                .requestMatchers(HttpMethod.GET, "/api/v1/products/inactive")
                                .hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/v1/products/categories/*/products")
                                .hasAnyRole("ADMIN", "CUSTOMER")
                                .requestMatchers(HttpMethod.GET, "/api/v1/products/searchProductName")
                                .hasAnyRole("ADMIN", "CUSTOMER")
                                .requestMatchers(HttpMethod.GET, "/api/v1/products/*")
                                .hasAnyRole("ADMIN", "CUSTOMER")
                                .requestMatchers(HttpMethod.PUT, "/api/v1/products/*")
                                .hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/api/v1/products/*/stock")
                                .hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PATCH, "/api/v1/products/*/activate")
                                .hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PATCH, "/api/v1/products/*/desative")
                                .hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/products/*")
                                .hasRole("ADMIN")


                                //TODO os outros endpoiunts

                                .anyRequest().authenticated())
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();

    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration
    ) throws Exception{

        return authenticationConfiguration.getAuthenticationManager();

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
