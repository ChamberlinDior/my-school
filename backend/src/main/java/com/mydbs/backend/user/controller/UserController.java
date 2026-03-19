package com.mydbs.backend.user.controller;

import com.mydbs.backend.common.response.ApiResponse;
import com.mydbs.backend.user.dto.UserCreateRequest;
import com.mydbs.backend.user.dto.UserResponse;
import com.mydbs.backend.user.dto.UserUpdateRequest;
import com.mydbs.backend.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ApiResponse<UserResponse> createUser(@Valid @RequestBody UserCreateRequest request) {
        return ApiResponse.success("Utilisateur cree avec succes", userService.createUser(request));
    }

    @GetMapping
    public ApiResponse<List<UserResponse>> getAllUsers() {
        return ApiResponse.success("Liste des utilisateurs recuperee avec succes", userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ApiResponse<UserResponse> getUserById(@PathVariable Long id) {
        return ApiResponse.success("Utilisateur recupere avec succes", userService.getUserById(id));
    }

    @GetMapping("/search")
    public ApiResponse<List<UserResponse>> searchUsers(@RequestParam(required = false) String keyword) {
        return ApiResponse.success("Resultat de recherche recupere avec succes", userService.searchUsers(keyword));
    }

    @PutMapping("/{id}")
    public ApiResponse<UserResponse> updateUser(@PathVariable Long id,
                                                @Valid @RequestBody UserUpdateRequest request) {
        return ApiResponse.success("Utilisateur mis a jour avec succes", userService.updateUser(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ApiResponse.success("Utilisateur supprime logiquement avec succes", null);
    }
}