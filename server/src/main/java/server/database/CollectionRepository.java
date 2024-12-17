package server.database;

import commons.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
/**
 * Repository interface for {@link Collection} entities.
 * <p>
 * The {@code CollectionRepository} interface extends {@link JpaRepository} to provide
 * basic CRUD operations for {@link Collection} entities, with the primary key being of type {@link Long}.
 * The {@link Repository} annotation indicates that this interface is a Spring Data JPA repository
 * and is responsible for interacting with the database to persist and retrieve {@link Collection} objects.
 * </p>
 * <p>
 * No implementation is required as Spring Data JPA automatically generates the necessary
 * implementation based on the method signatures.
 * </p>
 */

@Repository
public interface CollectionRepository  extends JpaRepository<Collection, Long> {

    @Query("SELECT c.id, c.name FROM Collection AS c")
    List<Object[]> findIdAndName();
}
