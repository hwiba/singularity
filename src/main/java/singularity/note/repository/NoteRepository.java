package singularity.note.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import singularity.note.domain.Note;
import singularity.party.domain.Party;

public interface NoteRepository extends JpaRepository<Note, Long>{

    List<Note> findAllByParty(Party party);

}
