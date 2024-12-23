package com.dto;

import java.util.List;

import com.entity.Recruitment;

public class ResultSearchDTO {
    private Integer totalPages;
    private List<Recruitment> content;
    private Integer number;

    // Getters and Setters
    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public List<Recruitment> getContent() {
        return content;
    }

    public void setContent(List<Recruitment> content) {
        this.content = content;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}
