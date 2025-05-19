package com.expense.auth.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.expense.auth.entities.RefreshToken;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {
     Optional<RefreshToken> findByToken(String token);

}