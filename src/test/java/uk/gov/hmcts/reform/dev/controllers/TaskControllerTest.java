package uk.gov.hmcts.reform.dev.controllers;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static io.restassured.RestAssured.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
class TaskControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        RestAssured.useRelaxedHTTPSValidation();
        RestAssured.port = port; // RestAssured hits the random server port
    }

    // --------- Integration test using RestAssured ---------
    @Test
    void shouldCreateTask() {
        String json = """
            {
                "title": "My Task",
                "description": "Do something",
                "status": "TODO",
                "dueDateTime": "2025-01-01T10:00:00"
            }
            """;

        Response response = given()
            .contentType(ContentType.JSON)
            .body(json)
            .when()
            .post("/tasks")
            .then()
            .extract().response();

        org.junit.jupiter.api.Assertions.assertEquals(200, response.statusCode());
        org.junit.jupiter.api.Assertions.assertEquals("My Task", response.jsonPath().getString("title"));
    }

    // --------- Unit-style test using MockMvc ---------
    @Test
    void shouldReturn400_WhenTitleMissing() throws Exception {
        String json = """
            {
                "description": "Do something",
                "status": "TODO",
                "dueDateTime": "2025-01-01T10:00:00"
            }
            """;

        mockMvc.perform(post("/tasks")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.title").value("Title is required"));
    }
}
