/*-
 * Copyright (c) 2023 Red Hat, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.fedoraproject.javadeptools.rpm;

import static org.fedoraproject.javadeptools.rpm.DynamicLinker.RTLD_LAZY;
import static org.fedoraproject.javadeptools.rpm.DynamicLinker.dlopen;
import static org.fedoraproject.javadeptools.rpm.DynamicLinker.dlsym;

import java.lang.foreign.Linker;
import java.lang.foreign.SymbolLookup;
import java.lang.reflect.Proxy;
import java.util.Optional;

/**
 * @author Mikolaj Izdebski
 */
class Native {

    static <T> T load(Class<T> type, SymbolLookup lookup, Linker linker) {
        return type.cast(Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[] { type },
                new NativeInvocationHandler(lookup, linker, type)));
    }

    static <T> T load(Class<T> type, String lib) {
        DynamicLibrary handle = dlopen(lib, RTLD_LAZY);
        SymbolLookup lookup = name -> Optional.of(dlsym(handle, name).ms);
        Linker linker = Linker.nativeLinker();
        return load(type, lookup, linker);

    }

    static <T> T load(Class<T> type) {
        Linker linker = Linker.nativeLinker();
        SymbolLookup lookup = linker.defaultLookup();
        return load(type, lookup, linker);
    }
}