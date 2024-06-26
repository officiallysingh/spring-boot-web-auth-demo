package com.ksoot.web.auth.security;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 * @author Rajveer Singh
 */
@Configuration
@EnableConfigurationProperties(value = {ActuatorEndpointProperties.class})
@EnableWebSecurity
@EnableMethodSecurity // Allow method annotations like @PreAuthorize, not required as of now
class SecurityConfiguration {

    private final String[] SWAGGER_URLS = new String[]{"/swagger-resources/**", "/swagger-ui/**", "/swagger-ui.*", "/v3/api-docs", "/v3/api-docs/**", "/webjars/**", "/sw.js"};

    private boolean actuatorBypassSecurity = true;

    private final AuthenticationEntryPoint authenticationEntryPoint;

    private final AccessDeniedHandler accessDeniedHandler;

    private final ActuatorEndpointProperties actuatorEndpointProperties;

    SecurityConfiguration(@Nullable final AuthenticationEntryPoint authenticationEntryPoint,
                          @Nullable final AccessDeniedHandler accessDeniedHandler,
                          @Nullable final ActuatorEndpointProperties actuatorEndpointProperties) {
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
        this.actuatorEndpointProperties = actuatorEndpointProperties;
    }

    @Bean
    SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
        // @formatter:off
        http.csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests((requests) -> requests
                .requestMatchers(this.actuatorBypassSecurity && this.actuatorEndpointProperties != null
                    ? ArrayUtils.addAll(SWAGGER_URLS, this.actuatorEndpointProperties.getPaths())
                    : SWAGGER_URLS).permitAll()
//                .requestMatchers("/problems/**", "/**/pets/**").permitAll()
                .anyRequest().authenticated()
            )
                .oauth2ResourceServer(
                resourceServerCustomizer ->
                        resourceServerCustomizer.jwt(
                                jwtCustomizer ->
                                        jwtCustomizer
                                                .jwtAuthenticationConverter(this.jwtAuthenticationConverter())
                                                .decoder(new JwtStringDecoder())));

        if (this.authenticationEntryPoint != null) {
          http.exceptionHandling(
                  exceptionHandling ->
                          exceptionHandling.authenticationEntryPoint(this.authenticationEntryPoint));
        }
        if (this.accessDeniedHandler != null) {
          http.exceptionHandling(
                  exceptionHandling -> exceptionHandling.accessDeniedHandler(this.accessDeniedHandler));
        }
        // @formatter:on
        return http.build();
    }

    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(JwtUtils.jwtGrantedAuthoritiesConverter());
        jwtAuthenticationConverter.setPrincipalClaimName(IdentityHelper.ClaimName.SUBJECT.value());
        return jwtAuthenticationConverter;
    }

    static class JwtStringDecoder  implements JwtDecoder {

        @Override
        public Jwt decode(final String token) throws JwtException {
            return JwtUtils.decodeToken(token);
        }
    }
}
