package ru.hogwarts.school.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.service.AvatarService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@RestController
public class AvatarController {
    private final AvatarService avatarService;

    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    @PostMapping(value = "/{studentId}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAvatar(@PathVariable Long studentId,
                                               @RequestParam MultipartFile avatar) throws IOException {
        avatarService.uploadAvatar(studentId, avatar);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/avatar-from-bd/")
    public ResponseEntity<byte[]> downloadAvatar(@PathVariable Long id) {
        if (avatarService.findAvatarById(id).isEmpty())
            return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).build();
        Avatar avatar = avatarService.findAvatarById(id).get();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(avatar.getMediaType()));
        headers.setContentLength(avatar.getData().length);
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(avatar.getData());
    }

    @GetMapping("/{id}/avatar-from-file/")
    public void downloadAvatar(@PathVariable Long id, HttpServletResponse response) throws IOException {
        Avatar avatar = avatarService.findAvatarById(id).get();
        Path path = Path.of(avatar.getFilePath());
        try (
                InputStream is = Files.newInputStream(path);
                OutputStream os = response.getOutputStream();
        ) {
            response.setStatus(200);
            response.setContentType(avatar.getMediaType());
            response.setContentLength(avatar.getData().length);
            is.transferTo(os);
        }
    }

    @GetMapping("/by-page")
    public List<Avatar> findAll(@RequestParam Integer pageNumber, @RequestParam Integer pageSize) {
        return avatarService.findAll(pageNumber, pageSize);
    }

//    @GetMapping("/by-page")
//    public void findAll(@RequestParam Integer pageNumber, @RequestParam Integer pageSize, HttpServletResponse response) throws IOException {
//        List<Avatar> findedAvatars = avatarService.findAll(pageNumber, pageSize);
//        for (Avatar avatar : findedAvatars) {
//            Path path = Path.of(avatar.getFilePath());
//            try (
//                    InputStream is = Files.newInputStream(path);
//                    OutputStream os = response.getOutputStream();
//            ) {
//                response.setStatus(200);
//                response.setContentType(avatar.getMediaType());
//                response.setContentLength(avatar.getData().length);
//                is.transferTo(os);
//            }
//        }
//    }
}
