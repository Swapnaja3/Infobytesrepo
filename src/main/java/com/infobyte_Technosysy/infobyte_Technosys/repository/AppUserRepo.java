package com.infobyte_Technosysy.infobyte_Technosys.repository;

import com.infobyte_Technosysy.infobyte_Technosys.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface AppUserRepo extends JpaRepository<AppUser,Integer> {
    boolean existByUsername(String username);
    Optional<AppUser> findByUsername(String username);
}
