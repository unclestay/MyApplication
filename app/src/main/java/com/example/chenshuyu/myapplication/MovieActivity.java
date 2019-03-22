package com.example.chenshuyu.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.chenshuyu.myapplication.CallBack.OnGetGenresCallBack;
import com.example.chenshuyu.myapplication.CallBack.OnGetMovieCallBack;
import com.example.chenshuyu.myapplication.CallBack.OnGetReviewsCallBack;
import com.example.chenshuyu.myapplication.Result.GenreResult;
import com.example.chenshuyu.myapplication.Result.MovieResult;
import com.example.chenshuyu.myapplication.Result.Review;

import java.util.ArrayList;
import java.util.List;

public class MovieActivity extends AppCompatActivity {

    public static String MOVIE_ID = "movie_id";

    private static String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w780";


    private ImageView movieBackdrop;
    private TextView movieTitle;
    private TextView movieGenres;
    private TextView movieOverview;
    private TextView movieOverviewLabel;
    private TextView movieReleaseDate;
    private TextView reviewsLabel;
    private RatingBar movieRating;
    private LinearLayout movieReviews;

    private MoviesFetch moviesRepository;
    private int movieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        movieId = getIntent().getIntExtra(MOVIE_ID, movieId);

        moviesRepository = MoviesFetch.getInstance();

        setupToolbar();

        initUI();

        getMovie();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    private void initUI() {
        movieBackdrop = findViewById(R.id.movieDetailsBackdrop);
        movieTitle = findViewById(R.id.movieDetailsTitle);
        movieGenres = findViewById(R.id.movieDetailsGenres);
        movieOverview = findViewById(R.id.movieDetailsOverview);
        movieOverviewLabel = findViewById(R.id.summaryLabel);
        movieReleaseDate = findViewById(R.id.movieDetailsReleaseDate);
        movieRating = findViewById(R.id.movieDetailsRating);
        movieReviews = findViewById(R.id.movieReviews);
        reviewsLabel = findViewById(R.id.reviewsLabel);
    }

    private void getMovie() {
        moviesRepository.getMovie(movieId, new OnGetMovieCallBack() {
            @Override
            public void onSuccess(MovieResult movie) {
                movieTitle.setText(movie.getTitle());
                movieOverviewLabel.setVisibility(View.VISIBLE);
                movieOverview.setText(movie.getOverview());
                movieRating.setVisibility(View.VISIBLE);
                movieRating.setRating(movie.getRating() / 2);
                getGenres(movie);
                movieReleaseDate.setText(movie.getReleaseDate());
                if (!isFinishing()) {
                    Glide.with(MovieActivity.this)
                            .load(IMAGE_BASE_URL + movie.getBackdrop())
                            .apply(RequestOptions.placeholderOf(R.color.colorPrimary))
                            .into(movieBackdrop);
                }

                getReviews(movie);
            }

            @Override
            public void onError() {
                finish();
            }
        });
    }

    private void getGenres(final MovieResult movie) {
        moviesRepository.getGenres(new OnGetGenresCallBack() {
            @Override
            public void onSuccess(List<GenreResult> genres) {
                if (movie.getGenres() != null) {
                    List<String> currentGenres = new ArrayList<>();
                    for (GenreResult genre : movie.getGenres()) {
                        currentGenres.add(genre.getName());
                    }
                    movieGenres.setText(TextUtils.join(", ", currentGenres));
                }
            }

            @Override
            public void onError() {
                showError();
            }
        });
    }




    private void getReviews(MovieResult movie) {
        moviesRepository.getReviews(movie.getId(), new OnGetReviewsCallBack() {
            @Override
            public void onSuccess(List<Review> reviews) {
                reviewsLabel.setVisibility(View.VISIBLE);
                movieReviews.removeAllViews();
                for (Review review : reviews) {
                    View parent = getLayoutInflater().inflate(R.layout.review, movieReviews, false);
                    TextView author = parent.findViewById(R.id.reviewAuthor);
                    TextView content = parent.findViewById(R.id.reviewContent);
                    author.setText(review.getAuthor());
                    content.setText(review.getContent());
                    movieReviews.addView(parent);
                }
            }

            @Override
            public void onError() {
                // Do nothing
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void showError() {
        Toast.makeText(MovieActivity.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
    }
}
