package com.craft.demo.controller;

import com.craft.demo.models.dtos.GenericResponse;
import com.craft.demo.models.dtos.UserDTO;
import com.craft.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<GenericResponse> registerUser(@RequestBody UserDTO userDTO) {
        GenericResponse<UserDTO> response = userService.registerUser(userDTO);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @PostMapping("/login")
    public ResponseEntity<GenericResponse> loginUser(@RequestBody UserDTO userDTO) {
        GenericResponse<UserDTO> response = userService.loginUser(userDTO);
        return new ResponseEntity<>(response, response.getStatus());
    }
}
