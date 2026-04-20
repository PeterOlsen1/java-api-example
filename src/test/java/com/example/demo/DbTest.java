package com.example.demo;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.Test;

class ApiTest {

    @Test
    void testGetDbSizeOfZero(){
        given()
                .baseUri("http://localhost:8080/")
        .when()
                .get("/db/getDbSize")
        .then()
                .statusCode(200);
    }

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
            .body("{\"value\":\"someValue\"}")
            .contentType("application/json")
        .when()
            .put("/db/set")
        .then()
            .statusCode(400);

        given()
            .baseUri("http://localhost:8080/")
            .body("{\"key\":\"someKey\"}")
            .contentType("application/json")
        .when()
            .put("/db/set")
        .then()
            .statusCode(400);
    }

    @Test
    void testSetFailsWithEmptyKey() {
        given()
            .baseUri("http://localhost:8080/")
            .body("{\"key\":\"\"}")
            .contentType("application/json")
        .when()
            .put("/db/set")
        .then()
            .statusCode(400);
    }

    @Test
    void testValidSet() {
        given()
            .baseUri("http://localhost:8080/")
            .body("{\"key\": \"test_key\", \"value\":\"test_value\"}")
            .contentType("application/json")
        .when()
            .put("/db/set")
        .then()
            .statusCode(200);
    }

    @Test
    void testValidGet() {
        // Set first
        given()
            .baseUri("http://localhost:8080/")
            .body("{\"key\": \"test_key\", \"value\":\"test_value\"}")
            .contentType("application/json")
        .when()
            .put("/db/set")
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

    @Test
    void testDeleteWithEmptyKey(){
        given()
                .baseUri("http://localhost:8080/")
                .queryParam("key", "")
        .when()
                .delete("/db/del")
        .then()
            .statusCode(400)
            .body("message", equalTo("Missing 'key' parameter"));

    }

    @Test
    void testDeleteWithNonExistentKey(){

        given()
                .baseUri("http://localhost:8080/")
                .queryParam("key", "testVal")
        .when()
                .delete("/db/del")
        .then()
                .statusCode(500)
                .body("message", equalTo("No such key exists"));
    }

    @Test
    void testDeleteWithExistentKey(){
        given()
                .baseUri("http://localhost:8080/")
                .body("{\"key\": \"test_key\", \"value\":\"test_value\"}")
                .contentType("application/json")
        .when()
                .put("/db/set")
        .then()
                .statusCode(200);

        given()
                .baseUri("http://localhost:8080/")
                .queryParam("key", "test_key")
        .when()
                .delete("/db/del")
        .then()
                .statusCode(200);
    }

    @Test
    void testGetAll(){
        given()
                .baseUri("http://localhost:8080/")

        .when()
                .get("/db/getAll")
        .then()
                .statusCode(200);

    }


    @Test
    void testGetDbSizeOfOne(){
        given()
                .baseUri("http://localhost:8080/")
                .body("{\"key\": \"test_key\", \"value\":\"test_value\"}")
                .contentType("application/json")
        .when()
                .put("/db/set")
        .then()
                .statusCode(200);

        given()
                .baseUri("http://localhost:8080/")
        .when()
                .get("/db/getDbSize")
        .then()
                .statusCode(200)
                .body("message", equalTo("Size of the Internal DB: 1"));
    }

    @Test
    void testGetMethodNotAllowedByPostRequest() {
        given()
                .baseUri("http://localhost:8080/")
        .when()
                .post("/db/get")
        .then()
                .statusCode(405);
    }

    // Performance and Response Time testing
    @Test
    void testResponseTime() {
        given()
                .baseUri("http://localhost:8080/")
        .when()
                .get("/db/getDbSize")
        .then()
                .statusCode(200)
                .time(lessThan(1000L));
    }

    // Response Protocol validation (ISO-8601 protocol for date time)
    @Test
    void testSuccessResponseStructure() {
        given()
                .baseUri("http://localhost:8080/")
                .body("{\"key\": \"test_key\", \"value\": \"test_value\"}")
                .contentType("application/json")
        .when()
                .put("/db/set")
        .then()
                .statusCode(200)
                .body("status", notNullValue())
                .body("timestamp", matchesPattern(
                        "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d+Z"
                ));
    }

}
