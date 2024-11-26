package server.database;

import commons.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for {@link Note} entities.
 * <p>
 * The {@code NoteRepository} interface extends {@link JpaRepository} to provide
 * basic CRUD operations for {@link Note} entities, with the primary key being of type {@link Long}.
 * The {@link Repository} annotation indicates that this interface is a Spring Data JPA repository
 * and is responsible for interacting with the database to persist and retrieve {@link Note} objects.
 * </p>
 * <p>
 * No implementation is required as Spring Data JPA automatically generates the necessary
 * implementation based on the method signatures.
 * </p>
 */
@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
}
