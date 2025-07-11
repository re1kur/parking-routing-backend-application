package re1kur.is.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import re1kur.core.dto.Credentials;
import re1kur.core.exception.*;
import re1kur.core.payload.GenerateCodeRequest;
import re1kur.core.payload.LoginRequest;
import re1kur.core.payload.UserPayload;
import re1kur.is.entity.User;
import re1kur.is.entity.UserInformation;
import re1kur.is.mapper.UserMapper;
import re1kur.is.mq.EventPublisher;
import re1kur.is.repository.sql.UserRepository;
import re1kur.is.service.AuthService;
import re1kur.is.service.CodeService;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository repo;
    private final EventPublisher publisher;
    private final UserMapper mapper;
    private final CodeService codeService;

    @Override
    @Transactional
    public UUID register(UserPayload payload) {
        if (repo.existsByEmail(payload.email()))
            throw new EmailAlreadyRegisteredException(
                    "User with email %s already registered.".formatted(payload.email()));
        if (repo.existsByPhoneNumber(payload.phoneNumber()))
            throw new PhoneNumberAlreadyRegisteredException(
                    "User with phone number +7%s already registered.".formatted(payload.phoneNumber()));
        User mapped = mapper.write(payload);
        UserInformation info = UserInformation.builder()
                .firstName(payload.firstName())
                .lastName(payload.lastName())
                .middleName(payload.middleName())
                .user(mapped)
                .build();
        mapped.setInformation(info);

        User saved = repo.save(mapped);
        return saved.getId();
    }

    @Override
    public Credentials login(LoginRequest request) {
        User user = repo.findByPhoneNumber(request.phoneNumber()).orElseThrow(
                () -> new UserNotFoundException(
                        "User with phone number +7%s not found.".formatted(request.phoneNumber())));
        codeService.validateCode(user.getId().toString(), request.code());
        return mapper.login(user);
    }

    @Override
    public void generateCode(GenerateCodeRequest request) {
        User user = repo.findByPhoneNumber(request.phoneNumber())
                .orElseThrow(() -> new UserNotFoundException(
                        "User with phone number +7%s not found.".formatted(request.phoneNumber())));
        UUID id = user.getId();
        String value = codeService.generateCode(id.toString());
        publisher.verificationCode(user, value);
    }
}