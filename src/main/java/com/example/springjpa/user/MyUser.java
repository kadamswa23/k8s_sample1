package com.example.springjpa.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name="users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long userId;

    @Column(nullable = false, unique = true)
    private String userName;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(name="created_at")
    private Date createdAt;
    @Column(name="modified_at")
    private Date updatedAt;
}
