package com.example.springboot.todo.domain.entity;

import com.example.springboot.common.BaseEntity;
import com.example.springboot.common.exception.UnAuthorizedException;
import com.example.springboot.like.domain.entity.Like;
import com.example.springboot.member.domain.entity.Member;
import com.example.springboot.todo.dto.request.TodoUpdateRequest;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.validator.constraints.Length;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "todos")
public class Todo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    @Length(max = 255)
    private String title;

    @Column(name = "content", nullable = false)
    @Length(max = 255)
    private String content;

    @Column(name = "is_done")
    @ColumnDefault("false")
    @Builder.Default
    private Boolean isDone = false;

    // @ColumnDefault 어노테이션은 DDL 생성 시에만 사용되는 어노테이션이므로, JPA에서는 해당 어노테이션을 인식하지 않는다.
    // 따라서, @ColumnDefault 어노테이션을 사용하여 필드의 기본값을 지정해도 JPA에서는 해당 기본값을 인식하지 않고 null 값
    // 그래서 @Builder.Default로 Default값을 주거나 Builder로 생성시 따로 값을 지정해줘야한다.
    @Column(name = "like_count")
    @ColumnDefault("0")
    @Builder.Default
    private Long likeCount = 0L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "todo", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Builder.Default
    private List<Like> likes = new ArrayList<>();

    public void update(final TodoUpdateRequest todoUpdateRequest) {
        this.title = todoUpdateRequest.getTitle();
        this.content = todoUpdateRequest.getContent();
    }

    public void isAuthor(final Long memberId) {
        if (!this.member.getId().equals(memberId)) {
            throw new UnAuthorizedException();
        }
    }

    public void updateIsDone() {
        this.isDone = !this.isDone;
    }

    public void decreaseLikeCount() {
        --this.likeCount;
    }

    public void increaseLikeCount() {
        ++this.likeCount;
    }
}
