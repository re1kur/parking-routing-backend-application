package re1kur.fs.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import re1kur.core.dto.FileDto;
import re1kur.fs.service.FileService;

import java.io.IOException;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {
    private final FileService service;

    @PostMapping("/upload")
    public ResponseEntity<FileDto> uploadFile(@RequestParam("file") MultipartFile payload) throws IOException {
        FileDto body = service.upload(payload);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<String> downloadFile(@PathVariable("id") String id) {
        return ResponseEntity.status(HttpStatus.FOUND).body(service.getUrl(id));
    }
}
