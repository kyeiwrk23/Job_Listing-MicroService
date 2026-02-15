package com.example.gateway.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;


import java.util.*;


@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);



    @Autowired
    private CustomerConvertor customerConvertor;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchange -> exchange
                .pathMatchers("/actuator/**").permitAll()
                .pathMatchers("/swagger-ui/index.html", "/swagger-resources/configuration/ui").permitAll()
                .pathMatchers(HttpMethod.POST,"/api/users").permitAll()
                .pathMatchers(HttpMethod.GET,"/api/users").hasRole("ADMIN")
                .pathMatchers(HttpMethod.GET,"/api/users/*").hasAnyRole("ADMIN", "USER","EMPLOYER")
                .pathMatchers(HttpMethod.DELETE,"/api/users/*").hasRole("ADMIN")
                .pathMatchers(HttpMethod.PUT,"/api/users/*").hasAnyRole("ADMIN", "USER","EMPLOYER")
                .pathMatchers(HttpMethod.PUT,"/api/users/*/suspend","/api/users/*/activate").hasRole("ADMIN")
                .pathMatchers(HttpMethod.PUT,"/api/users/*/reset-password").hasAnyRole("ADMIN","EMPLOYER")
                .pathMatchers(HttpMethod.GET,"/api/reviews","/api/reviews/*").hasAnyRole("ADMIN", "USER","EMPLOYER")
                .pathMatchers(HttpMethod.POST,"/api/reviews").hasRole("USER")
                .pathMatchers(HttpMethod.DELETE,"/api/reviews/*").hasAnyRole("ADMIN","EMPLOYER")
                .pathMatchers(HttpMethod.PUT,"/api/reviews/*").hasRole("USER")
                .pathMatchers(HttpMethod.POST,"/api/application").hasRole("USER")
                .pathMatchers(HttpMethod.GET,"/api/application/*").hasAnyRole("ADMIN","USER","EMPLOYER")
                .pathMatchers(HttpMethod.GET,"/api/application").hasRole("EMPLOYER")
                .pathMatchers(HttpMethod.GET,"/api/application/jobseeker").hasRole("USER")
                .pathMatchers(HttpMethod.PUT,"/api/application/*/status").hasAnyRole("ADMIN","EMPLOYER")
                .pathMatchers(HttpMethod.PUT,"/api/application/*").hasRole("USER")
                .pathMatchers(HttpMethod.GET,"/api/application/stats").hasAnyRole("ADMIN","EMPLOYER")
                .pathMatchers(HttpMethod.DELETE,"/api/application/*").hasRole("ADMIN")
                .pathMatchers(HttpMethod.POST,"/api/companies").hasRole("EMPLOYER")
                .pathMatchers(HttpMethod.GET,"/api/companies","/api/companies/*").hasAnyRole("ADMIN","EMPLOYER","USER")
                .pathMatchers(HttpMethod.DELETE,"/api/companies/*").hasAnyRole("ADMIN","EMPLOYER")
                .pathMatchers(HttpMethod.PUT,"/api/companies/*").hasAnyRole("ADMIN","EMPLOYER")
                .pathMatchers(HttpMethod.POST,"/api/jobs").hasAnyRole("EMPLOYER","ADMIN")
                .pathMatchers(HttpMethod.GET,
                        "/api/jobs","/api/jobs/*","/api/jobs/*/joblist",
                        "/api/jobs/keywords").hasAnyRole("ADMIN","EMPLOYER","USER")
                .pathMatchers(HttpMethod.PUT,"/api/jobs/*").hasAnyRole("ADMIN","EMPLOYER")
                .pathMatchers(HttpMethod.DELETE,"/api/jobs/*").hasRole("ADMIN")
                .anyExchange().authenticated())
                .oauth2ResourceServer(oauth-> oauth
                        .jwt((jwt)->jwt.jwtAuthenticationConverter(jwtAuthenticationConverter(customerConvertor))));

        return http.build();

    }


    @Bean
    public Converter<Jwt, Mono<AbstractAuthenticationToken>> jwtAuthenticationConverter(CustomerConvertor convertor) {

        JwtGrantedAuthoritiesConverter scopeConvertor = new JwtGrantedAuthoritiesConverter();
        return jwt ->{
            log.info("jwtAuthenticationConverter3456");
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            authorities.addAll(scopeConvertor.convert(jwt));
            authorities.addAll(convertor.convert(jwt));

            String principalName = jwt.getClaimAsString("preferred_username");

            return Mono.just(
                    new JwtAuthenticationToken(jwt,authorities,principalName)
            );
        };
    }




}
