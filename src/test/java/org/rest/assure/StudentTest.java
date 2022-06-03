package org.rest.assure;

import com.divecursos.m3s1.dto.request.StudentReqCreateDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StudentTest {

    private ObjectMapper mapper = new ObjectMapper();
    private static String tokenJwt = null;
    private static Integer studentRegister = null;

    private StudentReqCreateDTO studentReqCreateDTO = new StudentReqCreateDTO(1, "Teste");


    @BeforeAll
    public static void setUp() {
        baseURI = "http://localhost";
        port = 8080;
        basePath = "/m3s1-1.0-SNAPSHOT/api";
    }

    @Test
    @Order(1)
    public void testCreateStudent() throws JsonProcessingException {
        studentRegister = given()
                .contentType(ContentType.JSON)
                .body(mapper.writeValueAsString(studentReqCreateDTO))
                .when()
                .post("/students")
                .then()
                .statusCode(201)
                .body("register", notNullValue())
                .extract()
                .path("register");
        assertNotNull(studentRegister);
        System.out.println(studentRegister);
    }

    @Test
    @Order(2)
    public void testGetStudentById() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/students/" + studentRegister)
                .then()
                .statusCode(200)
                .body("register", equalTo(studentRegister))
                .body("register", equalTo(studentReqCreateDTO.getRegister()))
                .body("fullName", equalTo(studentReqCreateDTO.getFullName()));
    }

    @Test
    @Order(3)
    public void testGetStudents() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/students")
                .then()
                .statusCode(200)
                .body("register", hasItem(studentRegister))
                .body("register", hasItem(studentReqCreateDTO.getRegister()))
                .body("fullName", hasItem(studentReqCreateDTO.getFullName()));
    }

    @Test
    @Order(4)
    public void testUpdateStudent() throws JsonProcessingException {
        studentReqCreateDTO.setFullName("Teste Alterado");
        given()
                .contentType(ContentType.JSON)
                .body(mapper.writeValueAsString(studentReqCreateDTO))
                .when()
                .put("/students/" + studentRegister)
                .then()
                .statusCode(200)
                .body("register", equalTo(studentRegister))
                .body("register", equalTo(studentReqCreateDTO.getRegister()))
                .body("fullName", equalTo(studentReqCreateDTO.getFullName()));
    }

    @Test
    @Order(5)
    public void testDeleteStudent() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/students/" + studentRegister)
                .then()
                .statusCode(204);
    }
}
