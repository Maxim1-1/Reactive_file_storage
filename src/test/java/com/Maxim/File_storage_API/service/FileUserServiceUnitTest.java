package com.Maxim.File_storage_API.service;

import com.Maxim.File_storage_API.dto.HistoryDTO;
import com.Maxim.File_storage_API.entity.EventEntity;
import com.Maxim.File_storage_API.entity.FileEntity;
import com.Maxim.File_storage_API.entity.UserEntity;
import com.Maxim.File_storage_API.repository.EventRepository;
import com.Maxim.File_storage_API.repository.file_storage.S3RepositoryImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collection;
import java.util.Collections;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class FileUserServiceUnitTest {
    @Mock
    S3RepositoryImpl s3Async;

    @Mock
    EventRepository eventRepository;

    @Mock
    FileService fileService;

    @Mock
    UserService userService;

    @InjectMocks
    FileUserService fileUserService;


    @Test
    void getAllFilesForRoleUserTest() {
        Integer userId = 1;
        FileEntity file = new FileEntity();
        when(fileService.getFilesByUserId(userId)).thenReturn(Flux.just(file));
        Collection<? extends GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("USER"));
        fileUserService.getAllFilesForRole(authorities, userId);
        verify(fileService).getFilesByUserId(userId);
    }

    @Test
    void getAllFilesForRoleAdminTest() {
        Integer userId = 1;
        FileEntity file = new FileEntity();
        when(fileService.getAllFiles()).thenReturn(Flux.just(file));
        Collection<? extends GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ADMIN"));
        fileUserService.getAllFilesForRole(authorities, userId);
        verify(fileService).getAllFiles();
    }

    @Test
    void getAllFilesForRoleModeratorTest() {
        Integer userId = 1;
        FileEntity file = new FileEntity();
        when(fileService.getAllFiles()).thenReturn(Flux.just(file));
        Collection<? extends GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("MODERATOR"));
        fileUserService.getAllFilesForRole(authorities, userId);
        verify(fileService).getAllFiles();
    }

    @Test
    void getFileByIdForRoleAdmin() {
        Integer userId = 1;
        Integer fileId = 1;
        FileEntity file = new FileEntity();
        when(fileService.getFileById(userId)).thenReturn(Mono.just(file));
        Collection<? extends GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ADMIN"));
        fileUserService.getFileByIdForRole(authorities, userId,fileId);
        verify(fileService).getFileById(fileId);
    }

    @Test
    void getFileByIdForRoleModerator() {
        Integer userId = 1;
        Integer fileId = 1;
        FileEntity file = new FileEntity();
        when(fileService.getFileById(userId)).thenReturn(Mono.just(file));
        Collection<? extends GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("MODERATOR"));
        fileUserService.getFileByIdForRole(authorities, userId,fileId);
        verify(fileService).getFileById(fileId);
    }

    @Test
    void getFileByIdForRoleUser() {
        Integer userId = 1;
        Integer fileId = 1;
        FileEntity file = new FileEntity();
        EventEntity event = new EventEntity();
        when(eventRepository.findByUserIdAndFileId(userId,fileId)).thenReturn(Mono.just(event));
        when(fileService.getFileById(fileId)).thenReturn(Mono.just(file));
        Collection<? extends GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("USER"));

        fileUserService.getFileByIdForRole(authorities, userId, fileId)
                .subscribe(result -> {
                    assertThat(result).isEqualTo(file);
                });

        verify(eventRepository).findByUserIdAndFileId(userId, fileId);
        verify(fileService).getFileById(fileId);
    }

    @Test
    void getHistoryTestByUserReturn200() {
        Integer userId = 1;
        FileEntity file = new FileEntity();

        when(fileService.getFilesByUserId(userId)).thenReturn(Flux.just(file));
        Collection<? extends GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("USER"));

        fileUserService.getHistory(authorities, userId)
                .subscribe(result -> {
                    assertThat(result).isEqualTo(file);
                });
        verify(fileService).getFilesByUserId(userId);

    }

    @Test
    void getHistoryTestByAdminReturn200() {
        Integer userId = 1;
        UserEntity user = new UserEntity();
        FileEntity file = new FileEntity();

        when(userService.findAllUsers()).thenReturn(Flux.just(user));
        when(fileService.getFilesByUserId(userId)).thenReturn(Flux.just(file));

        Collection<? extends GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ADMIN"));

        fileUserService.getHistory(authorities, userId)
                .subscribe(result -> {
                    assertThat(result).isEqualTo(file);
                });

        verify(userService).findAllUsers();
    }
}