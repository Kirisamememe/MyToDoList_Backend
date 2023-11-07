package com.example.mytodolist_backend.controller;

import com.example.mytodolist_backend.HelloController;
import com.example.mytodolist_backend.dto.LoginDto;
import com.example.mytodolist_backend.dto.UserDto;
import com.example.mytodolist_backend.entity.User;
import com.example.mytodolist_backend.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(HelloController.class);

    private final UserService userService;


    private final AuthenticationManager authenticationManager;


    private final SecurityContextRepository securityContextRepository;

    @Autowired
    public UserController(UserService userService, AuthenticationManager authenticationManager, SecurityContextRepository securityContextRepository) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.securityContextRepository = securityContextRepository;
    }

//    @CrossOrigin
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody UserDto userDto){
        //リクエストに含まれた情報はUserDtoとして見なされる
        User newUser = userService.registerNewUser(userDto);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

//    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    @GetMapping("/testSession")
    public String testSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        return "Session ID: " + session.getId();
    }

//    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    @PostMapping("/login")
    public ResponseEntity<?> login(HttpServletRequest request, HttpServletResponse response, @RequestBody LoginDto loginDto){
        try {
            log.info("loginプロセスに入ったぞ");
            String email = loginDto.getEmail();
            String password = loginDto.getPassword();
            log.info(email);
            log.info(password);
            //ユーザー認証の試み
            log.info("ここからが本番やぞ！");
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    email,
                    password
                )
            );
            log.info("ユーザー認証が終わったらしいぞ！早速確認するわ");

            //SecurityContextHolderに認証情報を保存

            SecurityContextHolder.getContext().setAuthentication(authentication); //getUserinfoに飛ぶ模様
            securityContextRepository.saveContext(SecurityContextHolder.getContext(),request, response);

            return ResponseEntity.ok().body("ログイン成功");

        } catch (AuthenticationException ex) {
            //認証に失敗した場合の処理
            log.info(String.valueOf(ex));
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("認証に失敗しました。");
        }
    }


//    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    @GetMapping("/getUserinfo")
    public ResponseEntity<?> getUserinfo(HttpServletRequest request) {
        log.info("getUserinfoに入ったぞ");

        HttpSession session = request.getSession(false);
        if (session != null){
            log.info("sessionはあるぞ");
            SecurityContext securityContext = (SecurityContext) session.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);

            if (securityContext != null) {
                log.info("securityContextもあるぞ");
                Authentication authentication = securityContext.getAuthentication();

                if (authentication != null) {
                    log.info("認証情報: " + authentication.getName());
                }
                else {
                    log.info("認証情報は存在しない");
                }
            }
            else {
                log.info("SecurityContextがセッションに保存されてないわ。");
            }
        }
        else {
            log.info("sessionが存在しない");
        }


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            log.info("Authentication class: {}", authentication.getClass());
            log.info("Principal: {}", authentication.getPrincipal());
            log.info("Authorities: {}", authentication.getAuthorities());
            log.info("Credentials: {}", authentication.getCredentials());
            log.info("Details: {}", authentication.getDetails());
            log.info("Authenticated: {}", authentication.isAuthenticated());
        } else {
            log.info("No authentication information available");
        }


        String email = Objects.requireNonNull(authentication).getName();

        log.info("今ログインしてるユーザーは" + email);
        log.info("匿名じゃなくてちゃんとしたユーザーなのか？ >> " + !(authentication instanceof AnonymousAuthenticationToken));
        try{
            if (!(authentication instanceof AnonymousAuthenticationToken)) {
                log.info("今は匿名ユーザーじゃないぞ");
                UserDetails user = userService.loadUserByUsername(email);
                if (user == null) {
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.TEXT_PLAIN);
                    return new ResponseEntity<>("存在しないユーザー", headers, HttpStatus.UNAUTHORIZED);
                }
                // ユーザー情報を返す
                log.info("ユーザー情報取得できたぞ");
                return ResponseEntity.ok(user);
            } else {
                log.info("ログインできてないやん。やり直しや");
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.TEXT_PLAIN);
                return new ResponseEntity<>("ログインしてないか認証が切れた", headers, HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception e){
            log.info("エラーが出たわ。権限がないんちゃう？");
            log.info(String.valueOf(e));
            return new ResponseEntity<>("権限がない", HttpStatus.UNAUTHORIZED);
        }
    }
}
