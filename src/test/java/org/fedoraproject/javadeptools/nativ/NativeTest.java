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
package org.fedoraproject.javadeptools.nativ;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * @author Mikolaj Izdebski
 */
public class NativeTest {

    private static interface LibRt {
        int sched_yield();
    }

    @Test
    public void testSchedYield() throws Exception {
        LibRt rt = Native.load(LibRt.class, "librt.so.1");
        int ret = rt.sched_yield();
        assertEquals(0, ret);
    }

    static class Str extends NativeDataStructure {}
    static interface LibC {
        Str strdup(String s);
        int strcmp(String s1, String s2);
        int strcmp(Str s1, String s2);
        int strcmp(String s1, Str s2);
        int strcmp(Str s1, Str s2);
        void strcat(Str s1, String s2);
        void strcat(Str s1, Str s2);
        Str calloc(int m, int n);
        void free(Str s);
    }

    @Test
    public void testStringOps() throws Exception {
        LibC C = Native.load(LibC.class);

        Str s1 = C.strdup("hello");
        assertEquals(0, C.strcmp(s1, "hello"));
        assertEquals(1, C.strcmp(s1, "good bye"));
        assertEquals(0, C.strcmp(s1, s1));

        Str s2 = C.calloc(10, 10);
        C.strcat(s2, s1);
        C.strcat(s2, " world");
        assertEquals(0, C.strcmp("hello world", s2));

        C.free(s1);
        C.free(s2);

        assertEquals(0, C.strcmp("xy", "x" + 'y'));
    }
}
