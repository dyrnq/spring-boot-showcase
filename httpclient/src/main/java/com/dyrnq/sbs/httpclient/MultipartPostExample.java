package com.dyrnq.sbs.httpclient;

import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;

@Slf4j
public class MultipartPostExample {
    public static void main(String[] args) {
        // 创建 HttpClient
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {

            // 创建 POST 请求
            HttpPost httpPost = new HttpPost("http://127.0.0.1:5588/upload-part"); // 替换为您的目标 URL

            // 创建 MultipartEntityBuilder
            MultipartEntityBuilder builder = MultipartEntityBuilder.create()
                    .setCharset(StandardCharsets.UTF_8);

            // 添加字符串字段
            builder.addTextBody("token", "my_token中文", ContentType.TEXT_PLAIN);
            builder.addTextBody("sign", "my_sign", ContentType.TEXT_PLAIN);

            // 添加文件字段
            File file = new File("/tmp/random_file_14581"); // 替换为您的文件路径
            builder.addBinaryBody("file", file, ContentType.APPLICATION_OCTET_STREAM, file.getName());

            // 构建请求实体
            HttpEntity multipart = builder.build();
            httpPost.setEntity(multipart);

            // 执行请求
            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                System.out.println("Response Code: " + response.getCode());
                // 处理响应...
                String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                System.out.println("Response Body: " + responseBody);
            }

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
