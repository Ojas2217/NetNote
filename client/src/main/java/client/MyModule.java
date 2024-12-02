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

import client.scenes.NoteOverviewCtrl;
import client.scenes.SearchNoteContentCtrl;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Scopes;
import client.scenes.MainCtrl;

/**
 * Guice module for configuring dependency bindings in the application.
 * <p>
 * The {@code MyModule} class implements the {@link Module} interface to define
 * how various application components should be instantiated and managed.
 * It binds controllers such as {@link MainCtrl}, and
 * {@link NoteOverviewCtrl} as singletons, ensuring that only one instance of
 * each is created and reused throughout the application's lifecycle.
 * </p>
 * <p>
 * This module is used during the creation of the {@link com.google.inject.Injector}
 * to enable dependency injection across the application.
 * </p>
 */
public class MyModule implements Module {

    @Override
    public void configure(Binder binder) {
        binder.bind(MainCtrl.class).in(Scopes.SINGLETON);
        binder.bind(NoteOverviewCtrl.class).in(Scopes.SINGLETON);
        binder.bind(SearchNoteContentCtrl.class).in(Scopes.SINGLETON);
    }
}
