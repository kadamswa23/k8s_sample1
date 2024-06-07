package com.example.springjpa.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<MyUser, Long> {

    public Optional<MyUser> findByUserName(String userName);

    public void deleteByUserName(String userName);

    Optional<MyUser> findByUserId(long userId);
}
