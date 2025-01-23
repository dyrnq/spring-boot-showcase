package com.dyrnq.sbs.okhttp;


import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.File;
import java.io.IOException;

@Slf4j
public class MultipartPostExample {
    public static void main(String[] args) {
        // 创建 OkHttpClient
        OkHttpClient client = new OkHttpClient();

        // 设置目标 URL
        String url = "http://127.0.0.1:5588/upload-part"; // 替换为您的目标 URL

        // 创建文件对象
        File file = new File("/tmp/random_file_14581"); // 替换为您的文件路径

        // 创建请求体
        MultipartBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("token", "my_token中文")
                .addFormDataPart("sign", "my_sign")
                .addFormDataPart("file", file.getName(),
                        RequestBody.create(file, MediaType.parse("application/octet-stream")))
                .build();

        // 创建 POST 请求
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        // 执行请求并处理响应
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            // 输出响应代码和响应体
            System.out.println("Response Code: " + response.code());
            System.out.println("Response Body: " + response.body().string());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}

