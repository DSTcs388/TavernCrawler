package com.example.taverncrawler;

import com.loopj.android.http.*;

/**
 * An {@link RestClient} for communicating with Barzz API.
 */
public class RestClient {
    public static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(url, params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(url, params, responseHandler);
    }


}
