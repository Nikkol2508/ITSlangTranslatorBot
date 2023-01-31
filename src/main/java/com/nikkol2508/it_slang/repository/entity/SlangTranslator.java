package com.nikkol2508.it_slang.repository.entity;


import lombok.*;

import javax.persistence.*;

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
}
