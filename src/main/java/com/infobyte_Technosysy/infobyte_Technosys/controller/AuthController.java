package com.infobyte_Technosysy.infobyte_Technosys.controller;

import com.infobyte_Technosysy.infobyte_Technosys.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody AuthRequestDto authRequestDto)
    {
        var jwtToken=authService.login(authRequestDto.username(),authRequestDto.password());
        var authResponseDto=new AuthResponseDto(jwtToken, AuthStatus.LOGIN_SUCCESS);
       return ResponseEntity.status(HttpStatus.OK).body(authResponseDto);
    }

    @PostMapping("/sign-Up")
    public ResponseEntity<AuthResponseDto> singUp(@RequestBody AuthRequestDto authRequestDto)
    {
        try
        {
       var jwtToken= authService.signUp(authRequestDto.name(),authRequestDto.username(),authRequestDto.password());
        var authResponseDto=new AuthResponseDto(jwtToken, AuthStatus.USER_CREATED_SUCCESSFULLY);
        return ResponseEntity.status(HttpStatus.OK).body(authResponseDto);
    }
     catch (Exception e)
     {
         var authResponseDto=new AuthResponseDto(null, AuthStatus.USER_NOT_CREATED_SUCCESSFULLY);
         return ResponseEntity.status(HttpStatus.CONFLICT).body(authResponseDto);

     }
    }
}
