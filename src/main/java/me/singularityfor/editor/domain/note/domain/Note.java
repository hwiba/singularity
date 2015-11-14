package me.singularityfor.editor.domain.note.domain;

import lombok.*;
import me.singularityfor.group.domain.Group;
import me.singularityfor.user.domain.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by hyva on 2015. 11. 13..
 */
@Entity
@Getter @Setter @ToString(exclude = {})
@EqualsAndHashCode(exclude = {})
@Table(name = "_NOTE_")
@NoArgsConstructor
public class Note {

    private enum State {
        PUBLIC, DELETE
    }

    @Id @GeneratedValue
    private long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "author_FK", nullable = false)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "group_FK")
    private Group group;

    @Lob @Column(name="text")
    private String text;

    @Enumerated(value = EnumType.STRING)
    private State state;

    public Note(User author, Group group, String text) {
        this.author = author;
        this.group = group;
        this.text = text;
        this.state = State.PUBLIC;
    }
}
