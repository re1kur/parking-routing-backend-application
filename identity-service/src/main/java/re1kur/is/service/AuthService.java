package re1kur.is.service;

import re1kur.core.dto.Credentials;
import re1kur.core.exception.EmailAlreadyRegisteredException;
import re1kur.core.exception.UserNotFoundException;
import re1kur.core.payload.GenerateCodeRequest;
import re1kur.core.payload.LoginRequest;
import re1kur.core.payload.UserPayload;

import java.util.UUID;

public interface AuthService {
    UUID register(UserPayload payload) throws EmailAlreadyRegisteredException;

    Credentials login(LoginRequest request) throws UserNotFoundException;

    void generateCode(GenerateCodeRequest request);
}
