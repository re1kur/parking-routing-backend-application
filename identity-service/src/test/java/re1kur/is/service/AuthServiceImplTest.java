package re1kur.is.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
import re1kur.is.service.impl.AuthServiceImpl;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @InjectMocks
    AuthServiceImpl service;

    @Mock
    private UserRepository repo;
    @Mock
    private EventPublisher publisher;
    @Mock
    private UserMapper mapper;
    @Mock
    private CodeService codeService;

    private UUID id;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
    }

    @Test
    void register__ReturnsIdAndDoesNotThrowException() {
        UserPayload payload = UserPayload.builder()
                .phoneNumber("9999999999")
                .email("mail@mail.com")
                .firstName("firstname")
                .lastName("lastname")
                .build();

        User mappedUser = new User();
        mappedUser.setInformation(new UserInformation());

        when(repo.existsByEmail("mail@mail.com")).thenReturn(false);
        when(repo.existsByPhoneNumber("9999999999")).thenReturn(false);
        when(mapper.write(payload)).thenReturn(mappedUser);
        when(repo.save(any(User.class))).thenReturn(User.builder().id(id).build());

        UUID result = assertDoesNotThrow(() -> service.register(payload));
        assertEquals(id, result);

        verify(repo, times(1)).existsByEmail("mail@mail.com");
        verify(repo, times(1)).existsByPhoneNumber("9999999999");
        verify(mapper, times(1)).write(payload);
        verify(repo, times(1)).save(mappedUser);
    }

    @Test
    void register__EmailIsOccupied__ThrowsException() {
        UserPayload payload = UserPayload.builder()
                .phoneNumber("9999999999")
                .email("mail@mail.com")
                .firstName("firstname")
                .lastName("lastname")
                .build();

        User mappedUser = new User();
        mappedUser.setInformation(new UserInformation());

        when(repo.existsByEmail("mail@mail.com")).thenReturn(true);

        assertThrows(EmailAlreadyRegisteredException.class, () -> service.register(payload));

        verify(repo, times(1)).existsByEmail("mail@mail.com");
        verifyNoMoreInteractions(repo);
        verifyNoInteractions(mapper);
    }

    @Test
    void register__PhoneIsOccupied__ThrowsException() {
        UserPayload payload = UserPayload.builder()
                .phoneNumber("9999999999")
                .email("mail@mail.com")
                .firstName("firstname")
                .lastName("lastname")
                .build();

        User mappedUser = new User();
        mappedUser.setInformation(new UserInformation());

        when(repo.existsByEmail("mail@mail.com")).thenReturn(false);
        when(repo.existsByPhoneNumber("9999999999")).thenReturn(true);

        assertThrows(PhoneNumberAlreadyRegisteredException.class, () -> service.register(payload));

        verify(repo, times(1)).existsByEmail("mail@mail.com");
        verify(repo, times(1)).existsByPhoneNumber("9999999999");
        verifyNoMoreInteractions(repo);
        verifyNoInteractions(mapper);
    }

    @Test
    void login__ReturnsCredentials() {
        LoginRequest request = LoginRequest.builder()
                .phoneNumber("9999999999")
                .code("123123")
                .build();
        User expected = User.builder().id(id).build();

        when(repo.findByPhoneNumber("9999999999")).thenReturn(Optional.of(User.builder().id(id).build()));
        doNothing().when(codeService).validateCode(id.toString(), "123123");
        when(mapper.login(User.builder().id(id).build())).thenReturn(mock(Credentials.class));

        assertDoesNotThrow(() -> service.login(request));

        verify(codeService, times(1)).validateCode(id.toString(), request.code());
        verify(mapper, times(1)).login(expected);
    }

    @Test
    void login__UserNotFound__ThrowsException() {
        LoginRequest request = LoginRequest.builder()
                .phoneNumber("9999999999")
                .code("123123")
                .build();

        when(repo.findByPhoneNumber("9999999999")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> service.login(request));

        verify(repo, times(1)).findByPhoneNumber(request.phoneNumber());
        verifyNoInteractions(codeService);
        verifyNoInteractions(mapper);
    }

    @Test
    void login__CodeNotFound__ThrowsException() {
        LoginRequest request = LoginRequest.builder()
                .phoneNumber("9999999999")
                .code("123123")
                .build();

        when(repo.findByPhoneNumber("9999999999")).thenReturn(Optional.of(User.builder().id(id).build()));
        doThrow(CodeNotFoundException.class).when(codeService).validateCode(id.toString(), "123123");

        assertThrows(CodeNotFoundException.class, () -> service.login(request));

        verify(repo, times(1)).findByPhoneNumber(request.phoneNumber());
        verify(codeService, times(1)).validateCode(id.toString(), request.code());
        verifyNoInteractions(mapper);
    }

    @Test
    void login__CodeHasExpired__ThrowsException() {
        LoginRequest request = LoginRequest.builder()
                .phoneNumber("9999999999")
                .code("123123")
                .build();

        when(repo.findByPhoneNumber("9999999999")).thenReturn(Optional.of(User.builder().id(id).build()));
        doThrow(CodeHasExpiredException.class).when(codeService).validateCode(id.toString(), "123123");

        assertThrows(CodeHasExpiredException.class, () -> service.login(request));

        verify(repo, times(1)).findByPhoneNumber(request.phoneNumber());
        verify(codeService, times(1)).validateCode(id.toString(), request.code());
        verifyNoInteractions(mapper);
    }

    @Test
    void login__CodeMismatched__ThrowsException() {
        LoginRequest request = LoginRequest.builder()
                .phoneNumber("9999999999")
                .code("123123")
                .build();

        when(repo.findByPhoneNumber("9999999999")).thenReturn(Optional.of(User.builder().id(id).build()));
        doThrow(CodeMismatchException.class).when(codeService).validateCode(id.toString(), "123123");

        assertThrows(CodeMismatchException.class, () -> service.login(request));

        verify(repo, times(1)).findByPhoneNumber(request.phoneNumber());
        verify(codeService, times(1)).validateCode(id.toString(), request.code());
        verifyNoInteractions(mapper);
    }

    @Test
    void generateCode__DoesNotThrowsException() {
        GenerateCodeRequest request = new GenerateCodeRequest("9999999999");
        String value = "value";

        User expected = User.builder().id(id).build();
        when(repo.findByPhoneNumber("9999999999")).thenReturn(Optional.of(User.builder().id(id).build()));
        when(codeService.generateCode(id.toString())).thenReturn("value");
        doNothing().when(publisher).verificationCode(User.builder().id(id).build(), "value");

        assertDoesNotThrow(() -> service.generateCode(request));

        verify(repo, times(1)).findByPhoneNumber(request.phoneNumber());
        verify(codeService, times(1)).generateCode(id.toString());
        verify(publisher, times(1)).verificationCode(expected, value);
    }

    @Test
    void generateCode__UserNotFound__ThrowsException() {
        GenerateCodeRequest request = new GenerateCodeRequest("9999999999");

        when(repo.findByPhoneNumber("9999999999")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> service.generateCode(request));

        verify(repo, times(1)).findByPhoneNumber(request.phoneNumber());
        verifyNoInteractions(codeService);
        verifyNoInteractions(publisher);
    }
}