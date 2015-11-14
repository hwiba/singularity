package me.singularityfor.group.domain;

import lombok.*;
import me.singularityfor.user.domain.User;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.*;

/**
 * Created by hyva on 2015. 11. 13..
 */
@Entity
@Getter @Setter @ToString(exclude = {"members"})
@EqualsAndHashCode(exclude = {"members"})
@Table(name = "_GROUP_")
public class Group {

    private enum State {
        PUBLIC, SECRET, DELETE
    }

    @Id @GeneratedValue
    private long id;

    @Pattern(regexp = "([a-zA-Z0-9ㄱ-ㅎㅏ-ㅣ가-힣].*)")
    @Size(min=2, max=25)
    @Column(name = "name", length=25, nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "captain_FK")
    private User captain;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Enumerated(value = EnumType.STRING)
    private State state;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_FK")
    private Collection<User> members = new ArrayList<>();

    public Group(final String name, final User captain, final Date createDate) {
        this.name = name;
        this.captain = captain;
        this.createDate = createDate;
        this.state = State.SECRET;
    }

    public boolean isCaptain(final User user) {
        return this.captain.equals(user);
    }

    public boolean hasMember(final User... users) {
        return this.members.containsAll(Arrays.asList(users)) || (isCaptain(users[0]) && users.length==1);
    }

    public void addMember(final User... users) {
        this.members.addAll(Arrays.asList(users));
    }
}
