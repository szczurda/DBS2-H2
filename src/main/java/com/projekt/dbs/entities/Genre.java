package com.projekt.dbs.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class Genre {
    @Id
    @NotNull
    @GeneratedValue
    private Long genreId;

    @NotNull
    private String genreName;

    @NotNull
    private Integer ageAppropriate;

    private Integer booksUnderGenre;

    public Genre() {
    }

    public Genre(Long genreId, String genreName, Integer ageAppropriate, Integer booksUnderGenre) {
        this.genreId = genreId;
        this.genreName = genreName;
        this.ageAppropriate = ageAppropriate;
        this.booksUnderGenre = booksUnderGenre;
    }

    public Long getGenreId() {
        return genreId;
    }

    public void setGenreId(Long genreId) {
        this.genreId = genreId;
    }

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }

    public Integer getAgeAppropriate() {
        return ageAppropriate;
    }

    public void setAgeAppropriate(Integer ageAppropriate) {
        this.ageAppropriate = ageAppropriate;
    }

    public Integer getBooksUnderGenre() {
        return booksUnderGenre;
    }

    public void setBooksUnderGenre(Integer booksUnderGenre) {
        this.booksUnderGenre = booksUnderGenre;
    }
}
