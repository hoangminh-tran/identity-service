package com.learning.identityservice.service;

import com.learning.identityservice.dto.request.UserCreationRequest;
import com.learning.identityservice.entity.RedisUserTest;
import com.learning.identityservice.exception.InvalidDataException;
import com.learning.identityservice.repository.RedisUserTestRepository;
import com.redis.testcontainers.RedisContainer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.LocalDate;
import java.util.Optional;

@SpringBootTest
@Testcontainers
@Slf4j
public class RedisUserTestServiceTest {
    @Container
    static final MySQLContainer<?> MY_SQL_CONTAINER = new MySQLContainer<>("mysql:latest");

    @Container
    static final RedisContainer REDIS_CONTAINER = new RedisContainer(DockerImageName.parse("redis:latest"))
            .withExposedPorts(6379);

    @DynamicPropertySource
    static void configureDataSource(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.datasource.url", MY_SQL_CONTAINER::getJdbcUrl);
        dynamicPropertyRegistry.add("spring.datasource.username", MY_SQL_CONTAINER::getUsername);
        dynamicPropertyRegistry.add("spring.datasource.password", MY_SQL_CONTAINER::getPassword);
        dynamicPropertyRegistry.add("spring.datasource..driver-class-name", MY_SQL_CONTAINER::getDriverClassName);
        dynamicPropertyRegistry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
        dynamicPropertyRegistry.add("spring.redis.host", REDIS_CONTAINER::getHost);
        dynamicPropertyRegistry.add("spring.redis.port", () -> REDIS_CONTAINER.getMappedPort(6379).toString());
    }

    @Autowired
    private RedisUserTestService redisUserTestService;

    @MockBean
    private RedisUserTestRepository redisUserTestRepository;

    RedisUserTest redisUserTest;

    @BeforeEach
    void initializeData() {
        redisUserTest = RedisUserTest
                .builder()
                .id("0741195e-1774-4cd5-a47c-e99db62d5aa0")
                .username("lalalisa")
                .firstName("la la")
                .lastName("lisa")
                .dob(LocalDate.of(1997, 3, 27))
                .build();
    }

    @Test
    void connectionEstablished() {
        Assertions.assertEquals(MY_SQL_CONTAINER.isCreated(), true);
        Assertions.assertEquals(MY_SQL_CONTAINER.isRunning(), true);
        Assertions.assertEquals(REDIS_CONTAINER.isCreated(), true);
        Assertions.assertEquals(REDIS_CONTAINER.isRunning(), true);
    }

    @Test
    void save_validRequest_success(){
        Mockito.when(redisUserTestRepository.save(ArgumentMatchers.any()))
                .thenReturn(redisUserTest);

        var savedRedisUserTest = redisUserTestService.save(redisUserTest);

        Assertions.assertNotNull(savedRedisUserTest);
        Assertions.assertEquals(savedRedisUserTest.getId(), redisUserTest.getId());
        Assertions.assertEquals(savedRedisUserTest.getUsername(), redisUserTest.getUsername());
        Mockito.verify(redisUserTestRepository, Mockito.times(1)).save(redisUserTest);
    }

    @Test
    void delete_validRequest_success(){
        var id = "0741195e-1774-4cd5-a47c-e99db62d5aa0";
        redisUserTestService.delete(id);

        var redisUserTestResponse = redisUserTestRepository.findById(id).orElse(null);
        Assertions.assertNull(redisUserTestResponse);
        Mockito.verify(redisUserTestRepository, Mockito.times(1)).deleteById(id);
    }

    @Test
    void findById_invalidRequest_fail() {
        Mockito.when(redisUserTestRepository.findById("123")).thenReturn(Optional.ofNullable(null));

        Assertions.assertThrows(InvalidDataException.class, () -> redisUserTestService.findById("123"));
    }

    @Test
    void findById_validRequest_success() {
        var id = "0741195e-1774-4cd5-a47c-e99db62d5aa0";
        Mockito.when(redisUserTestRepository.findById(id)).thenReturn(Optional.ofNullable(redisUserTest));

        RedisUserTest foundRedisUser = redisUserTestService.findById(id);

        Assertions.assertNotNull(foundRedisUser);
        Assertions.assertEquals("lalalisa", foundRedisUser.getUsername());
        Assertions.assertEquals("la la", foundRedisUser.getFirstName());
        Assertions.assertEquals("lisa", foundRedisUser.getLastName());
    }
}
