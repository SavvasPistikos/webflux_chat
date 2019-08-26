package com.webflux.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.webflux.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
}
