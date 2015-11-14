package me.singularityfor.editor.domain.note.service;

import lombok.NonNull;
import me.singularityfor.editor.domain.note.domain.Note;
import me.singularityfor.editor.domain.note.repository.NoteRepository;
import me.singularityfor.group.domain.Group;
import me.singularityfor.group.repository.GroupRepository;
import me.singularityfor.user.domain.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;

/**
 * Created by hyva on 2015. 11. 13..
 */
@Service
@Transactional
public class NoteService {

    @Resource private NoteRepository noteRepository;
    @Resource private GroupRepository groupRepository;

    public Note create(@NonNull Note note) throws IllegalAccessException {
        if (! isGroupMember(note.getGroup(), note.getAuthor())) {
            throw new IllegalAccessException("권한이 없는 요청");
        }
        return noteRepository.save(note);
    }

    private boolean isGroupMember(Group group, User... users) {
        return groupRepository.findOne(group.getId()).hasMember(users);
    }

    public Note findOne(long id) {
        return noteRepository.findOne(id);
    }
}
