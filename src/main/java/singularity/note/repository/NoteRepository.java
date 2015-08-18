package singularity.note.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import singularity.domain.Note;

public interface NoteRepository extends JpaRepository<Note, Long>{
	List<Note> findAllByParty(Party party);
}
