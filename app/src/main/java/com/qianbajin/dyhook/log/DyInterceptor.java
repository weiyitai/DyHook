package com.qianbajin.dyhook.log;

import java.io.EOFException;
import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
/**
 * ----------------------
 * 代码千万行
 * 注释第一行
 * 代码不注释
 * 改bug两行泪
 * -----------------------
 *
 * @author  weiyitai
 * @date 2019/12/27
 */
public class DyInterceptor implements Interceptor {

    private static final String TAG = "DyInterceptor";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        logRequest(request);
        Response response = chain.proceed(request);
        return logResponse(response);
    }

    private Response logResponse(Response response) {
        ResponseBody responseBody = response.body();
        try {
            if (responseBody != null) {
                Headers headers = response.headers();
                int size = headers.size();
                StringBuilder builder = new StringBuilder(size * 16);
                for (int i = 0; i < size; i++) {
                    builder.append(headers.name(i)).append(':').append(headers.value(i)).append(',');
                }
                Logger.d(TAG, "response headers:" + builder.toString());
                BufferedSource source = responseBody.source();
                source.request(Long.MAX_VALUE);
                Buffer buffer = source.getBuffer();
                if (!isPlaintext(buffer)) {
                    Logger.d(TAG, "response binary " + buffer.size() + "-byte body omitted");
                    buffer.close();
                    return response;
                }
                String resp = buffer.clone().readUtf8();
                buffer.close();
                long requestAtMillis = response.sentRequestAtMillis();
                long responseAtMillis = response.receivedResponseAtMillis();
                String ts = response.request().header("ts");
                long takeTime = responseAtMillis - requestAtMillis;
                Logger.d(TAG, takeTime + "ms response code:" + response.code() + " responseBody:" + resp);
                return response;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    private static boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            // Truncated UTF-8 sequence.
            return false;
        }
    }

    private void logRequest(Request request) {
        String url = request.url().toString();
        String body = bodyToString(request.body());
        Headers headers = request.headers();
        int size = headers.size();
        StringBuilder builder = new StringBuilder(size * 16);
        for (int i = 0; i < size; i++) {
            String name = headers.name(i);
            String value = headers.value(i);
            builder.append(" name:").append(name).append(" value:").append(value);
        }
        Logger.d(TAG, "request header:" + builder.toString());
        Logger.d(TAG, "request url:" + url + " request body:" + body);
    }

    private String bodyToString(final RequestBody requestBody) {
        if (requestBody == null) {
            return "requestBody is empty";
        }
        try {
            MediaType contentType = requestBody.contentType();
            if (contentType != null) {
                Logger.d(TAG, "contentType:" + contentType);
                if (contentType.toString().contains("multipart/form-data")) {
                    return "multipart body";
                }
            }
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "something error when show requestBody.";
        }
    }
}
