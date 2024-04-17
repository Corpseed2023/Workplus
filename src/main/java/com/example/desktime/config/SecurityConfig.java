package com.example.desktime.config;//package com.example.desktime.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//import org.springframework.security.web.SecurityFilterChain;
//
//import static org.springframework.security.config.Customizer.withDefaults;
//
//
//@Configuration
//public class SecurityConfig {
//
////    @Bean
////    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
////        http.authorizeRequests()
////                .requestMatchers("/allUsersList","/createUser").authenticated()
////                .requestMatchers("/createUser").authenticated() // Require authentication for "/abc"
////                .requestMatchers("/userDetails","/createUser").permitAll() // Allow access to "/xyq" without authentication
////                .and()
////                .formLogin(withDefaults())
////                .httpBasic(withDefaults());
////        return http.build();
////    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder ()
//    {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public UserDetailsService userDetailsService ()
//
//    {
//        UserDetails adminUser = User.builder()
//
//                .username("admin")
//                .password(passwordEncoder().encode("admin"))
//                .roles("ADMIN")
//                .build();
//
//        UserDetails normalUser = User.builder()
//
//                .username("user")
//                .password(passwordEncoder().encode("user"))
//                .roles("USER")
//                .build();
//
//
//        return new InMemoryUserDetailsManager(adminUser,normalUser);
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager (AuthenticationConfiguration configuration) throws Exception {
//        return configuration.getAuthenticationManager();
//    }
//}
//
//


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
