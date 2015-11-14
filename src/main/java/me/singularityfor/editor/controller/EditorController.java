package me.singularityfor.editor.controller;

import me.singularityfor.note.domain.Note;
import me.singularityfor.editor.domain.Template;
import me.singularityfor.note.service.NoteService;
import me.singularityfor.editor.service.TemplateService;
import me.singularityfor.group.domain.Group;
import me.singularityfor.user.domain.User;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Order on 2015. 11. 13..
 */
@RestController
@RequestMapping(value = "/editor")
public class EditorController {

    @Resource private TemplateService templateService;

    @Resource private NoteService noteService;

    @RequestMapping(value = "/template/{author}", method = RequestMethod.POST)
    public boolean createTemplate (@PathVariable Long authorId, @RequestParam String form) {
        User author = new User();
        author.setId(authorId);
        return templateService.createTemplate(new Template(author, form));
    }

    @RequestMapping(value = "/template/group", method = RequestMethod.GET)
    public List<Template> readTemplates(Group group) {
        return templateService.findAll(group);
    }

    @RequestMapping(value = "/template/author", method = RequestMethod.GET)
    public List<Template> readTemplates(User author) {
        return templateService.findAll(author);
    }

    @RequestMapping(value = "/note", method = RequestMethod.POST)
    public boolean createNote (Note note) {
        return noteService.createNote(note);
    }


}
