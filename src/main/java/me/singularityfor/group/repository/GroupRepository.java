package me.singularityfor.group.repository;

import me.singularityfor.group.domain.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Order on 2015. 11. 14..
 */
@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
}
