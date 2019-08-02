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

package com.github.phillipkruger.jello;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represent a comment on a card
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
@Data @AllArgsConstructor @NoArgsConstructor
public class Comment {
    private String comment;
    private LocalDateTime madeOn;
    private String madeBy;
}