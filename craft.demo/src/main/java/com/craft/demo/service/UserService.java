package com.craft.demo.service;

import com.craft.demo.models.dtos.GenericResponse;
import com.craft.demo.models.dtos.UserDTO;

public interface UserService {
    public GenericResponse<UserDTO> registerUser(UserDTO userDTO);

    public GenericResponse<UserDTO> loginUser(UserDTO userDTO);
}
