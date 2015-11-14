package me.singularityfor.editor.domain.note.domain;

import lombok.Data;
import me.singularityfor.group.domain.Group;
import me.singularityfor.user.domain.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by Order on 2015. 11. 13..
 */
@Entity
@Data
@Table(name = "_NOTE_")
public class Note {

    @Id @GeneratedValue
    private long id;

    @ManyToOne @JoinColumn(name = "user_key") @NotNull
    private User author;

    @OneToMany @JoinColumn(name = "group_key")
    private Group group;

    @Lob @Column(name="text")
    private String text;

    public void updateNote(String text) {
        this.text = text;
    }
}
