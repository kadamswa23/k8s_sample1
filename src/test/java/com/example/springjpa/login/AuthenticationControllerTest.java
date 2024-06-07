package com.example.springjpa.login;

import com.example.springjpa.BaseControllerTest;
import com.example.springjpa.URLConstants;
import com.example.springjpa.user.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.Serializers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.User;

import static com.example.springjpa.URLConstants.AUTH_URL;
import static org.junit.jupiter.api.Assertions.*;

class AuthenticationControllerTest extends BaseControllerTest {


    @Autowired
    TestRestTemplate testRestTemplate;

    private static HttpHeaders headers;
    private static HttpEntity<UserDto> validEntity;
    private static HttpEntity<UserDto> invalidEntity;

    @BeforeAll
    public static void init() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        UserDto userDto = UserDto.builder()
                .userName("Valeria")
                .password("VAleria123")
                .build();

        validEntity = new HttpEntity<>(userDto, headers);

        UserDto nonExistingUser = UserDto.builder()
                .userName("Test")
                .password("VAleria123")
                .build();

        invalidEntity = new HttpEntity<>(nonExistingUser, headers );

    }
/*
    @Test
    public void getSuccesswithValidToken() {
        ResponseEntity<JwtResponseDto> response = this.testRestTemplate.postForEntity(createURLWithPort(AUTH_URL), validEntity, JwtResponseDto.class);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        System.out.print("===================="+response.getBody().getJwt());
    }

    @Test
    void getTokenWithInvalidUser() {
        ResponseEntity<JwtResponseDto> response = this.testRestTemplate.postForEntity(createURLWithPort(AUTH_URL), invalidEntity, JwtResponseDto.class);
        assertEquals(response.getStatusCode(), HttpStatus.UNAUTHORIZED);
    } */
}