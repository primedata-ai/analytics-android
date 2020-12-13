/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2014 Segment.io, Inc.
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.segment.analytics;

import android.util.Base64;

import com.segment.analytics.core.BuildConfig;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Abstraction to customize how connections are created. This is can be used to point our SDK at
 * your proxy server for instance.
 */
public class ConnectionFactory {

    private static final int DEFAULT_READ_TIMEOUT_MILLIS = 20 * 1000; // 20s
    private static final int DEFAULT_CONNECT_TIMEOUT_MILLIS = 15 * 1000; // 15s
    static final String USER_AGENT = "analytics-android/" + BuildConfig.VERSION_NAME;

    private String authorizationHeader(String writeKey) {
        return "Basic " + Base64.encodeToString((writeKey + ":").getBytes(), Base64.NO_WRAP);
    }

    /**
     * Return a {@link HttpURLConnection} that reads JSON formatted project settings.
     */
    public HttpURLConnection projectSettings(String writeKey) throws IOException {
        return openConnection(
                "https://cdn-settings.segment.com/v1/projects/" + writeKey + "/settings");
    }

    /**
     * Return a {@link HttpURLConnection} that writes batched payloads to {@code
     * https://api.segment.io/v1/import}.
     */
    public HttpURLConnection upload(String host, String writeKey, String sourceKey, String path) throws IOException {
        String url = String.format("%s%s", host, path);
        HttpURLConnection connection = openConnection(url);
        connection.setRequestProperty("x-client-access-token", writeKey);
        connection.setRequestProperty("x-client-id", sourceKey);
        connection.setRequestProperty("content-type", "text/plain;charset=UTF-8");
        connection.setRequestProperty("accept", "application/json");
//        connection.setRequestProperty("Content-Encoding", "gzip");
        connection.setDoOutput(true);
//        connection.setChunkedStreamingMode(0);
        return connection;
    }

    /**
     * #projectSettings(String)}.
     */
    protected HttpURLConnection openConnection(String url) throws IOException {
        URL requestedURL;

        try {
            requestedURL = new URL(url);
        } catch (MalformedURLException e) {
            throw new IOException("Attempted to use malformed url: " + url, e);
        }

        HttpURLConnection connection = (HttpURLConnection) requestedURL.openConnection();
        connection.setRequestMethod("POST");
//        connection.setConnectTimeout(DEFAULT_CONNECT_TIMEOUT_MILLIS);
//        connection.setReadTimeout(DEFAULT_READ_TIMEOUT_MILLIS);
        connection.setRequestProperty("User-Agent", USER_AGENT);
        connection.setDoInput(true);
        return connection;
    }
}
