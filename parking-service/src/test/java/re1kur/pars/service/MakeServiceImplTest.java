//package re1kur.pars.service;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import re1kur.core.dto.MakeDto;
//import re1kur.core.exception.MakeAlreadyExistsException;
//import re1kur.core.exception.MakeNotFoundException;
//import re1kur.core.payload.MakePayload;
//import re1kur.pars.entity.Make;
//import re1kur.pars.mapper.MakeMapper;
//import re1kur.pars.repository.MakeRepository;
//import re1kur.pars.service.make.impl.MakeServiceImpl;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class MakeServiceImplTest {
//    @InjectMocks
//    MakeServiceImpl service;
//
//    @Mock
//    MakeRepository repo;
//
//    @Mock
//    MakeMapper mapper;
//
//    @Test
//    void create__ReturnsMakeDto() {
//        MakePayload payload = MakePayload.builder()
//                .name("make")
//                .build();
//        MakeDto expected = MakeDto.builder()
//                .name("make")
//                .build();
//        Make mapped = Make.builder().name("make").build();
//        Make saved = Make.builder()
//                .id(1)
//                .name("make")
//                .build();
//
//
//        when(repo.existsByName(payload.name())).thenReturn(false);
//        when(mapper.write(payload)).thenReturn(Make.builder()
//                .name("make")
//                .build());
//        when(repo.save(Make.builder().name("make").build()))
//                .thenReturn(Make.builder()
//                        .id(1)
//                        .name("make")
//                        .build());
//        when(mapper.read(Make.builder()
//                .id(1)
//                .name("make")
//                .build())).thenReturn(MakeDto.builder()
//                .name("make")
//                .build());
//
//        MakeDto result = assertDoesNotThrow(() -> service.create(payload, bearer));
//        assertEquals(result, expected);
//
//        verify(repo, times(1)).existsByName(payload.name());
//        verify(mapper, times(1)).write(payload);
//        verify(repo, times(1)).save(mapped);
//        verify(mapper, times(1)).read(saved);
//    }
//
//    @Test
//    void create__MakeAlreadyExists__ThrowsException() {
//        MakePayload payload = MakePayload.builder()
//                .name("make")
//                .build();
//
//        when(repo.existsByName(payload.name())).thenReturn(true);
//
//        assertThrows(MakeAlreadyExistsException.class, () -> service.create(payload, bearer));
//
//        verify(repo, times(1)).existsByName(payload.name());
//        verifyNoMoreInteractions(repo);
//        verifyNoInteractions(mapper);
//    }
//
//    @Test
//    void get__ReturnsMakeDto() {
//        Integer makeId = 1;
//        Make found = Make.builder()
//                .id(1)
//                .name("name")
//                .build();
//        MakeDto expected = MakeDto.builder()
//                .id(1)
//                .name("name")
//                .build();
//
//        when(repo.findById(1)).thenReturn(Optional.of(Make.builder()
//                .id(1)
//                .name("name")
//                .build()));
//        when(mapper.read(Make.builder()
//                .id(1)
//                .name("name")
//                .build())).thenReturn(MakeDto.builder()
//                .id(1)
//                .name("name")
//                .build());
//
//        MakeDto result = assertDoesNotThrow(() -> service.get(makeId));
//        assertEquals(result, expected);
//
//        verify(repo, times(1)).findById(makeId);
//        verify(mapper, times(1)).read(found);
//    }
//
//    @Test
//    void get__MakeNotFound__ThrowsException() {
//        Integer makeId = 1;
//
//        when(repo.findById(1)).thenReturn(Optional.empty());
//
//        assertThrows(MakeNotFoundException.class, () -> service.get(makeId));
//
//        verify(repo, times(1)).findById(makeId);
//        verifyNoInteractions(mapper);
//    }
//
//    @Test
//    void update__ReturnsMakeDto() {
//        Integer makeId = 1;
//        MakePayload payload = MakePayload.builder()
//                .name("newName")
//                .build();
//
//        Make found = Make.builder()
//                .id(1)
//                .name("oldName")
//                .build();
//
//        Make updated = Make.builder()
//                .id(1)
//                .name("newName")
//                .build();
//
//        MakeDto expected = MakeDto.builder()
//                .id(1)
//                .name("newName")
//                .build();
//
//        when(repo.findById(makeId)).thenReturn(Optional.of(found));
//        when(repo.existsByName(payload.name())).thenReturn(false);
//        when(mapper.update(found, payload)).thenReturn(updated);
//        when(repo.save(updated)).thenReturn(updated);
//        when(mapper.read(updated)).thenReturn(expected);
//
//        MakeDto result = assertDoesNotThrow(() -> service.update(payload, makeId, bearer));
//
//        assertEquals(expected, result);
//
//        verify(repo, times(1)).findById(makeId);
//        verify(repo, times(1)).existsByName(payload.name());
//        verify(mapper, times(1)).update(found, payload);
//        verify(repo, times(1)).save(updated);
//        verify(mapper, times(1)).read(updated);
//    }
//
//    @Test
//    void update__NameUnchanged__SkipsExistsCheck() {
//        Integer makeId = 1;
//        MakePayload payload = MakePayload.builder()
//                .name("sameName")
//                .build();
//
//        Make found = Make.builder()
//                .id(1)
//                .name("sameName")
//                .build();
//
//        Make updated = Make.builder()
//                .id(1)
//                .name("sameName")
//                .build();
//
//        MakeDto expected = MakeDto.builder()
//                .id(1)
//                .name("sameName")
//                .build();
//
//        when(repo.findById(makeId)).thenReturn(Optional.of(found));
//        when(mapper.update(found, payload)).thenReturn(updated);
//        when(repo.save(updated)).thenReturn(updated);
//        when(mapper.read(updated)).thenReturn(expected);
//
//        MakeDto result = assertDoesNotThrow(() -> service.update(payload, makeId, bearer));
//
//        assertEquals(expected, result);
//
//        verify(repo, times(1)).findById(makeId);
//        verify(repo, never()).existsByName(anyString());
//        verify(mapper, times(1)).update(found, payload);
//        verify(repo, times(1)).save(updated);
//        verify(mapper, times(1)).read(updated);
//    }
//
//    @Test
//    void update__MakeNotFound__ThrowsException() {
//        Integer makeId = 1;
//        MakePayload payload = MakePayload.builder()
//                .name("newName")
//                .build();
//
//        when(repo.findById(makeId)).thenReturn(Optional.empty());
//
//        assertThrows(MakeNotFoundException.class, () -> service.update(payload, makeId, bearer));
//
//        verify(repo, times(1)).findById(makeId);
//        verifyNoMoreInteractions(repo);
//        verifyNoInteractions(mapper);
//    }
//
//    @Test
//    void update__NameAlreadyExists__ThrowsException() {
//        Integer makeId = 1;
//        MakePayload payload = MakePayload.builder()
//                .name("newName")
//                .build();
//
//        Make found = Make.builder()
//                .id(1)
//                .name("oldName")
//                .build();
//
//        when(repo.findById(makeId)).thenReturn(Optional.of(found));
//        when(repo.existsByName(payload.name())).thenReturn(true);
//
//        assertThrows(MakeAlreadyExistsException.class, () -> service.update(payload, makeId, bearer));
//
//        verify(repo, times(1)).findById(makeId);
//        verify(repo, times(1)).existsByName(payload.name());
//        verifyNoMoreInteractions(repo);
//        verifyNoInteractions(mapper);
//    }
//
//    @Test
//    void delete__DeletesMake() {
//        Integer makeId = 1;
//        Make found = Make.builder()
//                .id(1)
//                .name("toDelete")
//                .build();
//
//        when(repo.findById(makeId)).thenReturn(Optional.of(found));
//
//        assertDoesNotThrow(() -> service.delete(makeId, bearer));
//
//        verify(repo, times(1)).findById(makeId);
//        verify(repo, times(1)).delete(found);
//    }
//
//    @Test
//    void delete__MakeNotFound__ThrowsException() {
//        Integer makeId = 1;
//
//        when(repo.findById(makeId)).thenReturn(Optional.empty());
//
//        assertThrows(MakeNotFoundException.class, () -> service.delete(makeId, bearer));
//
//        verify(repo, times(1)).findById(makeId);
//        verify(repo, never()).delete(any());
//    }
//}