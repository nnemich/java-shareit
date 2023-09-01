package ru.practicum.shareit.item;

import lombok.*;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Класс описывает модель Item
 */

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
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @ToString.Exclude
    private User owner;
    @Column(name = "available")
    @NotNull(message = "Статус бронирования не может быть пустым")
    private Boolean available;
}
