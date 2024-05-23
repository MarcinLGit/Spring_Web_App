package com.freelibrary.Paplibrary.user;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String login;
    private String email;
    private String password_hash;
    private List<Role> roles = new ArrayList<>();
}
