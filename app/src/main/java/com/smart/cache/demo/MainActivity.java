package com.smart.cache.demo;


import android.os.Bundle;
import android.widget.VideoView;

import com.smart.cache.CacheListener;
import com.smart.cache.HttpProxyCacheServer;
import com.smart.utils.LogUtils;

import java.io.File;

import androidx.appcompat.app.AppCompatActivity;

import static com.smart.cache.Constants.LOG_TAG;

public class MainActivity extends AppCompatActivity implements CacheListener {


    String url = "http://x128.songtasty.com/attachment/20191106/3BhSDvxW5dgPt7NIUfL8.mp3";

//    String url = "https://cdn.changguwen.com/cms/media/2019812/e69e3268-6d24-4446-87c1-3569a66ddf71-1565599111905.mp4";

    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        videoView = findViewById(R.id.videoView);

        checkCachedState();
        startVideo();
    }


    private void checkCachedState() {
        HttpProxyCacheServer proxy = AppApplication.getProxy(this);
        boolean fullyCached = proxy.isCached(url);
        if (fullyCached) {
            LogUtils.e(LOG_TAG, "progress 100");
        }
    }

    private void startVideo() {
        HttpProxyCacheServer proxy = AppApplication.getProxy(this);
        proxy.registerCacheListener(this, url);
        String proxyUrl = proxy.getProxyUrl(url);
        LogUtils.e(LOG_TAG, "Use proxy url " + proxyUrl + " instead of original url " + url);
        videoView.setVideoPath(proxyUrl);
        videoView.start();
    }

    @Override
    public void onCacheAvailable(File cacheFile, String url, int percentsAvailable) {
        LogUtils.e(LOG_TAG, String.format("onCacheAvailable. percents: %d, file: %s, url: %s", percentsAvailable, cacheFile, url));
    }
}
