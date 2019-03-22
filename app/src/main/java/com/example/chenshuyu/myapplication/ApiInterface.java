package com.example.chenshuyu.myapplication;

import com.example.chenshuyu.myapplication.Result.GenresResult;
import com.example.chenshuyu.myapplication.Result.MovieResult;
import com.example.chenshuyu.myapplication.Result.MoviesResult;
import com.example.chenshuyu.myapplication.Result.ReviewResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("movie/popular")
    Call<MoviesResult> getPopularMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    @GET("genre/movie/list")
    Call<GenresResult> getGenres(
            @Query("api_key") String apiKey,
            @Query("language") String language
    );

    @GET("movie/top_rated")
    Call<MoviesResult> getTopRatedMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    @GET("movie/{movie_id}")
    Call<MovieResult> getMovie(
            @Path("movie_id") int id,
            @Query("api_key") String apiKEy,
            @Query("language") String language
    );


    @GET("movie/{movie_id}/reviews")
    Call<ReviewResult> getReviews(
            @Path("movie_id") int id,
            @Query("api_key") String apiKEy,
            @Query("language") String language
    );
}
