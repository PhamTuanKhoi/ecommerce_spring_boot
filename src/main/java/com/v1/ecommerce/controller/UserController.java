package com.v1.ecommerce.controller;

import com.v1.ecommerce.config.JwtConstant;
import com.v1.ecommerce.exception.UserException;
import com.v1.ecommerce.model.User;
import com.v1.ecommerce.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public User findUserById(@PathVariable Long id) throws UserException {
        return userService.findUserById(id);
    };

    @GetMapping("/profile")
    public User findUserProviderByJwt(HttpServletRequest request) throws UserException{ 
        String jwt = request.getHeader(JwtConstant.JWT_HEADER);

        if(jwt == null || jwt.isEmpty()){
            throw new UserException("Incorrect jwt!");
        }

        return userService.findUserProviderByJwt(jwt);
    };
}
