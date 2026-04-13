package com.example.demo;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.Test;

class ApiTest {
    @Test
    void basicAPITest() {
        given()
            .baseUri("http://localhost:8080")
        .when()
            .get("/returnMyNumber?number=5")
        .then()
            .statusCode(200)
            .body("number", equalTo("5"));
    }
}
