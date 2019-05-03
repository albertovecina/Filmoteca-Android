package com.vsa.filmoteca.data.repository.ws;

import com.vsa.filmoteca.FilmotecaApplication;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by albertovecinasanchez on 8/3/16.
 */
public class WSClient {

    private static final long TIMEOUT = 60000;
    public static final String CACHE_DIRECTORY = "HttpCache";

    private static FilmotecaInterface sRetrofitClient;

    private static CacheRequestInterceptor sCacheRequestInterceptor = new CacheRequestInterceptor();

    public static FilmotecaInterface getClient(CacheRequestInterceptor.CachePolicy cachePolicy) {
        if (sRetrofitClient == null) {
            int cacheSize = 10 * 1024 * 1024; // 10 MiB
            File cacheDirectory = new File(FilmotecaApplication.getInstance().getCacheDir().getAbsolutePath(), CACHE_DIRECTORY);
            Cache cache = new Cache(cacheDirectory, cacheSize);
            OkHttpClient client = new OkHttpClient.Builder()
                    .cache(cache)
                    .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                    .addInterceptor(sCacheRequestInterceptor)
                    .build();
            Retrofit retrofit = new Retrofit.Builder()
                    .client(client)
                    .baseUrl(Environment.BASE_URL_FILMOTECA)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();

            sRetrofitClient = retrofit.create(FilmotecaInterface.class);
        }
        if (sCacheRequestInterceptor != null)
            sCacheRequestInterceptor.setCachePolicy(cachePolicy);
        return sRetrofitClient;
    }

}
