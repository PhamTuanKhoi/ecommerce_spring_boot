package com.v1.ecommerce.controller;

import com.v1.ecommerce.config.JwtProvider;
import com.v1.ecommerce.exception.UserException;
import com.v1.ecommerce.model.Cart;
import com.v1.ecommerce.model.User;
import com.v1.ecommerce.repository.UserRepository;
import com.v1.ecommerce.request.LoginRequest;
import com.v1.ecommerce.response.AuthResponse;
import com.v1.ecommerce.service.CartService;
import com.v1.ecommerce.service.impl.CustomUserServiceImplementation;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private  UserRepository userRepository;
    private JwtProvider jwtProvider;
    private  PasswordEncoder passwordEncoder;
    private  CustomUserServiceImplementation customUserServiceImplementation;
    private CartService cartService;

    public AuthController(UserRepository userRepository, JwtProvider jwtProvider, PasswordEncoder passwordEncoder, CustomUserServiceImplementation customUserServiceImplementation, CartService cartService) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
        this.customUserServiceImplementation = customUserServiceImplementation;
        this.cartService = cartService;
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User userRequest) throws UserException {
        User userEmail = this.userRepository.findByEmail(userRequest.getEmail());

        if (userEmail != null) {
            throw new UserException("Email is already exist");
        }

        ModelMapper modelMapper = new ModelMapper();
        User user = new User();
        modelMapper.map(userRequest, user);
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));

        User saved = this.userRepository.save(user);

        Cart cart = new Cart();
        cart.setUser(saved);
        this.cartService.create(saved);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                saved.getEmail(), saved.getPassword()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = this.jwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setMessage("Signup success");

        return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> loginUserHandler(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticate(loginRequest.getEmail(), loginRequest.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = this.jwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setMessage("Signin success");

        return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.CREATED);
    }

    private Authentication authenticate(String email, String password) {
        UserDetails userDetails = this.customUserServiceImplementation.loadUserByUsername(email);

        if (userDetails == null) {
            throw new BadCredentialsException("Invalid username");
        }

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
