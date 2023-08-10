package com.v1.ecommerce.service.impl;

import com.v1.ecommerce.config.JwtProvider;
import com.v1.ecommerce.exception.UserException;
import com.v1.ecommerce.model.User;
import com.v1.ecommerce.repository.UserRepository;
import com.v1.ecommerce.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private JwtProvider jwtProvider;

    public UserServiceImpl(UserRepository userRepository, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public User findUserById(Long id) throws UserException {
        Optional<User> user = this.userRepository.findById(id);

        if(user == null){
            throw new UserException("User not found by id# " + id);
        }
        return user.get();
    }

    @Override
    public User findUserProviderByJwt(String jwt) throws UserException {
        String email = this.jwtProvider.getEmailFromToken(jwt);

        User user = this.userRepository.findByEmail(email);

        if(user == null){
            throw new UserException("User not found by email# " + email);
        }
        return user;
    }
}
