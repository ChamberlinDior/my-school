package com.mydbs.backend.config;

import com.mydbs.backend.auth.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsService userDetailsService;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
                          UserDetailsService userDetailsService) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/**",
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/api-docs/**",
                                "/actuator/health"
                        ).permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/users/**").authenticated()
                        .requestMatchers("/api/users/**")
                        .hasAnyAuthority("SUPER_ADMIN", "ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/academic-years/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/programs/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/cohorts/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/classes/**").authenticated()

                        .requestMatchers("/api/academic-years/**")
                        .hasAnyAuthority("SUPER_ADMIN", "ADMIN", "PEDAGOGICAL_MANAGER", "SCHOOL_MANAGER")

                        .requestMatchers("/api/programs/**")
                        .hasAnyAuthority("SUPER_ADMIN", "ADMIN", "PEDAGOGICAL_MANAGER", "SCHOOL_MANAGER")

                        .requestMatchers("/api/cohorts/**")
                        .hasAnyAuthority("SUPER_ADMIN", "ADMIN", "PEDAGOGICAL_MANAGER", "SCHOOL_MANAGER")

                        .requestMatchers("/api/classes/**")
                        .hasAnyAuthority("SUPER_ADMIN", "ADMIN", "PEDAGOGICAL_MANAGER", "SCHOOL_MANAGER")

                        .requestMatchers(HttpMethod.GET, "/api/courses/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/course-modules/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/lessons/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/course-sessions/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/course-resources/**").authenticated()

                        .requestMatchers("/api/courses/**")
                        .hasAnyAuthority("SUPER_ADMIN", "ADMIN", "PEDAGOGICAL_MANAGER", "SCHOOL_MANAGER", "TEACHER")

                        .requestMatchers("/api/course-modules/**")
                        .hasAnyAuthority("SUPER_ADMIN", "ADMIN", "PEDAGOGICAL_MANAGER", "SCHOOL_MANAGER", "TEACHER")

                        .requestMatchers("/api/lessons/**")
                        .hasAnyAuthority("SUPER_ADMIN", "ADMIN", "PEDAGOGICAL_MANAGER", "SCHOOL_MANAGER", "TEACHER")

                        .requestMatchers("/api/course-sessions/**")
                        .hasAnyAuthority("SUPER_ADMIN", "ADMIN", "PEDAGOGICAL_MANAGER", "SCHOOL_MANAGER", "TEACHER")

                        .requestMatchers("/api/course-resources/**")
                        .hasAnyAuthority("SUPER_ADMIN", "ADMIN", "PEDAGOGICAL_MANAGER", "SCHOOL_MANAGER", "TEACHER")

                        .requestMatchers(HttpMethod.GET, "/api/students/**").authenticated()
                        .requestMatchers("/api/students/**")
                        .hasAnyAuthority("SUPER_ADMIN", "ADMIN", "PEDAGOGICAL_MANAGER", "SCHOOL_MANAGER", "SUPPORT")

                        .requestMatchers(HttpMethod.GET, "/api/classes/*/students").authenticated()
                        .requestMatchers("/api/classes/*/students")
                        .hasAnyAuthority("SUPER_ADMIN", "ADMIN", "PEDAGOGICAL_MANAGER", "SCHOOL_MANAGER", "TEACHER", "SUPPORT")

                        .requestMatchers(HttpMethod.GET, "/api/teachers/**").authenticated()
                        .requestMatchers("/api/teachers/**")
                        .hasAnyAuthority("SUPER_ADMIN", "ADMIN", "PEDAGOGICAL_MANAGER", "SCHOOL_MANAGER", "SUPPORT")

                        .requestMatchers(HttpMethod.GET, "/api/programs/*/teachers").authenticated()
                        .requestMatchers("/api/programs/*/teachers")
                        .hasAnyAuthority("SUPER_ADMIN", "ADMIN", "PEDAGOGICAL_MANAGER", "SCHOOL_MANAGER", "SUPPORT")

                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}