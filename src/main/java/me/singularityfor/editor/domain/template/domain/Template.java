package me.singularityfor.editor.domain.template.domain;

import lombok.*;
import me.singularityfor.group.domain.Group;
import me.singularityfor.user.domain.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Created by hyva on 2015. 11. 13..
 */
@Entity
@Getter @Setter @ToString(exclude = {})
@EqualsAndHashCode(exclude = {})
@Table(name="_TEMPLATE_")
@NoArgsConstructor
public class Template {

    @Id @GeneratedValue
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_FK") @NotNull
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_FK") @NotNull
    private Group group;

    @Pattern(regexp = "([a-zA-Z0-9ㄱ-ㅎㅏ-ㅣ가-힣].*)") @NotNull
    @Size(min=2, max=25) @Column(name = "name", length=25, nullable = false)
    private String name;

    @Lob @Column(name="form")
    private String form;

    public Template(User author, Group group, String name, String form) {
        this.author = author;
        this.group = group;
        this.name = name;
        this.form = form;
    }

    public Template copy() {
        return new Template(this.author, this.group, this.name, this.form);
    }

    public Template copyAddSuffix(String suffix) {
        return new Template(this.author, this.group, this.name + suffix, this.form);
    }

    public boolean isAuthor(User author) {
        return this.author.equals(author);
    }

    public final void updateForm(String form) {
        this.form = form;
    }
}
