package singularity.note.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import singularity.note.domain.Note;
import singularity.party.domain.Party;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long>{

    List<Note> findAllByParty(Party party);

}
