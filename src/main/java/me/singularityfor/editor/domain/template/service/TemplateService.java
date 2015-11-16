package me.singularityfor.editor.domain.template.service;

import lombok.NonNull;
import me.singularityfor.editor.domain.template.domain.Template;
import me.singularityfor.editor.domain.template.repository.TemplateRepository;
import me.singularityfor.group.repository.GroupRepository;
import me.singularityfor.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

/**
 * Created by hyva on 2015. 11. 13..
 */
@Service
@Transactional
public class TemplateService {

    @Resource private TemplateRepository templateRepository;
    @Resource private UserRepository userRepository;
    @Resource private GroupRepository groupRepository;

    public Template create(@NonNull Template template) {
        //TODO 그룹과 유저 권한 체크 예외 처리 및 테스트
        template.setAuthor(userRepository.findOne(template.getAuthor().getId()));
        template.setGroup(groupRepository.findOne(template.getGroup().getId()));
        return templateRepository.save(this.numbering(template));
    }

    private Template numbering(Template template) {
        if (templateRepository.findOneByNameAndGroup(template.getName(), template.getGroup()).isPresent()) {
            return this.numbering(template, 1);
        }
        return template;
    }

    private Template numbering(Template template, Integer count) {
        String name = String.join("_", template.getName(), count.toString());
        if (templateRepository.findOneByNameAndGroup(name, template.getGroup()).isPresent()) {
            this.numbering(template, count + 1);
        } else {
          template.setName(name);
        }
        return template;
    }

}
