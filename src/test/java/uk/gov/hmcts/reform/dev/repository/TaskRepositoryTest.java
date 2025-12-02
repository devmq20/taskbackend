package uk.gov.hmcts.reform.dev.repository;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import uk.gov.hmcts.reform.dev.models.Task;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DataJpaTest
@EnableJpaRepositories(basePackageClasses = TaskRepository.class)
@EntityScan(basePackageClasses = Task.class)
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Test
    void should_store_and_retrieve_task() {

        Task saved = taskRepository.save(
            new Task(
                null,                        // id auto-generated
                "Test title",
                "Some description",
                "TODO",
                LocalDateTime.of(2025, 1, 1, 10, 0)
            )
        );

        Task found = taskRepository.findById(saved.getId()).orElseThrow();

        assertThat(found.getId()).isNotNull();
        assertThat(found.getTitle()).isEqualTo("Test title");
        assertThat(found.getDescription()).isEqualTo("Some description");
        assertThat(found.getStatus()).isEqualTo("TODO");
        assertThat(found.getDueDateTime()).isEqualTo(LocalDateTime.of(2025, 1, 1, 10, 0));

        saved = taskRepository.save(
            new Task(
                null,                        // id auto-generated
                "Test title 2",
                "Some description 2",
                "TODO",
                LocalDateTime.of(2025, 1, 1, 10, 0)
            )
        );

        found = taskRepository.findById(saved.getId()).orElseThrow();

        assertThat(found.getId()).isNotNull();
        assertThat(found.getTitle()).isEqualTo("Test title 2");
        assertThat(found.getDescription()).isEqualTo("Some description 2");
        assertThat(found.getStatus()).isEqualTo("TODO");
        assertThat(found.getDueDateTime()).isEqualTo(LocalDateTime.of(2025, 1, 1, 10, 0));


    }

    @Test
    void shouldThrowError_WhenTitleIsMissing() {
        Task task = new Task(
            null,
            null,                     // missing title
            "Test description",
            "TODO",
            LocalDateTime.now()
        );

        assertThatThrownBy(() -> taskRepository.saveAndFlush(task))
            .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void shouldThrowError_WhenTitleIsEmpty() {
        Task task = new Task(
            null,
            "",                            // empty title
            "Test description",
            "TODO",
            LocalDateTime.now()
        );

        assertThatThrownBy(() -> taskRepository.saveAndFlush(task))
            .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void shouldThrowError_WhenStatusIsMissing() {
        Task task = new Task(
            null,
            "Test title",
            "Test description",
            null,                         // missing status
            LocalDateTime.now()
        );

        assertThatThrownBy(() -> taskRepository.saveAndFlush(task))
            .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void shouldThrowError_WhenStatusIsEmpty() {
        Task task = new Task(
            null,
            "Test title",
            "Test description",
            "",                            // empty status
            LocalDateTime.now()
        );

        assertThatThrownBy(() -> taskRepository.saveAndFlush(task))
            .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void shouldThrowError_WhenDueDateTimeIsMissing() {
        Task task = new Task(
            null,
            "Test title",
            "Test description",
            "TODO",
            null                           // missing due date
        );

        assertThatThrownBy(() -> taskRepository.saveAndFlush(task))
            .isInstanceOf(ConstraintViolationException.class);
    }

}
