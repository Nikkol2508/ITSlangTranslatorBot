package com.nikkol2508.ITSlang.repository.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "not_found")
public class NotFound {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "not_found_query")
    private String notFoundQuery;
}
