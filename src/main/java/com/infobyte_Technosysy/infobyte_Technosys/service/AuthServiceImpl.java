package com.infobyte_Technosysy.infobyte_Technosys.service;

import com.infobyte_Technosysy.infobyte_Technosys.model.AppUser;
import com.infobyte_Technosysy.infobyte_Technosys.repository.AppUserRepo;
import com.infobyte_Technosysy.infobyte_Technosys.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
    private AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final AppUserRepo appUserRepo;

    @Override
    public String login(String username, String password) {
     var authToken=new UsernamePasswordAuthenticationToken(username,password);
     var authenticate=authenticationManager.authenticate(authToken);
        return JwtUtils.generateToken(((UserDetails)(authenticate.getPrincipal())).getUsername());

    }

    @Override
    public String signUp(String name, String username, String password) {
        if(appUserRepo.existByUsername(username)) {
            throw new RuntimeException("User already exists");
        }

        var encodedPassword = passwordEncoder.encode(password);
        var authority = new SimpleGrantedAuthority("ROLE_USER");
        var authorities = new ArrayList<GrantedAuthority>();
        authorities.add(authority);

        AppUser appUser = AppUser.builder()
                .name(name)
                .username(username)
                .password(encodedPassword).authorities(authorities)
                .build();
        appUserRepo.save(appUser);
        return JwtUtils.generateToken(username);
    }
}
