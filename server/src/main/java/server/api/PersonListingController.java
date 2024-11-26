// Copyright 2024 Sebastian Proksch
// Licensed under the Apache License, Version 2.0 (the "License"); you may not
// use this file except in compliance with the License. You may obtain a copy of
// the License at
// http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
// WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
// License for the specific language governing permissions and limitations under
// the License.

package server.api;

import java.util.LinkedList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import commons.Person;

/**
 * REST controller for managing a list of people.
 * <p>
 * The {@code PersonListingController} class provides endpoints to list and add {@link Person}
 * objects. It is annotated with {@link RestController} and {@link RequestMapping}, with the
 * base path set to {@code /api/people}.
 * </p>
 * <p>
 * This controller allows clients to:
 * <ul>
 *     <li>Retrieve the list of people ({@link GetMapping}).</li>
 *     <li>Add a new person to the list ({@link PostMapping}).</li>
 * </ul>
 * </p>
 * <p>
 * The list of people is maintained in memory, and new people are added via a {@link PostMapping}
 * request. If the person is not already in the list, they are added to the {@link List}.
 * </p>
 */
@RestController
@RequestMapping("/api/people")
public class PersonListingController {

	private List<Person> people = new LinkedList<>();

	public PersonListingController() {
		people.add(new Person("Mickey", "Mouse"));
		people.add(new Person("Donald", "Duck"));
	}

	@GetMapping("/")
	public List<Person> list() {
		return people;
	}

	/***/
	@PostMapping("/")
	public List<Person> add(@RequestBody Person p) {
		if (!people.contains(p)) {
			people.add(p);
		}
		return people;
	}
}
