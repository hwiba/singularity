package singularity.note.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import singularity.note.domain.Note;

public interface NoteRepository extends JpaRepository<Note, Long>{
	//List<Note> findAllByParty(Party party);
}
