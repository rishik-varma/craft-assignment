package com.craft.demo.service;

import com.craft.demo.models.dtos.GenericResponse;
import com.craft.demo.models.dtos.UserDTO;
import com.craft.demo.models.enitities.User;
import com.craft.demo.repositories.CustomORM;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public GenericResponse<UserDTO> registerUser(UserDTO userDTO) {
        try {
            User savedUser = CustomORM.saveUser(modelMapper.map(userDTO, User.class));
            return new GenericResponse<>(HttpStatus.CREATED, modelMapper.map(savedUser, UserDTO.class), "Success");
        } catch (Exception e) {
            return new GenericResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, null, "Failure");
        }
    }

    @Override
    public GenericResponse<UserDTO> loginUser(UserDTO userDTO) {
        try {
            User savedUser = CustomORM.findUserByUsernameAndPassword(userDTO.getUsername(), userDTO.getPassword());
            if(savedUser == null){
                return new GenericResponse<>(HttpStatus.UNAUTHORIZED, null, "Incorrect username or password!!");
            }
            return new GenericResponse<>(HttpStatus.OK, modelMapper.map(savedUser, UserDTO.class), "Success");
        } catch(Exception e) {
            return new GenericResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, null, "Failure");
        }
    }
}
