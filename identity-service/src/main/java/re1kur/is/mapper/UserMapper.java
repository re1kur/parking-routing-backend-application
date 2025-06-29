package re1kur.is.mapper;

import re1kur.core.dto.Credentials;
import re1kur.core.dto.UserDto;
import re1kur.core.payload.UserPayload;
import re1kur.is.entity.User;

public interface UserMapper {
    User write(UserPayload payload);

    UserDto read(User user);

    Credentials login(User user);
}
