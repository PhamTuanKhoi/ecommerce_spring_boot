package com.v1.ecommerce.service;

import com.v1.ecommerce.exception.UserException;
import com.v1.ecommerce.model.User;

public interface UserService {
    public User findUserById(Long userId) throws UserException;
    public User findUserProviderByJwt(String jwt) throws UserException;
}
