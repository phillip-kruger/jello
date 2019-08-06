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

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represent a card in a lane on a board
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
@Data @AllArgsConstructor @NoArgsConstructor
@Entity
@NamedQueries({
    @NamedQuery(name = Card.QUERY_FIND_ALL, query = "SELECT c FROM Card c"),
    @NamedQuery(name = Card.QUERY_SEARCH_BY_TITLE, query = "SELECT c FROM Card c WHERE c.title=:title")
})
public class Card implements Serializable {
    private static final long serialVersionUID = -8531040143398374446L;
    
    public static final String QUERY_FIND_ALL = "Note.findAll";
    public static final String QUERY_SEARCH_BY_TITLE = "Note.searchByTitle";
    
    @Id
    @GeneratedValue
    private Long id;
    
    @NotNull @Size(min=2, max=50)
    private String title;
    
    private String description;
    
    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.EAGER)
    private List<Comment> comments;
}
