
/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package commons;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * Entity class representing a person.
 * <p>
 * The {@code Person} class is a JPA entity annotated with {@link Entity}, which maps
 * it to a database table. It contains fields for the person's ID, first name, and last name,
 * along with constructors, and overrides for {@link Object#equals(Object)},
 * {@link Object#hashCode()}, and {@link Object#toString()}.
 * </p>
 * <ul>
 *     <li>{@link Id} and {@link GeneratedValue} annotations are used to specify
 *         that the {@code id} field is the primary key and its value is automatically generated.</li>
 *     <li>The no-argument constructor is provided for frameworks like object mappers
 *         that require it for instantiation.</li>
 * </ul>
 */
@Entity
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    public String firstName;
    public String lastName;

    @SuppressWarnings("unused")
    public Person() {
        // for object mapper
    }

    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
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
