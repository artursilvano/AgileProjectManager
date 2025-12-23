package com.arturcapelossi.agilepm.application.usecase;

import com.arturcapelossi.agilepm.domain.exception.ResourceNotFoundException;
import com.arturcapelossi.agilepm.domain.model.Comment;
import com.arturcapelossi.agilepm.domain.model.Task;
import com.arturcapelossi.agilepm.domain.model.User;
import com.arturcapelossi.agilepm.domain.repository.CommentRepository;
import com.arturcapelossi.agilepm.domain.repository.TaskRepository;
import com.arturcapelossi.agilepm.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateCommentUseCaseTest {

    @Mock
    private CommentRepository commentRepository;
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CreateCommentUseCase createCommentUseCase;

    private Task task;
    private User author;
    private UUID taskId;
    private String userEmail;

    @BeforeEach
    void setUp() {
        taskId = UUID.randomUUID();
        userEmail = "test@example.com";

        task = new Task();
        task.setId(taskId);

        author = new User();
        author.setEmail(userEmail);
    }

    @Test
    void execute_ShouldCreateComment_WhenConditionsAreMet() {
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(author));
        when(commentRepository.save(any(Comment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Comment comment = createCommentUseCase.execute(taskId, userEmail, "Comment content");

        assertNotNull(comment);
        assertEquals("Comment content", comment.getContent());
        assertEquals(task, comment.getTask());
        assertEquals(author, comment.getAuthor());
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void execute_ShouldThrowException_WhenTaskNotFound() {
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> createCommentUseCase.execute(taskId, userEmail, "Comment content"));
    }

    @Test
    void execute_ShouldThrowException_WhenUserNotFound() {
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> createCommentUseCase.execute(taskId, userEmail, "Comment content"));
    }
}

