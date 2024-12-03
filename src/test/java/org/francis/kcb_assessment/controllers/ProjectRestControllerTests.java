package org.francis.kcb_assessment.controllers;

import io.restassured.http.ContentType;
import org.apache.commons.lang3.RandomStringUtils;
import org.francis.kcb_assessment.KcbAssessmentApplicationTests;
import org.francis.kcb_assessment.datatypes.TaskStatus;
import org.francis.kcb_assessment.dto.request.CreateProjectDTO;
import org.francis.kcb_assessment.dto.request.CreateTaskDTO;
import org.francis.kcb_assessment.dto.request.UpdateTaskDTO;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ProjectRestControllerTests extends KcbAssessmentApplicationTests {

    @Test
    public void createProjectFailsEmptyRequest() {
        CreateProjectDTO createProjectDTO = new CreateProjectDTO();

        given()
                .contentType(ContentType.JSON)
                .body(createProjectDTO)
                .post("/projects")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body(containsString("name must not be blank"))
                .body(containsString("description must not be blank"));
    }

    @Test
    public void createProjectFailsInvalidLengths() {
        CreateProjectDTO createProjectDTO = new CreateProjectDTO();
        createProjectDTO.setDescription(RandomStringUtils.randomAlphabetic(600));
        createProjectDTO.setName(RandomStringUtils.randomAlphabetic(300));

        given()
                .contentType(ContentType.JSON)
                .body(createProjectDTO)
                .post("/projects")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body(containsString("name size must be between 3 and 250"))
                .body(containsString("description size must be between 3 and 500"));
    }

    @Test
    public void createProjectFailsNameExist() {
        CreateProjectDTO createProjectDTO = new CreateProjectDTO();
        createProjectDTO.setName("Project Monday");
        createProjectDTO.setDescription("My description");

        given()
                .contentType(ContentType.JSON)
                .body(createProjectDTO)
                .post("/projects")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body(containsString("project name exist"));
    }

    @Test
    public void createProjectWorks() {
        CreateProjectDTO createProjectDTO = new CreateProjectDTO();
        createProjectDTO.setName("ProjectAA");
        createProjectDTO.setDescription("My Project AA description");

        given()
                .contentType(ContentType.JSON)
                .body(createProjectDTO)
                .post("/projects")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("id", notNullValue());
    }

    @Test
    public void getProjectsWorks() {

        given()
                .get("/projects")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("content.size()", greaterThanOrEqualTo(3));

        given()
                .param("page", 0)
                .param("size", 1)
                .get("/projects")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("content.size()", equalTo(1));
    }

    @Test
    public void getProjectSummaryWorks() {

        given()
                .get("/projects/summary")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("content.size()", greaterThanOrEqualTo(3));
    }

    @Test
    public void getProjectByIdFails() {

        given()
                .get("/projects/{projectId}", 389374934)
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body(containsString("project not found"));

        given()
                .get("/projects/{projectId}", 2)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(2));

    }

    @Test
    public void getProjectByIdWorks() {
        given()
                .get("/projects/{projectId}", 2)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(2));

    }

    @Test
    public void createTaskFailsEmptyRequest() {
        CreateTaskDTO createTaskDTO = new CreateTaskDTO();

        given()
                .contentType(ContentType.JSON)
                .body(createTaskDTO)
                .post("/projects/{projectId}/tasks", 2)
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body(containsString("title must not be blank"))
                .body(containsString("description must not be blank"))
                .body(containsString("dueDate must not be null"));
    }

    @Test
    public void createTaskFailsInvalidLengths() {
        CreateTaskDTO createTaskDTO = new CreateTaskDTO();
        createTaskDTO.setDescription(RandomStringUtils.randomAlphabetic(600));
        createTaskDTO.setTitle(RandomStringUtils.randomAlphabetic(300));

        given()
                .contentType(ContentType.JSON)
                .body(createTaskDTO)
                .post("/projects/{projectId}/tasks", 2)
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body(containsString("description size must be between 3 and 500"))
                .body(containsString("title size must be between 3 and 250"));
    }

    @Test
    public void createTaskFailsInvalidDueDate() {
        CreateTaskDTO createTaskDTO = new CreateTaskDTO();
        createTaskDTO.setTitle("Juma Printing Task");
        createTaskDTO.setDescription("Printing docs task");
        createTaskDTO.setDueDate(LocalDate.now().minusDays(2));

        given()
                .contentType(ContentType.JSON)
                .body(createTaskDTO)
                .post("/projects/{projectId}/tasks", 2)
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body(containsString("dueDate must be a future date"));
    }

    @Test
    public void createTaskFailsInvalidProjectId() {
        CreateTaskDTO createTaskDTO = new CreateTaskDTO();
        createTaskDTO.setTitle("Juma Printing Task");
        createTaskDTO.setDescription("Printing docs task");
        createTaskDTO.setDueDate(LocalDate.now().plusDays(2));

        given()
                .contentType(ContentType.JSON)
                .body(createTaskDTO)
                .post("/projects/{projectId}/tasks", 3487643)
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body(containsString("project not found"));
    }

    @Test
    public void createTaskWorks() {
        CreateTaskDTO createTaskDTO = new CreateTaskDTO();
        createTaskDTO.setTitle("Juma Printing Task");
        createTaskDTO.setDescription("Printing docs task");
        createTaskDTO.setDueDate(LocalDate.now().plusDays(2));

        given()
                .contentType(ContentType.JSON)
                .body(createTaskDTO)
                .post("/projects/{projectId}/tasks", 2)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("id", notNullValue())
                .body("status", equalTo(TaskStatus.TO_DO.name()));
    }

    @Test
    public void getTasksWorks() {

        given()
                .get("/projects/{projectId}/tasks", 3)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("content.size()", greaterThanOrEqualTo(3));
    }

    @Test
    public void getTasksByStatusWorks() {

        given()
                .queryParam("status", TaskStatus.TO_DO.name())
                .get("/projects/{projectId}/tasks", 3)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("content.size()", greaterThanOrEqualTo(1))
                .body("content[0].status", equalTo(TaskStatus.TO_DO.name()));
    }

    @Test
    public void getTasksByDueDateWorks() {

        given()
                .queryParam("dueDate", "2024-12-10")
                .get("/projects/{projectId}/tasks", 3)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("content.size()", greaterThanOrEqualTo(1))
                .body("content[0].dueDate", equalTo("2024-12-10"));
    }

    @Test
    public void getTasksByStatusAndDueDateWorks() {

        given()
                .queryParam("dueDate", "2024-12-02")
                .queryParam("status", TaskStatus.DONE.name())
                .get("/projects/{projectId}/tasks", 3)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("content.size()", greaterThanOrEqualTo(1))
                .body("content[0].dueDate", equalTo("2024-12-02"))
                .body("content[0].status", equalTo(TaskStatus.DONE.name()));
    }

    @Test
    public void getTaskWorks() {

        given()
                .get("/projects/{projectId}/tasks/{taskId}", 3, 1)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(1));
    }

    @Test
    public void updateTaskFailsEmptyRequest() {
        UpdateTaskDTO updateTaskDTO = new UpdateTaskDTO();

        given()
                .contentType(ContentType.JSON)
                .body(updateTaskDTO)
                .put("/projects/{projectId}/tasks/{taskId}", 3, 2)
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body(containsString("title must not be blank"))
                .body(containsString("description must not be blank"))
                .body(containsString("dueDate must not be null"))
                .body(containsString("status must not be null"));
    }

    @Test
    public void updateTaskFailsInvalidLengths() {
        UpdateTaskDTO updateTaskDTO = new UpdateTaskDTO();
        updateTaskDTO.setDescription(RandomStringUtils.randomAlphabetic(600));
        updateTaskDTO.setTitle(RandomStringUtils.randomAlphabetic(300));
        updateTaskDTO.setStatus(TaskStatus.IN_PROGRESS);

        given()
                .contentType(ContentType.JSON)
                .body(updateTaskDTO)
                .put("/projects/{projectId}/tasks/{taskId}", 3, 2)
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body(containsString("description size must be between 3 and 500"))
                .body(containsString("title size must be between 3 and 250"));
    }

    @Test
    public void updateTaskFailsInvalidDueDate() {
        UpdateTaskDTO updateTaskDTO = new UpdateTaskDTO();
        updateTaskDTO.setTitle("Nick Tuesday Task");
        updateTaskDTO.setDescription("My Description");
        updateTaskDTO.setStatus(TaskStatus.IN_PROGRESS);
        updateTaskDTO.setDueDate(LocalDate.now().minusDays(2));

        given()
                .contentType(ContentType.JSON)
                .body(updateTaskDTO)
                .put("/projects/{projectId}/tasks/{taskId}", 3, 2)
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body(containsString("dueDate must be a future date"));
    }

    @Test
    public void updateTaskWorks() {
        UpdateTaskDTO updateTaskDTO = new UpdateTaskDTO();
        updateTaskDTO.setTitle("Filing Task");
        updateTaskDTO.setDescription("Ken filing Task");
        updateTaskDTO.setStatus(TaskStatus.DONE);
        updateTaskDTO.setDueDate(LocalDate.now().plusDays(2));

        given()
                .contentType(ContentType.JSON)
                .body(updateTaskDTO)
                .put("/projects/{projectId}/tasks/{taskId}", 3, 2)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(2))
                .body("status", equalTo(TaskStatus.DONE.name()));
    }

    @Test
    public void deleteTaskFailsInvalidTaskId() {

        given()
                .delete("/projects/{projectId}/tasks/{taskId}", 3, 3726823)
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body(containsString("Task not found"));
    }

    @Test
    public void deleteTaskWorks() {

        given()
                .delete("/projects/{projectId}/tasks/{taskId}", 3, 4)
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

}
