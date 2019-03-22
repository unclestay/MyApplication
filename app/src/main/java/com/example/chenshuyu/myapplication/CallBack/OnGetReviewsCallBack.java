package com.example.chenshuyu.myapplication.CallBack;

import com.example.chenshuyu.myapplication.Result.Review;

import java.util.List;

public interface OnGetReviewsCallBack {
    void onSuccess(List<Review> reviews);

    void onError();
}
