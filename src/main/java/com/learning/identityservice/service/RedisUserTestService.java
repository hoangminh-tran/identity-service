package com.learning.identityservice.service;

import com.learning.identityservice.entity.RedisUserTest;
import com.learning.identityservice.exception.InvalidDataException;
import com.learning.identityservice.repository.RedisUserTestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisUserTestService {
    private final RedisUserTestRepository redisUserTestRepository;

    public RedisUserTest save(RedisUserTest redisUserTest){
        return redisUserTestRepository.save(redisUserTest);
    }

    public void delete(String id){
        redisUserTestRepository.deleteById(id);
    }

    public RedisUserTest update(String id, RedisUserTest redisUserTest){
        if(isExisted(id)){
            return redisUserTestRepository.save(redisUserTest);
        }
        return null;
    }

    public boolean isExisted(String id){
        if(!redisUserTestRepository.existsById(id)){
            throw new InvalidDataException("Redis User Test not existed");
        }
        return true;
    }

    public Iterable<RedisUserTest> findAll(){
        return redisUserTestRepository.findAll();
    }

    public RedisUserTest findById(String id){
        return redisUserTestRepository.findById(id)
                .orElseThrow(() -> new InvalidDataException("Redis User Test not existed"));
    }
}
