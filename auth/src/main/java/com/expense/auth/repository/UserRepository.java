package com.expense.auth.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.expense.auth.entities.UserInfo;


@Repository
public interface UserRepository extends CrudRepository<UserInfo, String> {
    public UserInfo findByUsername(String username);
     
}

