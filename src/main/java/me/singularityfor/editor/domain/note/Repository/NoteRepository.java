package me.singularityfor.editor.domain.note.Repository;

import me.singularityfor.editor.domain.note.domain.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Created by Order on 2015. 11. 13..
 */
@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

    Optional<Note> save(Note note);
}
