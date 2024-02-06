package com.craft.demo.models.dtos;

import com.craft.demo.models.enums.UserType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class UserDTO extends BaseDTO {
    private String username;
    private String password;
    private String phone;
    private UserType userType;
}
