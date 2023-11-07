package com.example.mytodolist_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;


@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();

    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000")); // 許可するオリジン
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")); // 許可するHTTPメソッド
        configuration.setAllowCredentials(true); // クレデンシャルを許可
        configuration.setAllowedHeaders(List.of("*")); // すべてのリクエストヘッダーを許可
        configuration.setExposedHeaders(List.of("Set-Cookie")); // クライアントに露出するヘッダー
        configuration.setMaxAge(3600L); // pre-flightリクエストのキャッシュ時間(秒)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // すべてのパスに適用
        return source;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(request -> {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowCredentials(true);
                config.addAllowedOriginPattern("*"); // または特定のオリジンを指定
                config.addAllowedMethod("*");
                config.addAllowedHeader("*");
                return config;
            }))
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests((auth) -> auth
                .requestMatchers("/api/users/register", "/api/users/login", "/api/users/getUserinfo").permitAll()
                .requestMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**").permitAll()
                .anyRequest().authenticated())
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))//ここはALWAYSか、IF_REQUIRED
            .rememberMe(rememberMe -> rememberMe
                    .tokenValiditySeconds(604800)
                    .key("uniqueAndSecret")
                    .rememberMeCookieName("remember-me-cookie-name")
                    .useSecureCookie(false) // HTTPSを使っている場合にのみセットをtrueに
            )
            .logout(logout -> logout
                    .deleteCookies("JSESSIONID", "remember-me-cookie-name")
                    .invalidateHttpSession(true)
            )
            .httpBasic(AbstractHttpConfigurer::disable);
//                .formLogin(AbstractHttpConfigurer::disable);
        return http.build();
    }
}

