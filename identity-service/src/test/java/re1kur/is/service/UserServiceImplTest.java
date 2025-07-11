package re1kur.is.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import re1kur.core.dto.UserDto;
import re1kur.core.exception.*;
import re1kur.core.other.JwtExtractor;
import re1kur.core.payload.UserInformationPayload;
import re1kur.is.entity.User;
import re1kur.is.entity.UserInformation;
import re1kur.is.mapper.UserMapper;
import re1kur.is.repository.sql.UserInformationRepository;
import re1kur.is.repository.sql.UserRepository;
import re1kur.is.service.impl.UserServiceImpl;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserInformationRepository infoRepo;
    @Mock
    private UserRepository userRepo;
    @Mock
    private UserMapper mapper;

    @InjectMocks
    private UserServiceImpl service;

    private static UUID id;
    private static String token;
    private static MockedStatic<JwtExtractor> mocked;

    @BeforeAll
    static void setUp() {
        id = UUID.randomUUID();
        token = "fake.jwt.token";
        mocked = mockStatic(JwtExtractor.class);
    }

    @Test
    void getPersonalInfo__ReturnsUserDto() {
        User user = new User();
        user.setId(id);

        mocked.when(() -> JwtExtractor.extractSubFromJwt(token)).thenReturn(id.toString());
        when(userRepo.findById(id)).thenReturn(Optional.of(user));
        UserDto dto = new UserDto(null, null, null, null, null);
        when(mapper.read(user)).thenReturn(dto);

        UserDto result = service.getPersonalInfo(token);

        assertEquals(dto, result);
        verify(userRepo, times(1)).findById(id);
        verify(mapper, times(1)).read(user);
    }

    @Test
    void getPersonalInfo__ThrowsUserNotFoundException() {
        mocked.when(() -> JwtExtractor.extractSubFromJwt(token)).thenReturn(id.toString());
        when(userRepo.findById(id)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> service.getPersonalInfo(token));

        verify(userRepo, times(1)).findById(id);
    }

    @Test
    void updateUserInfo__UpdatesExistingInformation() {
        UserInformationPayload payload = new UserInformationPayload("first", "last", "middle");
        UserInformation info = new UserInformation();
        User user = new User();
        user.setId(id);
        user.setInformation(info);

        mocked.when(() -> JwtExtractor.extractSubFromJwt(token)).thenReturn(id.toString());
        when(userRepo.findById(id)).thenReturn(Optional.of(user));
        UserDto dto = new UserDto(null, null, null, null, null);
        when(mapper.read(user)).thenReturn(dto);

        assertDoesNotThrow(() -> service.updateUserInfo(payload, token));

        assertEquals("first", info.getFirstName());
        assertEquals("last", info.getLastName());
        assertEquals("middle", info.getMiddleName());

        verify(infoRepo, times(1)).save(info);
        verify(mapper, times(1)).read(user);
    }

    @Test
    void updateUserInfo__CreatesNewInformationWhenNull() {
        UserInformationPayload payload = new UserInformationPayload("first", "last", null);
        User user = new User();
        user.setId(id);
        user.setInformation(null);

        mocked.when(() -> JwtExtractor.extractSubFromJwt(token)).thenReturn(id.toString());
        when(userRepo.findById(id)).thenReturn(Optional.of(user));
        UserDto dto = new UserDto(null, null, null, null, null);
        when(mapper.read(user)).thenReturn(dto);

        assertDoesNotThrow(() -> service.updateUserInfo(payload, token));

        assertNotNull(user.getInformation());
        assertEquals("first", user.getInformation().getFirstName());
        assertEquals("last", user.getInformation().getLastName());
        assertNull(user.getInformation().getMiddleName());

        verify(infoRepo, times(1)).save(user.getInformation());
        verify(mapper, times(1)).read(user);
    }

    @Test
    void existsById__ReturnsCorrectValue() {
        when(userRepo.existsById(id)).thenReturn(true);
        assertTrue(service.existsById(id));

        when(userRepo.existsById(id)).thenReturn(false);
        assertFalse(service.existsById(id));

        verify(userRepo, times(2)).existsById(id);
    }

}