package ru.hogwarts.school.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {AvatarService.class})
@ExtendWith(SpringExtension.class)
class AvatarServiceTest {
    @MockBean
    private StudentRepository studentRepository;

    @MockBean
    private AvatarRepository avatarRepository;

    private final AvatarService avatarService;

    @Autowired
    AvatarServiceTest(AvatarService avatarService) {
        this.avatarService = avatarService;
    }


//    @Test
//    void uploadAvatar() throws IOException {
//        Long id = 1L;
//        Student student = new Student();
//        student.setId(id);
//        student.setName("Petr");
//        student.setAge(20);
//        Faculty faculty = new Faculty();
//        faculty.setColor("Red");
//        faculty.setId(id);
//        faculty.setName("First");
//        student.setFaculty(faculty);
//        Avatar avatar = new Avatar();
//        avatar.setId(id);
//        avatar.setFilePath("111");
//        avatar.setFileSize(111L);
//        avatar.setMediaType("jpeg");
//        byte[] data = new byte[] {1, 1, 1};
//        avatar.setData(data);
//        InputStream is = new InputStream() {
//            @Override
//            public int read() throws IOException {
//                return 111;
//            }
//        };
//        MultipartFile file = new MultipartFile() {
//            @Override
//            public String getName() {
//                return "file.jpeg";
//            }
//
//            @Override
//            public String getOriginalFilename() {
//                return "file.jpeg";
//            }
//
//            @Override
//            public String getContentType() {
//                return "jpeg";
//            }
//
//            @Override
//            public boolean isEmpty() {
//                return false;
//            }
//
//            @Override
//            public long getSize() {
//                return 111L;
//            }
//
//            @Override
//            public byte[] getBytes() throws IOException {
//                return data;
//            }
//
//            @Override
//            public InputStream getInputStream() throws IOException {
//                return is;
//            }
//
//            @Override
//            public void transferTo(File dest) throws IOException, IllegalStateException {
//
//            }
//        };
//
//
//        when(studentRepository.findById(id)).thenReturn(Optional.of(student));
//        when(avatarRepository.findByStudentId(id)).thenReturn(Optional.of(avatar));
//
//        avatarService.uploadAvatar(id, file);
//        verify(avatarRepository).save(avatar);
//    }

    @Test
    void findAvatarByIdCorrect() {
        Long id = 1L;
        Avatar avatar = new Avatar();
        avatar.setId(id);
        avatar.setFilePath("111");
        avatar.setFileSize(111L);
        avatar.setMediaType("jpeg");
        byte[] data = new byte[]{1, 1, 1};
        avatar.setData(data);

        when(avatarRepository.findById(id)).thenReturn(Optional.of(avatar));

        assertEquals(Optional.of(avatar), avatarService.findAvatarById(id));
    }

    @Test
    void findAvatarNullById() {
        Long id = 1L;

        when(avatarRepository.findById(id)).thenReturn(Optional.empty());

        assertEquals(Optional.empty(), avatarService.findAvatarById(id));
    }
}