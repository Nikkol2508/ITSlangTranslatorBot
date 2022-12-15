package com.nikkol2508.it_slang.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class NotFoundResult {

    private String notFoundQuery;
    private int countQuery;

    public String getNotFoundQuery() {
        return notFoundQuery;
    }

    public void setNotFoundQuery(String notFoundQuery) {
        this.notFoundQuery = notFoundQuery;
    }

    public int getCountQuery() {
        return countQuery;
    }

    public void setCountQuery(int countQuery) {
        this.countQuery = countQuery;
    }
}
