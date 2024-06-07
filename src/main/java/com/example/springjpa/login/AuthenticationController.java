package com.example.springjpa.login;

import com.example.springjpa.security.JwtService;
import com.example.springjpa.user.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/token")
public class AuthenticationController{
    @Autowired
    JwtService jwtService;
    @Autowired
    AuthenticationManager authenticationManager;
    public static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
    @PostMapping
    @Operation(summary = "Issue JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation sucessfull"),
            @ApiResponse(responseCode = "401", description = "User not authorised to perform this action")

    })
    public ResponseEntity<Long> getToken(@RequestBody UserDto userDto){
        JwtResponseDto jwtResponse;
        try{
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getUserName(), userDto.getPassword()));
            if(authentication.isAuthenticated()){

                jwtResponse = JwtResponseDto.builder()
                        .jwt(jwtService.generateToken(userDto.getUserName()))
                        .build();
                return new ResponseEntity(jwtResponse, HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        }catch (BadCredentialsException  | UsernameNotFoundException ex ){
            logger.error("Error getting token"+ex.getStackTrace());
            throw ex;
        }

    }
}
