package commons;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

/**
 * Entity class representing a Collection.
 * <p>
 * The {@code Collection} class is a JPA entity annotated with {@link Entity}, which maps
 * it to a database table. It contains fields for the collection's ID, name, and content,
 * along with constructors, and overrides for {@link Object#equals(Object)},
 * {@link Object#hashCode()}, and {@link Object#toString()}.
 * </p>
 * <ul>
 *     <li>{@link Id} and {@link GeneratedValue} annotations are used to specify
 *         that the {@code id} field is the primary key and its value is
 *         automatically generated.</li>
 *     <li>The no-argument constructor is provided for frameworks like object mappers
 *         that require it for instantiation.</li>
 * </ul>
 */
@Entity
public class Collection {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    @OneToMany(mappedBy = "collection", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Note> notes;

    public Collection(String name, List<Note> notes) {
        this.name = name;
        this.notes = notes;
    }

    /*
        for the default collection
     */
    public Collection(String name) {
        this.name = name;
    }

    public Collection() {
        //for object mapper
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    public void addNote(Note note) {
        notes.add(note);
    }

    public void removeNote(Note note) {
        notes.remove(note);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }

    public void removeNoteById(long id) {
        this.getNotes().removeIf(note -> note.getId() == id);
    }
}
