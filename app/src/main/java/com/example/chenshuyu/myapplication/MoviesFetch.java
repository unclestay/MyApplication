package com.example.chenshuyu.myapplication;

import com.example.chenshuyu.myapplication.CallBack.OnGetGenresCallBack;
import com.example.chenshuyu.myapplication.CallBack.OnGetMovieCallBack;
import com.example.chenshuyu.myapplication.CallBack.OnGetMoviesCallBack;
import com.example.chenshuyu.myapplication.CallBack.OnGetReviewsCallBack;
import com.example.chenshuyu.myapplication.Result.GenresResult;
import com.example.chenshuyu.myapplication.Result.MovieResult;
import com.example.chenshuyu.myapplication.Result.MoviesResult;
import com.example.chenshuyu.myapplication.Result.ReviewResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MoviesFetch {
    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String LANGUAGE = "en-US";
    private static final String API_KEY = "b53db5686812445c561290b874b6fb97";
    private static MoviesFetch repository;

    private ApiInterface api;

    public static final String POPULAR = "popular";
    public static final String TOP_RATED = "top_rated";

    private MoviesFetch(ApiInterface api) {
        this.api = api;
    }

    public static MoviesFetch getInstance() {
        if (repository == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            repository = new MoviesFetch(retrofit.create(ApiInterface.class));
        }

        return repository;
    }

    public void getMovies(int page, String sortBy, final OnGetMoviesCallBack callback) {
        Callback<MoviesResult> call = new Callback<MoviesResult>() {
            @Override
            public void onResponse(Call<MoviesResult> call, Response<MoviesResult> response) {
                if (response.isSuccessful()) {
                    MoviesResult moviesResult = response.body();
                    if (moviesResult != null && moviesResult.getMovies() != null) {
                        callback.onSuccess(moviesResult.getPage(), moviesResult.getMovies());
                    } else {
                        callback.onError();
                    }
                } else {
                    callback.onError();
                }
            }

            @Override
            public void onFailure(Call<MoviesResult> call, Throwable t) {
                callback.onError();
            }
        };

        switch (sortBy) {
            case TOP_RATED:
                api.getTopRatedMovies(API_KEY, LANGUAGE, page)
                        .enqueue(call);
                break;
            case POPULAR:
            default:
                api.getPopularMovies(API_KEY, LANGUAGE, page)
                        .enqueue(call);
                break;
        }
    }

    public void getGenres(final OnGetGenresCallBack callback) {
        api.getGenres(API_KEY, LANGUAGE)
                .enqueue(new Callback<GenresResult>() {
                    @Override
                    public void onResponse(Call<GenresResult> call, Response<GenresResult> response) {
                        if (response.isSuccessful()) {
                            GenresResult genresResult = response.body();
                            if (genresResult != null && genresResult.getGenres() != null) {
                                callback.onSuccess(genresResult.getGenres());
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(Call<GenresResult> call, Throwable t) {
                        callback.onError();
                    }
                });
    }

    public void getMovie(int movieId, final OnGetMovieCallBack callback) {
        api.getMovie(movieId, API_KEY, LANGUAGE)
                .enqueue(new Callback<MovieResult>() {
                    @Override
                    public void onResponse(Call<MovieResult> call, Response<MovieResult> response) {
                        if (response.isSuccessful()) {
                            MovieResult movie = response.body();
                            if (movie != null) {
                                callback.onSuccess(movie);
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieResult> call, Throwable t) {
                        callback.onError();
                    }
                });
    }



    public void getReviews(int movieId, final OnGetReviewsCallBack callback) {
        api.getReviews(movieId, API_KEY, LANGUAGE)
                .enqueue(new Callback<ReviewResult>() {
                    @Override
                    public void onResponse(Call<ReviewResult> call, Response<ReviewResult> response) {
                        if (response.isSuccessful()) {
                            ReviewResult reviewResult = response.body();
                            if (reviewResult != null && reviewResult.getReviews() != null) {
                                callback.onSuccess(reviewResult.getReviews());
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(Call<ReviewResult> call, Throwable t) {
                        callback.onError();
                    }
                });
    }
}
