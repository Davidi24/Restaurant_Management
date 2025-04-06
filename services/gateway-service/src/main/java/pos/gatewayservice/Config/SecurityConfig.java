package pos.gatewayservice.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import pos.gatewayservice.Service.CookiesManager;
import pos.gatewayservice.Service.JWTService;

import java.util.List;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private final ReactiveUserDetailsService userDetailsService;
    private final JWTService jwtService;
    private final CookiesManager cookiesManager;
    public static final List<String> PUBLIC_URIS = List.of(
            "/auth/login", "/swagger-ui/**", "/webjars/**", "/v3/**"
    );

    public SecurityConfig(ReactiveUserDetailsService userDetailsService, JWTService jwtService, CookiesManager cookiesManager) {
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
        this.cookiesManager = cookiesManager;
    }



    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity serverHttpSecurity) {
        serverHttpSecurity
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .authorizeExchange(exchange -> exchange
                        .pathMatchers(PUBLIC_URIS.toArray(new String[0])).permitAll()
                        .pathMatchers("/auth/login", "/swagger-ui/**", "/webjars/**", "/v3/api-docs/**").permitAll()
                        .pathMatchers("/admin/**").hasRole("ADMIN")
                        .pathMatchers("/manager/**").hasRole("MANAGER")
                        .anyExchange().authenticated()
                )
                .addFilterBefore(new JWTFilter(userDetailsService, jwtService, cookiesManager), SecurityWebFiltersOrder.AUTHENTICATION)
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
        ;
        return serverHttpSecurity.build();
    }


    @Bean
    public UserDetailsRepositoryReactiveAuthenticationManager authenticationManager(ReactiveUserDetailsService userDetailsService) {
        UserDetailsRepositoryReactiveAuthenticationManager manager = new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
        manager.setPasswordEncoder(passwordEncoder());
        return manager;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
