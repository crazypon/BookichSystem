package com.ilnur.bookich.services;

import com.ilnur.bookich.dtos.UserInfoUpdateDTO;
import com.ilnur.bookich.entities.User;
import com.ilnur.bookich.mappers.UserMapper;
import com.ilnur.bookich.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserContextService userContextService;
    private final UserMapper userMapper;

    public User getUserInfo() {
        return userContextService.getCurrentUser();
    }

    public void updateUser(UserInfoUpdateDTO userInfo) {
        User user = userContextService.getCurrentUser();
        // this method maps the fields which are not null to new ones
        userMapper.updateUserFromDto(userInfo, user);
        userRepository.save(user);
    }

    public User getUserProfile(String userId) {
        return userRepository.findByPublicId(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No Such User")
        );
    }
}
