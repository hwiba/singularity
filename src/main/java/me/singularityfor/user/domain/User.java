package me.singularityfor.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Created by Order on 2015. 10. 22..
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private enum State {
        READY, ACCEPT, DELETE
    }

    @Id
    @GeneratedValue
    private long id;

    @Pattern(regexp = "([a-zA-Z0-9ㄱ-ㅎㅏ-ㅣ가-힣].*)")
    @Size(min=2, max=25)
    @Column(name = "name", length=25, nullable = false)
    private String name;

    @JsonIgnore
    @Pattern(regexp = "([a-zA-Z].*[0-9])|([0-9].*[a-zA-Z])")
    @Size(min = 8, max = 25)
    @Column(name = "password", length=20, nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private State state;

    public User changeName (final String name) {
        this.name = name;
        return this;
    }

    public void delete() {
        this.state = State.DELETE;
    }

    public void accept() {
        this.state = State.ACCEPT;
    }

    public void ready() {
        this.state = State.READY;
    }

    public boolean isAccept() {
        return this.state.equals(State.ACCEPT);
    }

    public boolean isDelete() {
        return this.state.equals(State.DELETE);
    }

    public boolean isReady() {
        return this.state.equals(State.READY);
    }

    public boolean isValidatedPassword(String password) {
        return this.password.equals(password);
    }
}
