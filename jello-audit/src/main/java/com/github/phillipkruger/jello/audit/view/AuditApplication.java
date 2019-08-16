/*
 * Copyright 2019 Phillip Kruger (phillip.kruger@redhat.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.phillipkruger.jello.audit.view;

import java.util.HashMap;
import java.util.Map;
import javax.mvc.engine.ViewEngine;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * Configure the MVC Application
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
@ApplicationPath("resources")
public class AuditApplication extends Application {

//    @Override
//    public Map<String, Object> getProperties() {
//        final Map<String, Object> map = new HashMap<>();
//        map.put(ViewEngine.VIEW_FOLDER, "/jsp/");
//        return map;
//    }
}
