package me.singularityfor.group.domain;

import lombok.*;
import me.singularityfor.user.domain.User;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * Created by hyva on 2015. 11. 13..
 */
@Entity
@Getter @Setter @ToString(exclude = {"members"})
@EqualsAndHashCode(exclude = {"members"})
@Table(name = "_GROUP_")
public class Group {

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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_FK")
    private List<User> members;

    public Group(final String name, final User captain, final Date createDate) {
        this.name = name;
        this.captain = captain;
        this.createDate = createDate;
    }

    public boolean isCaptain(final User user) {
        return this.captain.equals(user);
    }

}
