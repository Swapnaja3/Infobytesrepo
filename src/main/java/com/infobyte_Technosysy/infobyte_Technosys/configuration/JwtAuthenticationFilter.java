package com.infobyte_Technosysy.infobyte_Technosys.configuration;

import com.mysql.cj.util.StringUtils;
import com.mysql.cj.x.protobuf.MysqlxDatatypes;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.infobyte_Technosysy.infobyte_Technosys.util.JwtUtils;

import java.io.IOException;
import java.util.Optional;

import static org.springframework.util.StringUtils.hasText;
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var jwtTokenOptional= getTokenFromRequest(request);
        jwtTokenOptional.ifPresent(jwtToken -> {
            if(JwtUtils.validateToken(jwtToken))
            {
                var username=JwtUtils.getUsernameFromToken(jwtToken);
                var userDetails=userDetailsService.loadUserByUsername(username);
                var authenticationToken=new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        });
        filterChain.doFilter(request,response);
    }

    private Optional<String> getTokenFromRequest(HttpServletRequest request) {
        var authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && !authHeader.trim().isEmpty() && authHeader.trim().startsWith("Bearer"))
        {
            return Optional.of(authHeader.substring(7));
        }
        return Optional.empty();

    }
}
