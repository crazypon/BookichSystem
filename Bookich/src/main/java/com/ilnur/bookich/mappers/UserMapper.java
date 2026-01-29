package com.ilnur.bookich.mappers;

import com.ilnur.bookich.dtos.UserInfoUpdateDTO;
import com.ilnur.bookich.dtos.UserRegistrationDTO;
import com.ilnur.bookich.entities.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {
    // if we meet null field, we just ignore it
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    // with MappingTarget we say which object should be updated
    void updateUserFromDto(UserInfoUpdateDTO dto, @MappingTarget User user);

    // no need to map anything since all fields have same name
    User toUser(UserRegistrationDTO regDTO);
}
