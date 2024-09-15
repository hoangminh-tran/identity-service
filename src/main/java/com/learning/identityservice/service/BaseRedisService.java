package com.learning.identityservice.service;


import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * BaseRedisService provides an abstraction for interacting with Redis using both key-value pairs and hash fields.
 *
 * @param <K> the type parameter representing the key
 * @param <F> the type parameter representing the field in a hash
 * @param <V> the type parameter representing the value
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class BaseRedisService<K, F, V> {
    private final RedisTemplate<K, V> redisTemplate;

    private HashOperations<K, F, V> hashOperations;

    @PostConstruct
    public void init() {
        hashOperations = redisTemplate.opsForHash();
    }

    /**
     * Stores a value associated with the specified key in Redis.
     *
     * @param key   the key to associate with the value
     * @param value the value to store
     */
    public void setValue(K key, V value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * Sets the expiration time (TTL) for the given key in Redis.
     *
     * @param key           the key to set the expiration for
     * @param timeoutInDays the expiration time in days
     */
    public void setKeyExpiration(K key, long timeoutInDays) {
        redisTemplate.expire(key, timeoutInDays, TimeUnit.DAYS);
    }

    /**
     * Adds a value to a Redis hash under the specified key and field.
     *
     * @param key   the Redis key
     * @param field the field within the hash
     * @param value the value to associate with the field
     */
    public void putHashFields(K key, F field, V value) {
        hashOperations.put(key, field, value);
    }

    /**
     * Checks whether a specific field exists in a Redis hash.
     *
     * @param key   the Redis key
     * @param field the field to check for existence
     * @return {@code true} if the field exists, {@code false} otherwise
     */
    public boolean doesHashFieldExist(K key, F field) {
        return hashOperations.hasKey(key, field);
    }

    /**
     * Retrieves the value associated with the specified key from Redis.
     *
     * @param key the Redis key
     * @return the value associated with the key, or {@code null} if none exists
     */
    public V getValue(K key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * Retrieves all fields and values from a Redis hash for the given key.
     *
     * @param key the Redis key
     * @return a map containing all fields and values in the hash
     */
    public Map<F, V> getHashFields(K key) {
        return hashOperations.entries(key);
    }

    /**
     * Retrieves a specific value from a Redis hash for the given key and field.
     *
     * @param key   the Redis key
     * @param field the field within the hash
     * @return the value associated with the specified field, or {@code null} if none exists
     */
    public V getHashField(K key, F field) {
        return hashOperations.get(key, field);
    }

    /**
     * Retrieves all values from a Redis hash where the field starts with the specified prefix.
     *
     * @param key         the Redis key
     * @param fieldPrefix the prefix to match field names against
     * @return a list of values where the field name starts with the specified prefix
     */
    public List<V> getHashValueByPrefix(K key, F fieldPrefix) {
        List<V> values = new ArrayList<>();
        Map<F, V> hashEntries = hashOperations.entries(key);

        for (Map.Entry<F, V> entry : hashEntries.entrySet()) {
            if (entry.getKey().toString().startsWith(fieldPrefix.toString())) {
                values.add(entry.getValue());
            }
        }
        return values;
    }

    /**
     * Retrieves all field names (keys) from a Redis hash for the given key.
     *
     * @param key the Redis key
     * @return a set of field names within the hash
     */
    public Set<F> getHashFieldKeys(K key) {
        return hashOperations.entries(key).keySet();
    }

    /**
     * Deletes the specified key and its associated value from Redis.
     *
     * @param key the Redis key to delete
     */
    public void deleteKey(K key) {
        redisTemplate.delete(key);
    }

    /**
     * Deletes a specific field from a Redis hash for the given key.
     *
     * @param key   the Redis key
     * @param field the field to delete within the hash
     */
    public void deleteHashField(K key, F field) {
        hashOperations.delete(key, field);
    }

    /**
     * Deletes multiple fields from a Redis hash for the given key.
     *
     * @param key    the Redis key
     * @param fields the list of fields to delete within the hash
     */
    public void deleteHashFields(K key, List<F> fields) {
        for (F field : fields) {
            deleteHashField(key, field);
        }
    }
}
