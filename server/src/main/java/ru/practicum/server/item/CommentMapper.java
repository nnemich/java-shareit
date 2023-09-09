package ru.practicum.server.item;

import lombok.experimental.UtilityClass;
import ru.practicum.server.item.dto.CommentDto;
import ru.practicum.server.item.dto.CommentResponseDto;
import ru.practicum.server.user.User;

import java.time.LocalDateTime;

@UtilityClass
public class CommentMapper {
    public CommentDto toCommentDto(Comment comment, User author) {
        return CommentDto
                .builder()
                .id(comment.getId())
                .authorName(author.getName())
                .created(comment.getCreated())
                .text(comment.getText())
                .build();
    }

    public Comment toComment(CommentDto dto, User author, Item item) {
        return Comment.builder()
                .authorName(author.getName())
                .created(LocalDateTime.now())
                .text(dto.getText())
                .item(item)
                .build();
    }

    public CommentResponseDto toCommentResponseDto(Comment comment) {
        return CommentResponseDto
                .builder()
                .id(comment.getId())
                .authorName(comment.getAuthorName())
                .created(LocalDateTime.now())
                .text(comment.getText())
                .item(new Item(
                        comment.getItem().getId(),
                        comment.getItem().getName()))
                .build();
    }
}