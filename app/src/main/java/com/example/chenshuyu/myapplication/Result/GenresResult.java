package com.example.chenshuyu.myapplication.Result;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GenresResult {

    @SerializedName("genres")
    @Expose
    private List<GenreResult> genres;

    public List<GenreResult> getGenres() {
        return genres;
    }

    public void setGenres(List<GenreResult> genres) {
        this.genres = genres;
    }
}
