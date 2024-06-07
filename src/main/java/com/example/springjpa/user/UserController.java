package com.example.springjpa.user;


import io.micrometer.common.util.StringUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Tag(name="User API")
@RequestMapping("/api/v1.0")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/users")
    @Operation(summary = "Get all users list by page")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description = "List all users by page",
             content = { @Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", description = "Internal server error")

    })
    public ResponseEntity<List<UserDto>> getUsers(@RequestParam(value="offset", required = false, defaultValue = "0") Integer offset,
                                                  @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                                  @RequestParam(value = "sortBy", required = false ) String sortBy){
        sortBy = StringUtils.isNotBlank(sortBy)? sortBy: "userId";
        List<UserDto> userList = userService.getUsers(offset, pageSize,sortBy);
        return new ResponseEntity(userList, HttpStatus.OK);
    }

    @PostMapping("/register")
    @Operation(summary = "Register user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User created successfully"),
        @ApiResponse(responseCode = "400", description = "Bad request")

    })
    public ResponseEntity<Long> createUser(@RequestBody UserDto userDto){
        MyUser user = userService.save(userDto);
        return new ResponseEntity(user.getUserId(),HttpStatus.CREATED);
    }

    @DeleteMapping("/users/{name}")
    public ResponseEntity deleteUser(@PathVariable(value = "name") String name){
        userService.deleteByName(name);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserDto> getUSer(@PathVariable long userId){
        UserDto user = userService.getUser(userId);
        return  new ResponseEntity<>(user, HttpStatus.OK);
    }
}
