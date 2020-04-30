/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.helper;

import com.google.gson.Gson;
import java.util.Map;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author Wei.Cheng
 */
public class HttpClientUtil {

    public static String doGet(String url, Map<String, String> params, String charset) throws Exception {

        String result = null;

        URIBuilder builder = new URIBuilder(url);

        params.forEach((k, v) -> {
            builder.setParameter(k, v);
        });

        HttpClient httpClient = new SSLClient();
        HttpGet httpGet = new HttpGet(builder.build());
        httpGet.addHeader("Content-Type", "application/json");
        HttpResponse response = httpClient.execute(httpGet);
        if (response != null) {
            HttpEntity resEntity = response.getEntity();
            if (resEntity != null) {
                result = EntityUtils.toString(resEntity, charset);
            }
        }

        return result;
    }

    public static String doPost(String url, Map<String, String> params, String charset) throws Exception {

        String result = null;

        HttpClient httpClient = new SSLClient();
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("Content-Type", "application/json");
        StringEntity se = new StringEntity(new Gson().toJson(params));
        se.setContentType("text/json");
        se.setContentEncoding(new BasicHeader("Content-Type", "application/json"));
        httpPost.setEntity(se);
        HttpResponse response = httpClient.execute(httpPost);
        if (response != null) {
            HttpEntity resEntity = response.getEntity();
            if (resEntity != null) {
                result = EntityUtils.toString(resEntity, charset);
            }
        }

        return result;
    }
}
