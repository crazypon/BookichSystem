package com.ilnur.bookich.services;

import com.ilnur.bookich.dtos.UserInfoUpdateDTO;
import com.ilnur.bookich.entities.User;
import com.ilnur.bookich.mappers.UserMapper;
import com.ilnur.bookich.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserContextService userContextService;
    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private User currentUser;

    @BeforeEach
    void setUp() {
        currentUser = new User();
        currentUser.setId(1L);
        currentUser.setUsername("testUser");
        currentUser.setPublicId("175839");
    }


    @Test
    @DisplayName("Get User Info: Should return the currently logged in user")
    void getUserInfo_Success() {
        when(userContextService.getCurrentUser()).thenReturn(currentUser);

        User result = userService.getUserInfo();

        assertNotNull(result);
        assertEquals("testUser", result.getUsername());
        verify(userContextService, times(1)).getCurrentUser();
    }

    @Test
    @DisplayName("Update User: Should map DTO fields and save user")
    void updateUser_Success() {
        UserInfoUpdateDTO updateDTO = new UserInfoUpdateDTO();
        when(userContextService.getCurrentUser()).thenReturn(currentUser);

        userService.updateUser(updateDTO);

        verify(userContextService).getCurrentUser();
        verify(userMapper).updateUserFromDto(updateDTO, currentUser);
        verify(userRepository).save(currentUser);
    }

    @Test
    @DisplayName("Get User Profile: Should return user when Public ID exists")
    void getUserProfile_Success() {

        String publicId = "175839";

        when(userRepository.findByPublicId(publicId)).thenReturn(Optional.of(currentUser));

        User result = userService.getUserProfile(publicId);

        assertNotNull(result);
        assertEquals(currentUser.getId(), result.getId());
    }

    @Test
    @DisplayName("Get User Profile: Should throw 404 when User not found")
    void getUserProfile_NotFound() {
        String nonExistentId = "non-existent-id";
        when(userRepository.findByPublicId(nonExistentId)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> userService.getUserProfile(nonExistentId));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertEquals("No Such User", ex.getReason());
    }
}