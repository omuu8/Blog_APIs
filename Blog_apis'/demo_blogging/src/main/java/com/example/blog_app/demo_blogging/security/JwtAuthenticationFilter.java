package com.example.blog_app.demo_blogging.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Enumeration;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        //1) Get Token

        String requestToken = request.getHeader("Authorization");
        Enumeration<String> headerNames = request.getHeaderNames();

        while(headerNames.hasMoreElements())
        {
            System.out.println(headerNames.nextElement());
        }
        //Bearer 2743dsdf

        System.out.println(requestToken);

        String userName = null;
        String token = null;

        if(requestToken != null && requestToken.startsWith("Bearer")) {

            token = requestToken.substring(7);
            try {
                userName = this.jwtTokenHelper.getUserNameFromToken(token);
            }catch(IllegalArgumentException e) {
                System.out.println("Unable to get Jwt Token");
            }catch(ExpiredJwtException e) {
                System.out.println("Jwt Token has been expired");
            }catch(MalformedJwtException e) {
                System.out.println("Invalid Jwt Exception");
            }

        }else {
            System.out.println("JwtToken does not begin with Bearer");
        }

        if(userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);

            // we need to validate the token using received token and userDetails

            if(this.jwtTokenHelper.validateToken(token, userDetails)) {
                // sab sahi chal rah
                //authentication karna h ekbad validate hua to

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

            }else {
                System.out.println("Invalid Jwt Token");
            }
        }else {
            System.out.println("username is null or context is not null");
        }

        filterChain.doFilter(request, response);
    }

}
