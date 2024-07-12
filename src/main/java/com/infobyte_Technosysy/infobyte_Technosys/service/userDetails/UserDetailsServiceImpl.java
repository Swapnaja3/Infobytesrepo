package com.infobyte_Technosysy.infobyte_Technosys.service.userDetails;

import com.infobyte_Technosysy.infobyte_Technosys.model.AppUser;
import com.infobyte_Technosysy.infobyte_Technosys.repository.AppUserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;



@Component
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private final AppUserRepo appUserRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      /* var appUser= appUserRepo.findByUserName(username).orElseThrow(()->new UsernameNotFoundException("Username not found"));
       return new User(appUser.getUsername(), appUser.getPassword(), appUser.getAuthorities());    }*/
        Object userObject = appUserRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));

        if (userObject instanceof AppUser) {
            AppUser appUser = (AppUser) userObject;
            return new User(appUser.getUsername(), appUser.getPassword(), appUser.getAuthorities());
        } else {
            throw new UsernameNotFoundException("Username not found");
        }
    }
}
