package me.singularityfor.editor.controller;

import me.singularityfor.editor.domain.note.service.NoteService;
import me.singularityfor.editor.domain.template.domain.Template;
import me.singularityfor.editor.domain.template.service.TemplateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * Created by hyva on 2015. 11. 13..
 */
@RestController
@RequestMapping(value = "/editor")
public class EditorController {

    private static final Logger logger = LoggerFactory.getLogger(EditorController.class);

    @Resource private TemplateService templateService;
    @Resource private NoteService noteService;

    @RequestMapping(value = "/template", method = RequestMethod.POST)
    public Template createTemplate (@Valid @RequestBody Template template) {
        return templateService.create(template);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public String handleException(MethodArgumentNotValidException exception) {
        //TODO 에러 메시지 통합 관리
        return exception.getMessage();
    }

}
