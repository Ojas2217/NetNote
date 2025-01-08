
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

package client;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ResourceBundle;

import com.google.inject.Injector;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.util.Builder;
import javafx.util.BuilderFactory;
import javafx.util.Callback;
import javafx.util.Pair;

/**
 * Utility class for loading FXML files and injecting dependencies.
 * <p>
 * The {@code MyFXML} class simplifies the process of loading FXML files and
 * initializing their controllers using Guice dependency injection. It supports
 * dynamic construction of controllers and views, ensuring seamless integration
 * between FXML files and the application's dependency injection framework.
 * </p>
 * <p>
 * The {@link FXMLLoader} is configured with a custom {@link MyFactory} to retrieve
 * instances of controllers and other dependencies from the provided {@link Injector}.
 * </p>
 */
public class MyFXML {

    private Injector injector;

    public MyFXML(Injector injector) {
        this.injector = injector;
    }

    /**
     * Loads an FXML file, initializes its controller, and returns a pair containing the controller and its parent node.
     *
     * @param <T>   the type of the controller
     * @param c     the class of the controller
     * @param parts the path parts to locate the FXML file
     * @return a pair containing the controller of type T and the parent node
     * @throws RuntimeException if an IOException occurs during the loading process
     */
    public <T> Pair<T, Parent> load(Class<T> c, ResourceBundle resources, String... parts) {
        try {
            var loader = new FXMLLoader(getLocation(parts), null, null, new MyFactory(), StandardCharsets.UTF_8);
            loader.setResources(resources);
            Parent parent = loader.load();
            T ctrl = loader.getController();
            return new Pair<>(ctrl, parent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private URL getLocation(String... parts) {
        var path = Path.of("", parts).toString();
        return MyFXML.class.getClassLoader().getResource(path);
    }

    /**
     * Custom factory for building and injecting instances of FXML controllers and components.
     * <p>
     * The {@code MyFactory} class implements both {@link BuilderFactory} and
     * {@link Callback} to integrate Guice dependency injection into the FXML loading process.
     * It provides custom logic to retrieve instances of required types from the
     * {@link Injector}, ensuring that all dependencies are properly resolved.
     * </p>
     * <ul>
     *     <li>{@link #getBuilder(Class)}: Creates a {@link Builder} to construct instances of the given type.</li>
     *     <li>{@link #call(Class)}: Directly retrieves an instance of the given type from the {@link Injector}.</li>
     * </ul>
     */
    private class MyFactory implements BuilderFactory, Callback<Class<?>, Object> {

        @Override
        @SuppressWarnings("rawtypes")
        public Builder<?> getBuilder(Class<?> type) {
            return new Builder() {
                @Override
                public Object build() {
                    return injector.getInstance(type);
                }
            };
        }

        @Override
        public Object call(Class<?> type) {
            return injector.getInstance(type);
        }
    }
}
