package re1kur.is.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import re1kur.core.exception.CodeHasExpiredException;
import re1kur.core.exception.CodeMismatchException;
import re1kur.core.exception.CodeNotFoundException;
import re1kur.is.entity.Code;
import re1kur.is.mapper.CodeMapper;
import re1kur.is.repository.cache.CodeRepository;
import re1kur.is.service.impl.CodeGenerator;
import re1kur.is.service.impl.CodeServiceImpl;

import java.lang.reflect.Field;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CodeServiceImplTest {

    @Mock
    private CodeRepository repo;

    @Mock
    private CodeMapper mapper;

    @InjectMocks
    private CodeServiceImpl service;

    private final String id = "some-user-id";
    private final String codeValue = "123456";

    private final Integer cacheTtl = 30;

    private static MockedStatic<CodeGenerator> mockedStatic;

    @BeforeAll
    static void setUp() {
        mockedStatic = Mockito.mockStatic(CodeGenerator.class);
    }

    @BeforeEach
    void setUpEach() throws Exception{
        Field field = CodeServiceImpl.class.getDeclaredField("cacheTtl");
        field.setAccessible(true);
        field.set(service, cacheTtl);
    }

    @Test
    void generateCode__GeneratesAndSavesCode() {
        Code mappedCode = new Code(id, codeValue, Instant.now(), Instant.now().plus(Duration.ofMillis(cacheTtl)));

        mockedStatic.when(CodeGenerator::generateOTP).thenReturn(codeValue);

        when(mapper.write(eq(id), eq(codeValue), anyInt())).thenReturn(mappedCode);

        String result = service.generateCode(id);

        assertEquals(codeValue, result);
        verify(mapper).write(eq(id), eq(codeValue), anyInt());
        verify(repo).save(mappedCode);
    }

    @Test
    void validateCode__ValidCode__DeletesCode() {
        Code code = mock(Code.class);

        when(repo.findById(id)).thenReturn(Optional.of(code));
        when(code.isExpired()).thenReturn(false);
        when(code.isMatches(codeValue)).thenReturn(true);

        assertDoesNotThrow(() -> service.validateCode(id, codeValue));

        verify(repo).findById(id);
        verify(code).isExpired();
        verify(code).isMatches(codeValue);
        verify(repo).delete(code);
    }

    @Test
    void validateCode__CodeNotFound__ThrowsException() {
        when(repo.findById(id)).thenReturn(Optional.empty());

        assertThrows(CodeNotFoundException.class, () -> service.validateCode(id, codeValue));

        verify(repo).findById(id);
        verifyNoMoreInteractions(repo);
    }

    @Test
    void validateCode__CodeExpired__ThrowsException() {
        Code code = mock(Code.class);

        when(repo.findById(id)).thenReturn(Optional.of(code));
        when(code.isExpired()).thenReturn(true);

        assertThrows(CodeHasExpiredException.class, () -> service.validateCode(id, codeValue));

        verify(repo).findById(id);
        verify(code).isExpired();
        verifyNoMoreInteractions(repo);
    }

    @Test
    void validateCode__CodeMismatch__ThrowsException() {
        Code code = mock(Code.class);

        when(repo.findById(id)).thenReturn(Optional.of(code));
        when(code.isExpired()).thenReturn(false);
        when(code.isMatches(codeValue)).thenReturn(false);

        assertThrows(CodeMismatchException.class, () -> service.validateCode(id, codeValue));

        verify(repo).findById(id);
        verify(code).isExpired();
        verify(code).isMatches(codeValue);
        verifyNoMoreInteractions(repo);
    }
}
