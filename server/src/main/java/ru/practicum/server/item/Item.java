package ru.practicum.server.item;

import lombok.*;
import ru.practicum.server.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;
    @NotBlank(message = "Имя не может быть пустым")
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    @NotBlank(message = "Описание не может быть пустым")
    private String description;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @ToString.Exclude
    private User owner;
    @Column(name = "available")
    @NotNull(message = "Статус бронирования не может быть пустым")
    private Boolean available;
    @Column(name = "request")
    private Long requestId;

    public Item(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}