package com.learning.identityservice;

import com.learning.identityservice.constant.PredefinedRole;
import com.learning.identityservice.dto.request.RoleRequest;
import com.learning.identityservice.dto.request.UserCreationRequest;
import com.learning.identityservice.entity.RedisUserTest;
import com.learning.identityservice.entity.User;
import com.learning.identityservice.service.BaseRedisService;
import com.learning.identityservice.service.RoleService;
import com.learning.identityservice.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@Slf4j
@SpringBootApplication
public class IdentityServiceApplication {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(IdentityServiceApplication.class, args);

        User user = User
                .builder()
                .id("0741195e-1774-4cd5-a47c-e99db62d5aa0")
                .username("lalalisa")
                .firstName("la la")
                .lastName("lisa")
                .build();

        BaseRedisService<String, String, Object> baseRedisService = context.getBean(BaseRedisService.class);

        String key = "userRedisTestKey" + user.getId();
        baseRedisService.setValue(key, user);

        log.info("Redis Value {}", baseRedisService.getValue(key));
    }

    @PostConstruct
    @ConditionalOnProperty(
            prefix = "spring",
            value = "datasource.driver-class-name",
            havingValue = "com.mysql.cj.jdbc.Driver"
    )
    public void createDefaultsData() {
        if (roleService.countRoles() == 0) {
            RoleRequest roleAdmin = RoleRequest
                    .builder()
                    .name(PredefinedRole.ADMIN_ROLE)
                    .description("Admin Role")
                    .build();

            RoleRequest roleUser = RoleRequest
                    .builder()
                    .name(PredefinedRole.USER_ROLE)
                    .description("User Role")
                    .build();

            roleService.create(roleUser);
            roleService.create(roleAdmin);
        }
        if (userService.countUsers() == 0) {
            UserCreationRequest user1 = UserCreationRequest.builder()
                    .username("nguyenvanquan")
                    .password("Aa@123456")
                    .firstName("Nguyen Van")
                    .lastName("Quan")
                    .dob(LocalDate.of(1995, 8, 20))
                    .build();

            UserCreationRequest user2 = UserCreationRequest.builder()
                    .username("hoanganhduy1122")
                    .password("Aa@123456")
                    .firstName("Hoang Anh")
                    .lastName("Duy")
                    .dob(LocalDate.of(1998, 3, 15))
                    .build();

            UserCreationRequest user3 = UserCreationRequest.builder()
                    .username("trantanbinh98")
                    .password("Aa@123456")
                    .firstName("Tran Tan")
                    .lastName("Binh")
                    .dob(LocalDate.of(2000, 11, 10))
                    .build();

            UserCreationRequest user4 = UserCreationRequest.builder()
                    .username("doanthuan97")
                    .password("Aa@123456")
                    .firstName("Doan")
                    .lastName("Thu An")
                    .dob(LocalDate.of(1995, 8, 20))
                    .build();

            UserCreationRequest user5 = UserCreationRequest.builder()
                    .username("adminsmarttailor123")
                    .password("Aa@123456")
                    .firstName("Admin")
                    .lastName("Smart Tailor")
                    .dob(LocalDate.of(1995, 8, 20))
                    .build();

            UserCreationRequest user6 = UserCreationRequest.builder()
                    .username("railwaydb")
                    .password("Aa@123456")
                    .firstName("railway")
                    .lastName("database")
                    .dob(LocalDate.of(1995, 8, 20))
                    .build();

            UserCreationRequest user7 = UserCreationRequest.builder()
                    .username("springbootrender")
                    .password("Aa@123456")
                    .firstName("spring boot")
                    .lastName("render")
                    .dob(LocalDate.of(1995, 8, 20))
                    .build();

            UserCreationRequest user8 = UserCreationRequest.builder()
                    .username("levanluyen123")
                    .password("Aa@123456")
                    .firstName("Le")
                    .lastName("Van Luyen")
                    .dob(LocalDate.of(1995, 8, 20))
                    .build();

            UserCreationRequest user9 = UserCreationRequest.builder()
                    .username("phamthanhgiang458")
                    .password("Aa@123456")
                    .firstName("Pham")
                    .lastName("Thanh Giang")
                    .dob(LocalDate.of(1995, 8, 20))
                    .build();

            UserCreationRequest user10 = UserCreationRequest.builder()
                    .username("cristiano")
                    .password("Aa@123456")
                    .firstName("Ronaldo")
                    .lastName("Ronaldo")
                    .dob(LocalDate.of(1995, 8, 20))
                    .build();

            userService.createUserWithRole(user1, PredefinedRole.USER_ROLE);
            userService.createUserWithRole(user2, PredefinedRole.USER_ROLE);
            userService.createUserWithRole(user3, PredefinedRole.USER_ROLE);
            userService.createUserWithRole(user4, PredefinedRole.USER_ROLE);
            userService.createUserWithRole(user5, PredefinedRole.ADMIN_ROLE);
            userService.createUserWithRole(user6, PredefinedRole.USER_ROLE);
            userService.createUserWithRole(user7, PredefinedRole.USER_ROLE);
            userService.createUserWithRole(user8, PredefinedRole.USER_ROLE);
            userService.createUserWithRole(user9, PredefinedRole.USER_ROLE);
            userService.createUserWithRole(user10, PredefinedRole.USER_ROLE);
        }
    }
}
