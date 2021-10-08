package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.model.Role;
import com.example.model.User;
import com.example.service.UserService;
import com.example.service.UserServiceImp;

import java.util.*;

@Controller

public class HelloController {

    @GetMapping(value = "/admin")
    public String printAdminPage() {
        return "admin";
    }

    @GetMapping(value = "/user")
    public String printUserPage() {
        return "user";
    }
}
