package com.example.springjpa;

import com.example.springjpa.login.JwtResponseDto;
import com.example.springjpa.user.UserDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import static com.example.springjpa.URLConstants.AUTH_URL;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BaseControllerTest {

    @LocalServerPort
    private int port;
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
    }

    protected String createURLWithPort(String API_URL) {
        return URLConstants.BASE_URL + port +API_URL;
    }

    public String getValidToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        UserDto userDto = UserDto.builder()
                .userName("Valeria")
                .password("VAleria123")
                .build();

        HttpEntity<UserDto> validEntity = new HttpEntity<>(userDto, headers);

        ResponseEntity<JwtResponseDto> response = this.testRestTemplate.postForEntity(createURLWithPort(AUTH_URL), validEntity, JwtResponseDto.class);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        System.out.print("===================="+response.getBody().getJwt());
        String token = response.getBody().getJwt();
        return token;
    }



}
