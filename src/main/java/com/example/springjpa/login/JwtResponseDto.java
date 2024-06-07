package com.example.springjpa.login;

import lombok.*;
import org.springframework.stereotype.Service;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponseDto {

    String jwt;
}
