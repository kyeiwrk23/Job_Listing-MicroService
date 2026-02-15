package com.example.gateway.security;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Component
public class CustomerConvertor implements Converter<Jwt, Collection<GrantedAuthority>> {
    private static  final Logger log = LoggerFactory.getLogger(CustomerConvertor.class);

    @Value("${keycloak.clientId}")
    public String clientId;

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        log.info("starting ....");
        Map<String, Object> resourceAccess = (Map<String, Object>) jwt.getClaimAsMap("resource_access");

        if(resourceAccess == null || !resourceAccess.containsKey(clientId)){
            return List.of();
        }

        for(Map.Entry<String, Object> entry : resourceAccess.entrySet()){
            String key = entry.getKey();
            String value = entry.getValue().toString();
            log.info("Converting client id {} resource level {}", key, value);
        }

        Map<String, Object> mapRoles = (Map<String,Object>) resourceAccess.get(clientId);

        if(mapRoles == null)
        {
            log.info("No client id found");
            return List.of();
        }

        for (Map.Entry<String, Object> entry : mapRoles.entrySet()){
            log.info("Converting client id {} resource next", entry.getKey());
        }

        Collection<String> roles = (Collection<String>)  mapRoles.get("roles");

        for(String role : roles){
            log.info("Converting client id {} role", role);
        }

        if(roles == null)
        {
            return List.of();
        }

        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_"+role))
                .collect(Collectors.toList());
    }
}
