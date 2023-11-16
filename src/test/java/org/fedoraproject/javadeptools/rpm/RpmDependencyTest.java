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

import static org.junit.Assert.assertEquals;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

/**
 * @author Mikolaj Izdebski
 */
public class RpmDependencyTest {
    @Test
    public void testDependencies() throws Exception {
        Path path = Paths.get("src/test/resources/rpm/rpmtags-1-1.noarch.rpm");
        RpmInfo info = new RpmPackage(path).getInfo();

        RpmDependency dep0 = info.getRecommends().get(0);
        assertEquals("((ant and ivy) or maven >= 3.0.4)", dep0.toString());
        assertEquals("((ant and ivy) or maven >= 3.0.4)", dep0.getName());
        assertEquals("", dep0.getEVR());

        RpmDependency dep1 = info.getRecommends().get(1);
        assertEquals("bar = 23-4.5", dep1.toString());
        assertEquals("bar", dep1.getName());
        assertEquals("23-4.5", dep1.getEVR());

        RpmDependency dep2 = info.getRecommends().get(2);
        assertEquals("baz >= 3333333333:444444444444444444444-xaxaxayyyy.5517~77+8", dep2.toString());
        assertEquals("baz", dep2.getName());
        assertEquals("3333333333:444444444444444444444-xaxaxayyyy.5517~77+8", dep2.getEVR());

        RpmDependency dep3 = info.getRecommends().get(3);
        assertEquals("foo < 1", dep3.toString());
        assertEquals("foo", dep3.getName());
        assertEquals("1", dep3.getEVR());

        RpmDependency dep4 = info.getRecommends().get(4);
        assertEquals("nethack", dep4.toString());
        assertEquals("nethack", dep4.getName());
        assertEquals("", dep4.getEVR());
    }
}
