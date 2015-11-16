package me.singularityfor.editor.domain.template.controller;

import me.singularityfor.editor.domain.template.domain.Template;
import me.singularityfor.editor.domain.template.service.TemplateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Collection;

/**
 * Created by hyva on 2015. 11. 16..
 */
@RestController
@RequestMapping(value = "/template")
public class TemplateController {

    private static final Logger logger = LoggerFactory.getLogger(TemplateController.class);

    @Resource
    private TemplateService templateService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public Template create(@Valid @RequestBody Template template) {
        return templateService.create(template);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Template findOne(@PathVariable long id) {
        return templateService.findOne(id);
    }

    @RequestMapping(value = "group/{groupId}", method = RequestMethod.GET)
    public Collection<Template> findAllByPage(@PathVariable long groupId) {
        //TODO pageable 적용
        return templateService.findAllByGroup(groupId);
    }

}
