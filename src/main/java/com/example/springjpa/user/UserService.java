package com.example.springjpa.user;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private static Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserRepository userRepository;

    public List<UserDto> getUsers(int pageNumber, int pageSize, String sortBy){
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        Page<MyUser> userPage = userRepository.findAll(pageable);
        List<MyUser> usersList = userPage.getContent();
        return  usersList.stream()
                        .map( user -> {
                            UserDto userDto = UserDto.builder()
                                    .userName(user.getUserName())
                                    .email(user.getEmail())
                                    .build();
                                    return userDto;
                                })
                        .collect(Collectors.toList());
    }

    public MyUser save(UserDto userDto) {
        try{
            MyUser user = MyUser.builder()
                    .email(userDto.getEmail())
                    .userName(userDto.getUserName())
                    .password(passwordEncoder.encode(userDto.getPassword()))
                    .build();
            return userRepository.save(user);
        }catch (Exception ex){
            log.error(ex.getMessage());
            throw ex;
        }
    }
    public void deleteByName(String name) {
        userRepository.findByUserName(name).orElseThrow( () -> new EntityNotFoundException("User not found exception"));
        try{
            userRepository.deleteByUserName(name);
        }catch (Exception ex){
            log.error("User can not be deleted "+ex.getMessage());
            throw ex;
        }
    }

    public UserDto getUser(long userId) {
        MyUser user = userRepository.findByUserId(userId).orElseThrow( () -> new EntityNotFoundException());
        return  UserDto.builder()
                .email(user.getEmail())
                .userName(user.getUserName())
                .password(passwordEncoder.encode(user.getPassword()))
                .build();
    }
}
