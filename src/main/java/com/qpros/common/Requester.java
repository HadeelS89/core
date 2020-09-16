package com.qpros.common;

import com.google.gson.Gson;
import okhttp3.*;

import javax.annotation.Nullable;
import java.util.Map;

public class Requester {

    private final OkHttpClient httpClient = new OkHttpClient();
    public ResponseBody responseBody;
    public int statusCode;
    LogManger logManger= new LogManger(Requester.class.getSimpleName());


    public Requester() {

    }

    public Request request(Verb verb, String uri, String endpoint, Map<String, String> params, @Nullable RequestBody body) {

        if (!params.isEmpty()) {
            endpoint = endpoint + ParameterizedEndPoint(params);
        }
        Request.Builder request = new Request.Builder()
                .url(String.format("%s%s",uri,endpoint))
                .addHeader("accept", "application/json");
        switch (verb) {
            case GET:
                request.get();
                break;
            case POST:
                request.addHeader("Content-Type", "application/json");

                if (body != null) {
                    request.post(body);
                }
                break;
            case DELETE:
                request.delete();
                break;
        }
        return request.build();
    }

    public String ParameterizedEndPoint(Map<String, String> params) {
        StringBuilder paramterizedEndPoint = new StringBuilder();
        params.forEach((k, v) -> paramterizedEndPoint.append(k).append("=").append(v).append("&"));
        return paramterizedEndPoint.substring(0, paramterizedEndPoint.toString().length() - 1);
    }

    public String requestBodybuilder(Object obj) {

        Gson gson = new Gson();
        return gson.toJson(obj);
    }


    public Response response(Request request) throws Exception {
        logManger.INFO(request.url().toString());

        try (Response response = httpClient.newCall(request).execute()) {
            statusCode = response.code();
            responseBody = response.body();

            //if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            // Get response object
            return response;
        }

    }


}
