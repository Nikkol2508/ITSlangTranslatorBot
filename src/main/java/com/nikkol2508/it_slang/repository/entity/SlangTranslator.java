package com.nikkol2508.it_slang.repository.entity;


import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "slang_translator")
public class SlangTranslator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "search_query")
    private String searchQuery;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SlangTranslator that = (SlangTranslator) o;
        return id == that.id && Objects.equals(searchQuery, that.searchQuery) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, searchQuery, description);
    }
}
