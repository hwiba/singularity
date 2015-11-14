package me.singularityfor.editor.domain.note.repository;

import me.singularityfor.editor.domain.note.domain.Note;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by hyva on 2015. 11. 13..
 */
public interface NoteRepository extends JpaRepository<Note, Long> {

}
