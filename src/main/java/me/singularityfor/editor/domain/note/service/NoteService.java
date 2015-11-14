package me.singularityfor.editor.domain.note.service;

import me.singularityfor.editor.domain.note.domain.Note;
import me.singularityfor.editor.domain.note.Repository.NoteRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Order on 2015. 11. 13..
 */
@Service
public class NoteService {

    @Resource private NoteRepository noteRepository;

    public boolean createNote(Note note) {
        return noteRepository.save(note).isPresent();
    }
}
