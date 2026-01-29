package com.ilnur.bookich.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ilnur.bookich.dtos.UserInfoUpdateDTO;
import com.ilnur.bookich.entities.User;
import com.ilnur.bookich.services.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @WithMockUser
    @DisplayName("GET /me: Should return current user info")
    void getMe_Success() throws Exception {
        User mockUser = new User();
        given(userService.getUserInfo()).willReturn(mockUser);

        mockMvc.perform(get("/api/users/me"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser
    @DisplayName("POST /me: Should update user and return success message")
    void updateUser_Success() throws Exception {
        UserInfoUpdateDTO updateDto = new UserInfoUpdateDTO();

        mockMvc.perform(post("/api/users/me")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("User Information Updated Successfully"));

        verify(userService).updateUser(any(UserInfoUpdateDTO.class));
    }

    @Test
    @WithMockUser
    @DisplayName("GET /{publicId}: Should return user profile")
    void showUsersProfile_Success() throws Exception {
        String publicId = "user-123";
        User mockUser = new User();
        given(userService.getUserProfile(publicId)).willReturn(mockUser);

        mockMvc.perform(get("/api/users/{publicId}", publicId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}