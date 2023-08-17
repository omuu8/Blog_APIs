package com.example.blog_app.demo_blogging.config;

import com.example.blog_app.demo_blogging.security.CustomUserDetailService;
import com.example.blog_app.demo_blogging.security.JwtAuthenticationEntryPoint;
import com.example.blog_app.demo_blogging.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebSecurity
@EnableWebMvc
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {


    public static final String[] PUBLIC_URLS = {
            "/api/v1/auth/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/swagger-resources/**",
            "/swagger-ui/**",
            "/webjars/**"
    };
    //Below both the methods used are for basic auth which is in JS
    // The main problem with this authentication is that every time you logged into the system you need to login it again
    // Therefore we shift to JWT Authentication
    //Also the JWT Auth works on token based auth and also is stateless
    //The token generated is handled at the client's side

//	private static final String PUBLIC_URLS = null;

    @Autowired
    private CustomUserDetailService customUserDetailService;

    @Autowired
    private JwtAuthenticationEntryPoint point;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    // From version 5.7 the WebSecurityConfigurerAdapter class is depreciated so that we dont need to override any security method
    //and we can move towards component-based security configuration

    //	public SecurityFilterChain filter(HttpSecurity http){
//		http
//				.csrf(c -> c.disable())
//				.authorizeHttpRequests(a -> a.)
//
//	}
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(c -> c.disable())
                .cors(c -> c.disable())
                .authorizeHttpRequests(auth -> auth.
                        requestMatchers(PUBLIC_URLS)
                        .permitAll().
                        requestMatchers(HttpMethod.GET).permitAll()
                        .anyRequest()
                        .authenticated())
                .exceptionHandling(ex -> ex.authenticationEntryPoint(point))
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));


        http.addFilterBefore(this.jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        http.authenticationProvider(daoAuthenticationProvider());
        DefaultSecurityFilterChain defaultSecurityFilterChain = http.build();
        return defaultSecurityFilterChain;

    }

    /*
    public void configure(AuthenticationManagerBuilder auth)throws Exception{

        auth.userDetailsService(this.customUserDetailService).passwordEncoder(passwordEncoder());
    }
    */
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(this.customUserDetailService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();


    }
}