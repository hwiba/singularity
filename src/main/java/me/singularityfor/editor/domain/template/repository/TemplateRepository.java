package me.singularityfor.editor.domain.template.repository;

import me.singularityfor.editor.domain.template.domain.Template;
import me.singularityfor.group.domain.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Created by hyva on 2015. 11. 14..
 */
public interface TemplateRepository extends JpaRepository<Template, Long> {

    Optional<Template> findOneByNameAndGroup(String name, Group group);

}