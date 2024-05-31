package com.freelibrary.Paplibrary.user;

public class UserMapper {

    public static User mapToUser(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .login(userDto.getLogin())
                .email(userDto.getEmail())
                .password_hash(userDto.getPassword_hash())
                .roles(userDto.getRoles())
                .build();
    }

    public static UserDto mapToUserDto(User user) {
        System.out.println(user.getRoles().toString());
        return UserDto.builder()
                .id(user.getId())
                .login(user.getLogin())
                .email(user.getEmail())
                .password_hash(user.getPassword_hash())
                .roles(user.getRoles())
                .build();
    }
}
