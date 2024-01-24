package com.my.comparustesttask;

import com.my.comparustesttask.dto.ErrorResponseData;
import com.my.comparustesttask.entity.User;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.ClassRule;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ComparusTestTaskApplicationTests {

    @LocalServerPort
    private Integer port;

    @ClassRule
    public static PostgreSQLContainer postgres = (PostgreSQLContainer) new PostgreSQLContainer("postgres:11.1")
            .withDatabaseName("data-base-1")
            .withUsername("sa")
            .withPassword("sa")
            .withInitScript("dbscripts/create_and_populate_users_table.sql");

    @ClassRule
    public static PostgreSQLContainer postgres2 = (PostgreSQLContainer) new PostgreSQLContainer("postgres:11.1")
            .withDatabaseName("data-base-2")
            .withUsername("sa")
            .withPassword("sa")
            .withInitScript("dbscripts/create_and_populate_user_data_table.sql");

    @BeforeAll
    static void beforeAll() {
        postgres.start();
        postgres2.start();
        System.setProperty("DB_URL", postgres.getJdbcUrl());
        System.setProperty("DB2_URL", postgres2.getJdbcUrl());
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
        postgres2.stop();
    }

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
    }

    @Test
    void shouldGetAllUsers() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/v1/users")
                .then()
                .statusCode(200)
                .body(".", hasSize(6));
    }

    @Test
    void shouldGetUsersByName() {
        String name = "Artem";
        User[] users = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/v1/users?name=" + name)
                .then()
                .statusCode(200)
                .extract()
                .as(User[].class);

        Assertions.assertEquals(2, users.length);
        List<User> userList = Arrays.asList(users);
        userList.forEach(user -> {
            Assertions.assertEquals(name, user.getName());
        });
    }

    @Test
    void shouldReturn404code() {
        ErrorResponseData errorResponseData = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/v1/users?name=invalidName")
                .then()
                .statusCode(404)
                .extract()
                .as(ErrorResponseData.class);

        Assertions.assertEquals(HttpStatus.NOT_FOUND.getReasonPhrase(), errorResponseData.getErrorCode());
        Assertions.assertEquals("Not Found. Original Msg: User with name 'invalidName' not found.", errorResponseData.getErrorDescription());
    }

}
