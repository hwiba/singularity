package me.singularityfor.editor.domain.template.service;

import lombok.NonNull;
import me.singularityfor.editor.domain.template.domain.Template;
import me.singularityfor.editor.domain.template.repository.TemplateRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by hyva on 2015. 11. 13..
 */
@Service
public class TemplateService {

    @Resource private TemplateRepository templateRepository;

    public Template createTemplate(@NonNull Template template) {
        return templateRepository.save(this.numberingTemplate(template));
    }

    private Template numberingTemplate(Template template) {
        if (templateRepository.findOneByNameAndGroup(template.getName(), template.getGroup()).isPresent()) {
            return this.numberingTemplate(template, 1);
        }
        return template;
    }

    private Template numberingTemplate(Template template, Integer count) {
        String name = String.join("_", template.getName(), count.toString());
        if (templateRepository.findOneByNameAndGroup(name, template.getGroup()).isPresent()) {
            this.numberingTemplate(template, count + 1);
        } else {
          template.setName(name);
        }
        return template;
    }

}
