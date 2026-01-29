package com.ilnur.bookich.controllers;

import com.ilnur.bookich.dtos.UserInfoUpdateDTO;
import com.ilnur.bookich.entities.User;
import com.ilnur.bookich.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<User> getMe() {
        User user = userService.getUserInfo();
        return ResponseEntity.ok(user);
    }

    @PostMapping("/me")
    public ResponseEntity<String> updateUser(@Valid @RequestBody UserInfoUpdateDTO updateInfo) {
        userService.updateUser(updateInfo);
        return ResponseEntity.ok("User Information Updated Successfully");
    }

    @GetMapping("/{publicId}")
    public ResponseEntity<User> showUsersProfile(@PathVariable String publicId) {
        User user = userService.getUserProfile(publicId);
        return ResponseEntity.ok(user);
    };
}
