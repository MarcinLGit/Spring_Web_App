package com.freelibrary.Paplibrary.user;

import java.util.List;

public interface UserService {
    void saveUser(RegistrationDto registrationDto);
    List<UserDto> getAllUsers();
    User findByEmail(String email);
    void deleteUserByID(Long id);
}
