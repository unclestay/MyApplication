package com.example.chenshuyu.myapplication.CallBack;

import com.example.chenshuyu.myapplication.Result.GenreResult;

import java.util.List;

public interface OnGetGenresCallBack {
    void onSuccess(List<GenreResult> genres);

    void onError();
}
