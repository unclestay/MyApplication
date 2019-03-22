package com.example.chenshuyu.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.chenshuyu.myapplication.CallBack.OnGetGenresCallBack;
import com.example.chenshuyu.myapplication.CallBack.OnGetMoviesCallBack;
import com.example.chenshuyu.myapplication.CallBack.OnMovieClickCallBack;
import com.example.chenshuyu.myapplication.Result.GenreResult;
import com.example.chenshuyu.myapplication.Result.MovieResult;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView moviesList;
    private MoviesAdapter adapter;

    private MoviesFetch moviesFetch;

    private List<GenreResult> movieGenres;

    private boolean isFetchingMovies;
    private int currentPage = 1;

    private String sortBy = MoviesFetch.POPULAR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        moviesFetch = MoviesFetch.getInstance();

        moviesList = findViewById(R.id.movies_list);
        setupOnScrollListener();

        getGenres();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_movies, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort:
                showSortMenu();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showSortMenu() {
        PopupMenu sortMenu = new PopupMenu(this, findViewById(R.id.sort));
        sortMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                /*
                 * Every time we sort, we need to go back to page 1
                 */
                currentPage = 1;

                switch (item.getItemId()) {
                    case R.id.popular:
                        sortBy = MoviesFetch.POPULAR;
                        getMovies(currentPage);
                        return true;
                    case R.id.top_rated:
                        sortBy = MoviesFetch.TOP_RATED;
                        getMovies(currentPage);
                        return true;
                    default:
                        return false;
                }
            }
        });
        sortMenu.inflate(R.menu.menu_movies_sort);
        sortMenu.show();
    }

    private void setupOnScrollListener() {
        final LinearLayoutManager manager = new LinearLayoutManager(this);
        moviesList.setLayoutManager(manager);
        moviesList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int totalItemCount = manager.getItemCount();
                int visibleItemCount = manager.getChildCount();
                int firstVisibleItem = manager.findFirstVisibleItemPosition();

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    if (!isFetchingMovies) {
                        getMovies(currentPage + 1);
                    }
                }
            }
        });
    }

    private void getGenres() {
        moviesFetch.getGenres(new OnGetGenresCallBack() {
            @Override
            public void onSuccess(List<GenreResult> genres) {
                movieGenres = genres;
                getMovies(currentPage);
            }

            @Override
            public void onError() {
                showError();
            }
        });
    }

    private void getMovies(int page) {
        isFetchingMovies = true;
        moviesFetch.getMovies(page, sortBy, new OnGetMoviesCallBack() {
            @Override
            public void onSuccess(int page, List<MovieResult> movies) {
                Log.d("MoviesFetch", "Current Page = " + page);
                if (adapter == null) {
                    adapter = new MoviesAdapter(movies, movieGenres, callback);
                    moviesList.setAdapter(adapter);
                } else {
                    if (page == 1) {
                        adapter.clearMovies();
                    }
                    adapter.appendMovies(movies);
                }
                currentPage = page;
                isFetchingMovies = false;

                setTitle();
            }

            @Override
            public void onError() {
                showError();
            }
        });
    }

    OnMovieClickCallBack callback = new OnMovieClickCallBack() {
        @Override
        public void onClick(MovieResult movie) {
            Intent intent = new Intent(MainActivity.this, MovieActivity.class);
            intent.putExtra(MovieActivity.MOVIE_ID, movie.getId());
            startActivity(intent);
        }
    };

    private void setTitle() {
        switch (sortBy) {
            case MoviesFetch.POPULAR:
                setTitle(getString(R.string.popular));
                break;
            case MoviesFetch.TOP_RATED:
                setTitle(getString(R.string.top_rated));
                break;
        }
    }

    private void showError() {
        Toast.makeText(MainActivity.this, "Need Data.", Toast.LENGTH_SHORT).show();
    }
}
