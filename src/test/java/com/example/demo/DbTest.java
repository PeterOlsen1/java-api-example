package com.example.demo;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.Test;

class ApiTest {
    @Test
    void testGetSends400WithNoRequestParameter() {
        given()
            .baseUri("http://localhost:8080/")
        .when()
            .get("/db/get")
        .then()
            .statusCode(400);
    }

    @Test
    void testGetSends404OnMissingKey() {
        given()
            .baseUri("http://localhost:8080/")
        .when()
            .get("/db/get?key=testing")
        .then()
            .statusCode(404);
    }

    @Test
    void testSetFailsWithMissingParameters() {
        given()
            .baseUri("http://localhost:8080/")
        .when()
            .put("/db/set")
        .then()
            .statusCode(400);

        given()
            .baseUri("http://localhost:8080/")
        .when()
            .put("/db/set")
            .body("{\"value\":\"someValue\"}")
            .contentType("application/json")
        .then()
            .statusCode(400);

        given()
            .baseUri("http://localhost:8080/")
        .when()
            .put("/db/set")
            .body("{\"key\":\"someKey\"}")
            .contentType("application/json")
        .then()
            .statusCode(400);
    }

    @Test
    void testSetFailsWithEmptyKey() {
        given()
            .baseUri("http://localhost:8080/")
        .when()
            .put("/db/set")
            .body("{\"key\":\"\"}")
            .contentType("application/json")
        .then()
            .statusCode(400);
    }

    @Test
    void testValidSet() {
        given()
            .baseUri("http://localhost:8080/")
        .when()
            .put("/db/set")
            .body("{\"key\": \"test_key\", \"value\":\"test_value\"}")
            .contentType("application/json")
        .then()
            .statusCode(200);
    }

    @Test
    void testValidGet() {
        // Set first
        given()
            .baseUri("http://localhost:8080/")
        .when()
            .put("/db/set")
            .body("{\"key\": \"test_key\", \"value\":\"test_value\"}")
            .contentType("application/json")
        .then()
            .statusCode(200);

        given()
            .baseUri("http://localhost:8080/")
        .when()
            .get("/db/get?key=test_key")
        .then()
            .statusCode(200)
            .body("value", equalTo("test_value"));
    }
}
