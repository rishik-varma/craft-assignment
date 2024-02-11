package com.craft.demo.service;

import com.craft.demo.models.dtos.GenericResponse;
import com.craft.demo.models.dtos.UserDTO;
import com.craft.demo.models.enitities.User;
import com.craft.demo.repositories.CustomORM;
import com.craft.demo.utils.CraftConstants;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public GenericResponse<UserDTO> registerUser(UserDTO userDTO) {
        try {
            logger.info("UserServiceImpl.registerUser => method called with user : {}", userDTO);
            User savedUser = CustomORM.saveUser(modelMapper.map(userDTO, User.class));
            return new GenericResponse<>(HttpStatus.CREATED, modelMapper.map(savedUser, UserDTO.class), CraftConstants.SUCCESS_MESSAGE);
        } catch (Exception e) {
            logger.error("UserServiceImpl.registerUser => " + CraftConstants.EXCEPTION_OCCURRED_MESSAGE, e);
            return new GenericResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, null, CraftConstants.FAILURE_MESSAGE);
        }
    }

    @Override
    public GenericResponse<UserDTO> loginUser(UserDTO userDTO) {
        try {
            logger.info("UserServiceImpl.loginUser => method called with user: {}", userDTO);
            User savedUser = CustomORM.findUserByUsernameAndPassword(userDTO.getUsername(), userDTO.getPassword());
            if(savedUser == null){
                logger.info("UserServiceImpl.loginUser => " + CraftConstants.INCORRECT_USERNAME_OR_PASSWORD_MESSAGE, userDTO);
                return new GenericResponse<>(HttpStatus.UNAUTHORIZED, null, CraftConstants.INCORRECT_USERNAME_OR_PASSWORD_MESSAGE);
            }
            return new GenericResponse<>(HttpStatus.OK, modelMapper.map(savedUser, UserDTO.class), CraftConstants.SUCCESS_MESSAGE);
        } catch(Exception e) {
            logger.error("UserServiceImpl.loginUser => " + CraftConstants.EXCEPTION_OCCURRED_MESSAGE, e);
            return new GenericResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, null,  CraftConstants.FAILURE_MESSAGE);
        }
    }
}
