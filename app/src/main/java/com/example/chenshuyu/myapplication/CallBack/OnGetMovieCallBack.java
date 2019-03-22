package com.example.chenshuyu.myapplication.CallBack;

import com.example.chenshuyu.myapplication.Result.MovieResult;

public interface OnGetMovieCallBack {

    void onSuccess(MovieResult movie);

    void onError();
}
