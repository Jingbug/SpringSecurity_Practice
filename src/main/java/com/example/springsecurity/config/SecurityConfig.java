package com.example.springsecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity  // 이 Class는 Spring Security가 관리
public class SecurityConfig {

    // SpringSecurity는 비밀번호 암호화를 기본으로 한다.
    // 단방향 Hash 암호화를 사용 -> 암호화 해지 불가능
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder () {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http
                    .authorizeHttpRequests((auth) -> auth   // Http 요청이 있을 시, 람다식으로 표현
                            // 상단에서부터 동작 순서 주의!!!

                            // "/", "login"은 모든 사용자가 접근
                            .requestMatchers("/", "/login", "/loginProc", "/join", "/joinProc").permitAll()

                            // "/admin"은 "ADMIN"이라는 권한을 가진 사용자가 접근
                            .requestMatchers("/admin").hasRole("ADMIN")

                            // "/mypage"는 **을 통해 입력받는 권한에 따라 접근
                            .requestMatchers("/mypage/**").hasAnyRole("ADMIN", "USER")

                            // 나머지의 요청에 대해선 로그인한 사용자만 접근 or denyAll() -> 모든 사용자 접근 X
                            .anyRequest().authenticated()
                    );
            http
                    .formLogin((auth) -> auth.loginPage("/login") // LoginPage의 기본경로 설정 redirect
                            // SpringSecurity가 자동으로 login 요청 처리
                            .loginProcessingUrl("/loginProc").permitAll()
                    );
            // SpringSecurity는 id, pw외 security token을 받아야 로그인 처리를 해준다
            // csrf란? 요청을 위조하여 사용자가 원하지 않아도 서버측으로 강제로 특정 요청을 보낸다. (DB의 CRUD에 강제 관여)
            // default는 enable 상태이며, 개발 시 disable, 배포 시 enable 설정
            // enable상태에서는 logout을 get으로 요청할 수 없다!! -> LogoutController를 통해 작업
//            http
//                    .csrf((auth) -> auth.disable());
            http
                    // 하나의 아이디에서 최대 Session 생성 갯수 (다중 로그인)
                    // 세션 갯수를 초과할 경우 true : 로그인 차단 / false : 기존 세션 삭제, 새로 생성
                    .sessionManagement((auth) -> auth
                            .maximumSessions(1)
                            .maxSessionsPreventsLogin(true)
                    );
            http
                    // 세션 고정 보호 작업 -> 세션을 바꾸거나, 세션의 쿠키 ID 값을 바꾸어 보안 강화
                    // none() : 세션 값을 고정으로 계속 사용
                    // newSession() : 로그인마다 세션 값을 변경
                    // changeSessionId() : 로그인마다 세션의 쿠키 ID 값을 변경 -> 자주 사용
                    .sessionManagement((auth) -> auth
                            .sessionFixation().changeSessionId()
                    );

        return http.build();
    }
}
