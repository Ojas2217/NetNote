package commons;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

/**
 * Entity class representing a note.
 * <p>
 * The {@code Note} class is a JPA entity annotated with {@link Entity}, which maps
 * it to a database table. It contains fields for the note's ID, title, and content,
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
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;
    public String title;
    public String content;


    public Note() {
        // for object mapper
    }

    public Note(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
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
}
