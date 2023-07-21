package com.example.springboot.member.domain.entity;

import com.example.springboot.common.BaseEntity;
import com.example.springboot.todo.domain.entity.Todo;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "members")
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "password", nullable = false)
    @Length(max = 255)
    private String password;

    @Column(name = "email", nullable = false, unique = true)
    @Length(max = 50)
    private String email;

    @Column(name = "name", nullable = false)
    @Length(max = 20)
    private String name;

    @Column(name = "age", nullable = false)
    private int age;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Builder.Default
    private List<Todo> todos = new ArrayList<>();

    public boolean hasSamePassword(final String password) {
        return Objects.equals(this.password, password);
    }
}
