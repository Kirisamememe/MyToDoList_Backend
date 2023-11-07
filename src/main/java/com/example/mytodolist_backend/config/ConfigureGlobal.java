package com.example.mytodolist_backend.config;

import com.example.mytodolist_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class ConfigureGlobal {
    //グローバル構成
    //アプリケーションの認証マネージャーをカスタマイズ

    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;

    public ConfigureGlobal(UserService userService, BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userService);
        provider.setPasswordEncoder(passwordEncoder);
        auth.authenticationProvider(provider);
    }
    //AuthenticationManagerBuilder に DaoAuthenticationProvider を追加することで、ユーザー名とパスワードに基づいた認証を設定
}
