package com.example.demo.controller;


import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.exception.EmptyInputException;
import com.example.demo.exception.ErrorMessage;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

@RestController
@RequestMapping("/api/users")
public class UserController {

    Logger logger = LoggerFactory.getLogger(UserController.class);
    
    private boolean deafult_value = true;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping
    public String addUser(@RequestBody User user) {
        user.setActive(deafult_value);
        if(user.getUserName().isEmpty() || user.getPassword().isEmpty() || user.getRoles().isEmpty()) {
            throw new EmptyInputException("Failed while saving the user, input fields are empty!");
        } else {
        String encryptedPwd = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPwd);
        userRepository.save(user);
            logger.info("Saving user");
        return "User " +user.getUserName()+ " added successfully";
        }
    }

    @GetMapping
    public List<User> loadUsers() {
        logger.info("get all users");
        return userRepository.findAll();
    }

    @DeleteMapping("{id}")
    public String deleteUser(@PathVariable("id") int id){
        userRepository.deleteById(id);
        logger.info("user deleted");
        return "User " +id+ " deleted successfully";
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleAnyException(Exception exception, WebRequest request){

        String errorMessageDescription = exception.getLocalizedMessage();

        if(errorMessageDescription == null) errorMessageDescription = exception.toString();

        int errorCode = 500;
        ErrorMessage errorMessage = new ErrorMessage(errorCode, errorMessageDescription);
        
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {EmptyInputException.class})
    public ResponseEntity<Object> handleEmptyInputException(Exception exception, WebRequest request){

        String errorMessageDescription = exception.getLocalizedMessage();

        if(errorMessageDescription == null) errorMessageDescription = exception.toString();

        int errorCode = 406;
        ErrorMessage errorMessage = new ErrorMessage(errorCode, errorMessageDescription);
        
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_ACCEPTABLE);
    }



}
