package com.nikkol2508.ITSlang.repository.entity;


import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "slang_translator")
public class SlangTranslator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "search_query_ru")
    private String searchQueryRu;

    @Column(name = "search_query_en")
    private String searchQueryEn;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;
}
