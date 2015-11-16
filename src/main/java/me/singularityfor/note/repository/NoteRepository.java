package me.singularityfor.note.repository;

import me.singularityfor.note.domain.Note;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by hyva on 2015. 11. 13..
 */
public interface NoteRepository extends JpaRepository<Note, Long> {

}
