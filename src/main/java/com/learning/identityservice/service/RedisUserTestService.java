package com.learning.identityservice.service;

import com.learning.identityservice.entity.RedisUserTest;
import com.learning.identityservice.exception.InvalidDataException;
import com.learning.identityservice.repository.RedisUserTestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * The type Redis user test service.
 */
@Service
@RequiredArgsConstructor
public class RedisUserTestService {
    private final RedisUserTestRepository redisUserTestRepository;

    /**
     * Save redis user test.
     *
     * @param redisUserTest the redis user test
     * @return the redis user test
     */
    public RedisUserTest save(RedisUserTest redisUserTest){
        return redisUserTestRepository.save(redisUserTest);
    }

    /**
     * Delete.
     *
     * @param id the id
     */
    public void delete(String id){
        redisUserTestRepository.deleteById(id);
    }

    /**
     * Update redis user test.
     *
     * @param id            the id
     * @param redisUserTest the redis user test
     * @return the redis user test
     */
    public RedisUserTest update(String id, RedisUserTest redisUserTest){
        if(isExisted(id)){
            return redisUserTestRepository.save(redisUserTest);
        }
        return null;
    }

    /**
     * Is existed boolean.
     *
     * @param id the id
     * @return the boolean
     */
    public boolean isExisted(String id){
        if(!redisUserTestRepository.existsById(id)){
            throw new InvalidDataException("Redis User Test not existed");
        }
        return true;
    }

    /**
     * Find all iterable.
     *
     * @return the iterable
     */
    public Iterable<RedisUserTest> findAll(){
        return redisUserTestRepository.findAll();
    }

    /**
     * Find by id redis user test.
     *
     * @param id the id
     * @return the redis user test
     */
    public RedisUserTest findById(String id){
        return redisUserTestRepository.findById(id)
                .orElseThrow(() -> new InvalidDataException("Redis User Test not existed"));
    }
}
