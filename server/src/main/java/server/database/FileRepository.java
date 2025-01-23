package server.database;

import commons.File;
import commons.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for {@link File} entities.
 * <p>
 * The {@code FileRepository} interface extends {@link JpaRepository} to provide
 * basic CRUD operations for {@link File} entities, with the primary key being of type {@link Long}.
 * The {@link Repository} annotation indicates that this interface is a Spring Data JPA repository
 * and is responsible for interacting with the database to persist and retrieve {@link File} objects.
 * </p>
 * <p>
 * No implementation is required as Spring Data JPA automatically generates the necessary
 * implementation based on the method signatures.
 * </p>
 */
@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    Optional<File> findByIdAndNote(Long id, Note note);
}
