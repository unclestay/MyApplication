package com.example.chenshuyu.myapplication.CallBack;

import com.example.chenshuyu.myapplication.Result.MovieResult;

import java.util.List;

public interface OnGetMoviesCallBack {

    void onSuccess(int page, List<MovieResult> movies);

    void onError();
}
