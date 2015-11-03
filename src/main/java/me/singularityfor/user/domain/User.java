package me.singularityfor.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Email;
import org.springframework.security.crypto.keygen.KeyGenerators;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.function.Function;

import static org.springframework.security.crypto.util.EncodingUtils.subArray;

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

    @JsonIgnore
    @Lob
    @Column(name = "password", length=512, nullable = false)
    private String password;

    @Column(name="email")
    @Email(message = "이메일 주소가 유효하지 않습니다.")
    @Size(max = 50, message = "이메일은 50 글자 이하만 사용 가능합니다.")
    private String email;

    @JsonIgnore
    @Lob
    @Column(name="salt", length = 512, nullable = false)
    private String salt;

    @Pattern(regexp = "([a-zA-Z0-9ㄱ-ㅎㅏ-ㅣ가-힣].*)")
    @Size(min=2, max=25)
    @Column(name = "name", length=25, nullable = false)
    private String name;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Enumerated(EnumType.STRING)
    private State state;

    public User (String email, String name, String password, Date createDate) {
        this.password = password;
        this.email = email;
        this.name = name;
        this.createDate = createDate;
        this.state = State.READY;
    }

    public void hashingPassword(Function<String, Object> hasher) {
        this.salt = KeyGenerators.string().generateKey();
        this.password = (String) hasher.apply(this.password + this.salt);
    }

    public User clone() {
        return new User(this.id, this.password, this.salt, this.email, this.name, this.createDate, this.state);
    }

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
