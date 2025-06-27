package re1kur.is.mapper.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import re1kur.core.dto.UserDto;
import re1kur.core.dto.UserInformationDto;
import re1kur.core.payload.UserPayload;
import re1kur.is.entity.User;
import re1kur.is.entity.UserInformation;
import re1kur.is.mapper.UserMapper;

@Component
@RequiredArgsConstructor
public class UserMapperImpl implements UserMapper {

    @Override
    public User write(UserPayload payload) {
        return User.builder()
                .email(payload.email())
                .phoneNumber(payload.phoneNumber())
                .build();
    }

    @Override
    public UserDto read(User user) {
        UserInformationDto userInfoDto = null;
        if (user.getInformation() != null) {
            UserInformation info = user.getInformation();
            userInfoDto = UserInformationDto.builder()
                    .firstName(info.getFirstName())
                    .lastName(info.getLastName())
                    .middleName(info.getMiddleName())
                    .build();
        }

        return UserDto.builder()
                .id(user.getId().toString())
                .email(user.getEmail())
                .enabled(user.getEnabled())
                .info(userInfoDto)
                .build();
    }
}
