package com.example.demo4.SecurityApp.controllers;

import com.example.demo4.SecurityApp.dto.LoginDto;
import com.example.demo4.SecurityApp.dto.LoginResponseDto;
import com.example.demo4.SecurityApp.dto.SignUpDto;
import com.example.demo4.SecurityApp.dto.UserDto;
import com.example.demo4.SecurityApp.services.AuthService;
import com.example.demo4.SecurityApp.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    @Value("${deploy.env}")
    private String deployEnv;

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signUp(@RequestBody SignUpDto signUpDto) {
        UserDto userDto = userService.signUp(signUpDto);
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginDto loginDto, HttpServletRequest request,
                                        HttpServletResponse response) {
        LoginResponseDto loginResponseDto = authService.login(loginDto);

        Cookie cookie = new Cookie("refreshToken", loginResponseDto.getRefreshToken());
        cookie.setHttpOnly(true);
        cookie.setSecure("production".equals(deployEnv));
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok(loginResponseDto);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDto> refresh(HttpServletRequest request,HttpServletResponse response) {
        String refreshToken = Arrays.stream(request.getCookies()).
                filter(cookie -> "refreshToken".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new AuthenticationServiceException("Refresh token not found inside the Cookies"));
        LoginResponseDto loginResponseDto = authService.refreshToken(refreshToken);
        //Add new refresh token to cookie
        Cookie cookie = new Cookie("refreshToken", loginResponseDto.getRefreshToken());
        cookie.setHttpOnly(true);
        cookie.setSecure("production".equals(deployEnv));
        cookie.setPath("/");
        response.addCookie(cookie);
        return ResponseEntity.ok(loginResponseDto);
    }

    //Logout end-point
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {

        // Check if the cookies array is present and contains the refreshToken
        Cookie[] cookies = request.getCookies();
        if (cookies == null || Arrays.stream(cookies).noneMatch(cookie -> "refreshToken".equals(cookie.getName()))) {
            // If no refresh token is found in the cookies, return a message indicating that the user is already logged out
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User is already logged out or no session exists.");
        }
        // Get refresh token from the cookie
        String refreshToken = Arrays.stream(request.getCookies())
                .filter(cookie -> "refreshToken".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new AuthenticationServiceException("Refresh token not found inside the Cookies"));

        // Invalidate the session associated with the refresh token
        authService.logout(refreshToken);

        // Remove the refreshToken from the cookie
        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setHttpOnly(true);
        cookie.setSecure("production".equals(deployEnv));
        cookie.setMaxAge(0); // Set the cookie's max age to 0 to delete it
        cookie.setPath("/"); // Ensure the correct cookie is removed
        response.addCookie(cookie);
        //

        return ResponseEntity.noContent().build(); // Return 204 No Content status
    }
}
