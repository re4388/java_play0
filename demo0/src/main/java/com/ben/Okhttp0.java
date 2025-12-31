package com.ben;


import okhttp3.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

public class Okhttp0 {

    public static void main(String[] args) {

    }

    public static void ex4() throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://web-scraping.dev/api/products")
                .build();

        // Synchronous
        Response response = client.newCall(request).execute();

        // Asynchronous
        client.newCall(request).enqueue(new Callback() {
            // Add callback for success:
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println(response.body().string());
            }

            // Add callback for failure:
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });

    }


    public static void ex3() throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://web-scraping.dev/api/products")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                assert response.body() != null;
                System.out.println("Response Body: " + response.body().string());
                System.out.println("Status Code: " + response.code());
            }
        }
    }

    public static void doGet2(String url, Map<String, Object> paramMap) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder requestbuilder = new Request.Builder().get(); //默认就是GET请求，可以不写

        StringBuilder stringBuilder = new StringBuilder(url);

        if (Objects.nonNull(paramMap)) {
            stringBuilder.append("?");
            paramMap.forEach((key, value) -> {
                stringBuilder.append(URLEncoder.encode(key, StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode((String) value, StandardCharsets.UTF_8)).append("&");
            });
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }

        Request request = requestbuilder.url(stringBuilder.toString()).build();
        Response response = okHttpClient.newCall(request).execute();
        assert response.body() != null;
        String string = response.body().string();
        System.out.println(string);

    }


    /**
     * 以get方式调用第三方接口
     *
     * @param url
     */
    public static void doGet1(String url) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .get()//默认就是GET请求，可以不写
                .build();
        Response response = okHttpClient.newCall(request).execute();
        assert response.body() != null;
        String string = response.body().string();
        System.out.println(string);

    }

}
