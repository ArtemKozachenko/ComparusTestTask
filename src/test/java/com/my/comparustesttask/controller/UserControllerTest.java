package com.my.comparustesttask.controller;

import com.my.comparustesttask.PostgreDB1ContainerBase;
import com.my.comparustesttask.PostgreDB2ContainerBase;
import com.my.comparustesttask.dto.ErrorResponseData;
import com.my.comparustesttask.dto.UserDTO;
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
class UserControllerTest {

    @LocalServerPort
    private Integer port;

    @ClassRule
    public static PostgreSQLContainer<PostgreDB1ContainerBase> postgresDB1Container = PostgreDB1ContainerBase.getInstance();
    @ClassRule
    public static PostgreSQLContainer<PostgreDB2ContainerBase> postgresDB2Container = PostgreDB2ContainerBase.getInstance();

    @BeforeAll
    static void beforeAll() {
        postgresDB1Container.start();
        postgresDB2Container.start();
    }

    @AfterAll
    static void afterAll() {
        postgresDB1Container.stop();
        postgresDB2Container.stop();
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
        UserDTO[] users = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/v1/users?name=" + name)
                .then()
                .statusCode(200)
                .extract()
                .as(UserDTO[].class);
        List<UserDTO> userList = Arrays.asList(users);

        Assertions.assertEquals(2, userList.size());
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
