package me.singularityfor.editor.controller;

import me.singularityfor.editor.domain.note.service.NoteService;
import me.singularityfor.editor.domain.template.domain.Template;
import me.singularityfor.editor.domain.template.dto.TemplateDtoIgnoreGroup;
import me.singularityfor.editor.domain.template.service.TemplateService;
import me.singularityfor.utility.handle.ExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * Created by hyva on 2015. 11. 13..
 */
@RestController
@RequestMapping(value = "/editor")
public class EditorController extends ExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(EditorController.class);

    @Resource private TemplateService templateService;
    @Resource private NoteService noteService;

    @RequestMapping(value = "/template", method = RequestMethod.POST)
    public TemplateDtoIgnoreGroup createTemplate (@Valid @RequestBody Template template) {
        return new TemplateDtoIgnoreGroup(templateService.create(template));
    }

}
