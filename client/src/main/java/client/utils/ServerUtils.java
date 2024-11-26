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
package client.utils;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import commons.ExceptionType;
import commons.ProcessOperationException;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;

import commons.Note;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;

public class ServerUtils {

	private static final String SERVER = "http://localhost:8080/";

	public ServerUtils() {
	}
	public boolean isServerAvailable() {
		try {
			ClientBuilder.newClient(new ClientConfig()) //
					.target(SERVER) //
					.request(APPLICATION_JSON) //
					.get();
		} catch (ProcessingException e) {
			if (e.getCause() instanceof ConnectException) {
				return false;
			}
		}
		return true;
	}

	/**
	 * GET method
	 *
	 * @param endpoint the path to the specified item
	 * @param type the type
	 * @return the entity
	 * @param <T> the type of the entity
	 * @throws ProcessOperationException if the operation fails
	 */
	protected <T> T get(String endpoint, GenericType<T> type) throws ProcessOperationException {
		Response response = ClientBuilder.newClient(new ClientConfig())
				.target(this.SERVER).path(endpoint)
				.request(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
				.get();

		if (response.getStatus() == 200 ||
			response.getStatus() == 201 ||
			response.getStatus() == 204)
			return response.readEntity(type);

		throw new ProcessOperationException("Operation",
				response.getStatus(), ExceptionType.INVALID_CREDENTIALS);
	}

	/**
	 * DELETE method
	 *
	 * @param endpoint the path to the specified item
	 * @param type the type
	 * @return the deleted entity
	 * @param <T> the type of the entity
	 * @throws ProcessOperationException if the operation fails
	 */
	protected <T> T delete(String endpoint, GenericType<T> type) throws ProcessOperationException {
		Response response = ClientBuilder.newClient(new ClientConfig())
				.target(this.SERVER).path(endpoint)
				.request(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
				.delete();
		if (response.getStatus() == 200 || response.getStatus() == 201)
			return response.readEntity(type);
		throw new ProcessOperationException("Operation",
				response.getStatus(), ExceptionType.INVALID_CREDENTIALS);
	}

	/**
	 * PUT method
	 *
	 * @param endpoint the path to the specified item
	 * @param body the object
	 * @param type the type
	 * @return the entity
	 * @param <T> the type of the entity
	 * @throws ProcessOperationException if the operation fails
	 */
	protected <T> T put(String endpoint, T body, GenericType<T> type) throws ProcessOperationException {
		Response response = ClientBuilder.newClient(new ClientConfig())
				.target(this.SERVER).path(endpoint)
				.request(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
				.put(Entity.entity(body, APPLICATION_JSON));
		if (response.getStatus() == 200 || response.getStatus() == 201)
			return response.readEntity(type);
		throw new ProcessOperationException("Operation",
				response.getStatus(), ExceptionType.INVALID_CREDENTIALS);
	}

	/**
	 * POST method
	 *
	 * @param endpoint the endpoint
	 * @param body the body
	 * @param type the type
	 * @return the entity or object
	 * @param <T> the type
	 * @throws ProcessOperationException if the operation fails
	 */
	protected <T> T post(String endpoint, T body, GenericType<T> type)
			throws ProcessOperationException {
		Response response = ClientBuilder.newClient(new ClientConfig()) //
				.target(this.SERVER).path(endpoint)
				.request(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
				.post(Entity.entity(body, APPLICATION_JSON));
		if (response.getStatus() == 200 || response.getStatus() == 201)
			return response.readEntity(type);
		throw new ProcessOperationException("Operation",
				response.getStatus(), ExceptionType.INVALID_CREDENTIALS);
	}
}