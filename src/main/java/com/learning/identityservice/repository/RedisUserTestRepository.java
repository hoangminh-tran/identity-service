package com.learning.identityservice.repository;

import com.learning.identityservice.entity.RedisUserTest;
import com.learning.identityservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RedisUserTestRepository extends CrudRepository<RedisUserTest, String> {
}
