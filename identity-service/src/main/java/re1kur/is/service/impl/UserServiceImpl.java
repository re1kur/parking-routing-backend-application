package re1kur.is.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import re1kur.core.dto.UserDto;
import re1kur.core.exception.UserNotFoundException;
import re1kur.core.payload.UserInformationPayload;
import re1kur.is.entity.User;
import re1kur.is.entity.UserInformation;
import re1kur.is.mapper.UserMapper;
import re1kur.is.repository.sql.UserInformationRepository;
import re1kur.is.repository.sql.UserRepository;
import re1kur.is.service.UserService;

import java.util.UUID;

import static re1kur.core.other.JwtExtractor.extractSubFromJwt;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserInformationRepository infoRepo;
    private final UserRepository userRepo;
    private final UserMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public UserDto getPersonalInfo(String token) {
        String sub = extractSubFromJwt(token);
        log.info("Received 'get info' request by ID: {}", sub);

        User found = userRepo.findById(UUID.fromString(sub))
                .orElseThrow(() ->
                new UserNotFoundException("User %s not found.".formatted(sub)));

        return mapper.read(found);
    }

    @Override
    public UserDto updateUserInfo(UserInformationPayload payload, String authHeader) {
        String subject = extractSubFromJwt(authHeader);
        User user = userRepo.findById(UUID.fromString(subject)).orElseThrow(
                () -> new UserNotFoundException("User %s not found.".formatted(subject)));
        UserInformation information = user.getInformation();
        if (information == null) {
            information = UserInformation.builder()
                    .userId(user.getId())
                    .build();
            user.setInformation(information);
        }
        information.setFirstName(payload.firstName());
        information.setLastName(payload.lastName());
        if (payload.middleName() != null) information.setMiddleName(payload.middleName());
        infoRepo.save(information);
        return mapper.read(user);
    }

    @Override
    public Boolean existsById(UUID id) {
        return userRepo.existsById(id);
    }
}
