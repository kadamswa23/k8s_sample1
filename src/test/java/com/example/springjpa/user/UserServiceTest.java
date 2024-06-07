package com.example.springjpa.user;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
class UserServiceTest {

    @Mock
    UserRepository userRepositoryMock;

    @Mock
    PasswordEncoder passwordEncoder;
    @InjectMocks
    UserService userService;

    private static final String password = "$2a$10$wuk9cNsdHhlcj.dPaqL3w.Ma9oRki./cxb/RZhbEx3/JPA6sjlDbG";
    private static final int PAGE_NUMBER = 2;
    private static final int PAGE_SIZE = 2;
    private static final String SORT_BY = "userId";

    private  static final String USERNAME = "Alex";
    private static final long USERID = 302L;
    List<MyUser> userList = new ArrayList<>();


    MyUser user;

    @BeforeAll
    public static void beforeAll(){

    }

    @Test
    void getUsers() {
        MyUser user1 =  MyUser.builder()
                .userName("Alex").password("Alex123").build();
        MyUser user2 =  MyUser.builder()
                .userName("John").password("Jon134").build();

        userList.add(user1);
        userList.add(user2);

        Pageable pageable = PageRequest.of(PAGE_NUMBER, PAGE_SIZE, Sort.by(SORT_BY));
        Page page = new PageImpl<MyUser>( userList);
        when(userRepositoryMock.findAll(any(Pageable.class))).thenReturn(page);

        List<UserDto> expectedList = userService.getUsers(0,5,"id");
        assertNotNull(expectedList);
        assertEquals(expectedList.size(),userList.size());
    }

    @Test
    void save() {
        UserDto user =  UserDto.builder()
                .userName("Alex").password("Alex123").build();

        MyUser user1 =  MyUser.builder()
                .userName("Alex").password("Alex123").build();

        when(userRepositoryMock.save(any(MyUser.class))).thenReturn(user1);
        when(passwordEncoder.encode(anyString())).thenReturn(password);
        MyUser result = userService.save(user);
        assertNotNull(result);    }

    @Test
    void deleteByNameSuccess() {
        Optional<MyUser> user1 =  Optional.of(MyUser.builder()
                .userName("Alex").password("Alex123").build());
        when(userRepositoryMock.findByUserName(any(String.class))).thenReturn(user1);

        userService.deleteByName("Alex");

        verify(userRepositoryMock, times(1)).findByUserName(USERNAME);
        verify(userRepositoryMock, times(1)).deleteByUserName(USERNAME);
        verifyNoMoreInteractions(userRepositoryMock);

    }

    @Test
    void deleteByNameShouldThrowError() {
        Optional<MyUser> user1 =  Optional.of(MyUser.builder()
                .userName("Alex").password("Alex123").build());
        when(userRepositoryMock.findByUserName(any(String.class))).thenThrow(new EntityNotFoundException("User not found exception"));

        assertThrows(EntityNotFoundException.class, () -> userService.deleteByName("Alex"), "User not found exception");
        verify(userRepositoryMock, times(1)).findByUserName(USERNAME);
        verifyNoMoreInteractions(userRepositoryMock);
    }

    @Test
    void getSingleUserShouldReturnUSerWithValidId(){
        Optional<MyUser> user1 =  Optional.of(MyUser.builder()
                .userName("Alex")
                .password("Alex123")
                .build());
        when(userRepositoryMock.findByUserId(anyLong())).thenReturn(user1);
        UserDto userdto = userService.getUser(USERID);
        assertNotNull(userdto);
        verify(userRepositoryMock, times(1)).findByUserId(USERID);
        verifyNoMoreInteractions(userRepositoryMock);
    }

    @Test
    void getSingleUserShouldReturnError(){
        Optional<MyUser> user1 =  Optional.of(MyUser.builder()
                .userName("Alex").password("Alex123").build());
        when(userRepositoryMock.findByUserId(anyLong())).thenThrow(EntityNotFoundException.class);

        assertThrowsExactly(EntityNotFoundException.class, () -> userService.getUser(USERID));
        verify(userRepositoryMock, times(1)).findByUserId(USERID);
        verifyNoMoreInteractions(userRepositoryMock);
    }

}