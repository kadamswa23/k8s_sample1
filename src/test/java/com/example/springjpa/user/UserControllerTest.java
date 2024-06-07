package com.example.springjpa.user;

import com.example.springjpa.BaseControllerTest;
import com.example.springjpa.URLConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
class UserControllerTest extends BaseControllerTest {

    @Autowired
    TestRestTemplate testRestTemplate;

    private static HttpHeaders headers;

    @BeforeAll
    public static void init() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }


  /*  @Test
    void shouldReturnForbiddenException_whenNoHeaders(){
        ResponseEntity<UserDto[]> response = this.testRestTemplate.getForEntity(createURLWithPort(URLConstants.USER_GET_URL), UserDto[].class);
        assertEquals(response.getStatusCode(), HttpStatus.FORBIDDEN);
    }

    @Test
    void shouldReturnUsersWhenValidJwtToken(){
        String jwt = getValidToken();
        headers.setBearerAuth(jwt);

        HttpEntity<String> httpEntity = new HttpEntity(headers);

        ResponseEntity<UserDto[]> response = this.testRestTemplate.exchange(createURLWithPort(URLConstants.USER_GET_URL), HttpMethod.GET, httpEntity, UserDto[].class);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assert(Arrays.stream(response.getBody()).count() >= 1);
    }
*/
}