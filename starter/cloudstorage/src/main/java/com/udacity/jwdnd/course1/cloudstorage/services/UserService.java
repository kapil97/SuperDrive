package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mappers.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class UserService {
    private UserMapper userMapper;
    private HashService hashService;

    public UserService(UserMapper userMapper, HashService hashService) {
        this.userMapper = userMapper;
        this.hashService = hashService;
    }
    public int createUser(User user){
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        String encodedSalt = Base64.getEncoder().encodeToString(salt);
        String hashPass = hashService.getHashedValue(user.getPassword(), encodedSalt);
        return userMapper.addUser(new User(null, user.getUsername(), encodedSalt, hashPass, user.getFirstName(), user.getLastName()));
    }

    public User getUser(String username){
        return userMapper.getUser(username);
    }
    public boolean isUsernameAvailable(String username) {
        return this.getUser(username) == null;
    }
    public List<User> getAllUserData(){return userMapper.getAllUser();}

}
