package com.nikkol2508.it_slang.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotFoundResult that = (NotFoundResult) o;
        return countQuery == that.countQuery && Objects.equals(notFoundQuery, that.notFoundQuery);
    }

    @Override
    public int hashCode() {
        return Objects.hash(notFoundQuery, countQuery);
    }
}
