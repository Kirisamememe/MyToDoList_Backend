//package com.example.mytodolist_backend.util;
//
//import jakarta.servlet.ServletException;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.web.filter.OncePerRequestFilter;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.Collections;
//
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//
//    private final JwtUtil jwtUtil;
//
//    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
//        this.jwtUtil = jwtUtil;
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//
//        // Authorization ヘッダーからトークンを取得
//        String header = request.getHeader("Authorization");
//        String username = null;
//        String jwtToken = null;
//
//        // ヘッダーが null でなく、Bearer で始まる場合
//        if (header != null && header.startsWith("Bearer ")) {
//            jwtToken = header.substring(7); // "Bearer " の後のトークンを取得
//            username = jwtUtil.extractUsername(jwtToken); // トークンからユーザー名を取得
//        }
//
//        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            // トークンの検証を行い、有効な場合は認証情報をセット
//            if (jwtUtil.validateToken(jwtToken, username)) {
//                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
//                        username, null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//            }
//        }
//        filterChain.doFilter(request, response); // フィルタチェーンを続行
//    }
//}
//
//
