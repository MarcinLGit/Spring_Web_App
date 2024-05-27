package com.freelibrary.Paplibrary.user;

import java.util.List;

public interface UserService {
    void saveUser(RegistrationDto registrationDto);
    List<UserDto> getAllUsers();
    User findByEmail(String email);
    User findByUserId(Long userId);
    void deleteUserByID(Long id);
}
