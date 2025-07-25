package re1kur.is.service;

import re1kur.core.dto.UserDto;
import re1kur.core.payload.UserInformationPayload;

import java.util.UUID;

public interface UserService {

    UserDto getPersonalInfo(String sub);

    UserDto updateUserInfo(UserInformationPayload payload, String subject);

    Boolean existsById(UUID id);
}
