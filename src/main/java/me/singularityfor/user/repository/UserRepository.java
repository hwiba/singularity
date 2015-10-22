package me.singularityfor.user.repository;

import me.singularityfor.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Order on 2015. 10. 22..
 */
public interface UserRepository extends JpaRepository<User, Long> {


}
