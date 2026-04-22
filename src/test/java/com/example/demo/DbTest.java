package com.example.demo;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.Test;

class ApiTest {
    String baseApplicationURI = "http://localhost:8080/";

    @Test
    void testGetDbSizeOfZero(){
        given()
                .baseUri(baseApplicationURI)
        .when()
                .get("/db/getDbSize")
        .then()
                .statusCode(200);
    }

    @Test
    void testGetSends400WithNoRequestParameter() {
        given()
            .baseUri(baseApplicationURI)
        .when()
            .get("/db/get")
        .then()
            .statusCode(400);
    }

    @Test
    void testGetSends404OnMissingKey() {
        given()
            .baseUri(baseApplicationURI)
        .when()
            .get("/db/get?key=testing")
        .then()
            .statusCode(404);
    }

    @Test
    void testSetFailsWithMissingParameters() {
        given()
            .baseUri(baseApplicationURI)
        .when()
            .put("/db/set")
        .then()
            .statusCode(400);

        given()
            .baseUri(baseApplicationURI)
            .body("{\"value\":\"someValue\"}")
            .contentType("application/json")
        .when()
            .put("/db/set")
        .then()
            .statusCode(400);

        given()
            .baseUri(baseApplicationURI)
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
            .baseUri(baseApplicationURI)
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
            .baseUri(baseApplicationURI)
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
            .baseUri(baseApplicationURI)
            .body("{\"key\": \"test_key\", \"value\":\"test_value\"}")
            .contentType("application/json")
        .when()
            .put("/db/set")
        .then()
            .statusCode(200);

        given()
            .baseUri(baseApplicationURI)
        .when()
            .get("/db/get?key=test_key")
        .then()
            .statusCode(200)
            .body("value", equalTo("test_value"));
    }

    @Test
    void testDeleteWithEmptyKey(){
        given()
                .baseUri(baseApplicationURI)
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
                .baseUri(baseApplicationURI)
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
                .baseUri(baseApplicationURI)
                .body("{\"key\": \"test_key\", \"value\":\"test_value\"}")
                .contentType("application/json")
        .when()
                .put("/db/set")
        .then()
                .statusCode(200);

        given()
                .baseUri(baseApplicationURI)
                .queryParam("key", "test_key")
        .when()
                .delete("/db/del")
        .then()
                .statusCode(200);
    }

    @Test
    void testGetAll(){
        given()
                .baseUri(baseApplicationURI)

        .when()
                .get("/db/getAll")
        .then()
                .statusCode(200);

    }


    @Test
    void testGetDbSizeOfOne(){
        given()
                .baseUri(baseApplicationURI)
                .body("{\"key\": \"test_key\", \"value\":\"test_value\"}")
                .contentType("application/json")
        .when()
                .put("/db/set")
        .then()
                .statusCode(200);

        given()
                .baseUri(baseApplicationURI)
        .when()
                .get("/db/getDbSize")
        .then()
                .statusCode(200)
                .body("message", equalTo("Size of the Internal DB: 1"));
    }

    @Test
    void testGetMethodNotAllowedByPostRequest() {
        given()
                .baseUri(baseApplicationURI)
        .when()
                .post("/db/get")
        .then()
                .statusCode(405);
    }

    // Performance and Response Time testing
    @Test
    void testResponseTime() {
        given()
                .baseUri(baseApplicationURI)
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
                .baseUri(baseApplicationURI)
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
