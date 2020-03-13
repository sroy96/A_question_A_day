package com.email.classify.emailpoll.Utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AccessTokenGeneratorUtils {

    public String getAccessToken() {

        try
        {
            log.info("Generating New Access Token ");
            Map<String,Object> params = new LinkedHashMap<String,Object>();
            params.put("grant_type",CommonConstants.GRANT_TYPE);
            params.put("client_id",CommonConstants.CLIENT_ID);
            params.put("client_secret",CommonConstants.CLIENT_SECRET);
            params.put("refresh_token",CommonConstants.REFRESH_TOKEN);

            StringBuilder postData = new StringBuilder();
            for(Map.Entry<String,Object> param : params.entrySet())
            {
                if(postData.length() != 0)
                {
                    postData.append('&');
                }
                postData.append(URLEncoder.encode(param.getKey(),"UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()),"UTF-8"));
            }
            byte[] postDataBytes = postData.toString().getBytes(CommonConstants.CHAR_SET);

            URL url = new URL(CommonConstants.OAUTH_URI);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setDoOutput(true);
            con.setUseCaches(false);
            con.setRequestMethod(CommonConstants.METHOD);
            con.getOutputStream().write(postDataBytes);

            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuffer buffer = new StringBuffer();
            for (String line = reader.readLine(); line != null; line = reader.readLine())
            {
                buffer.append(line);
            }

            JSONObject json = new JSONObject(buffer.toString());
            String accessToken = json.getString(CommonConstants.AUTH_TYPE);
            return accessToken;
        }
        catch (Exception ex)
        {
            log.error("Failed to Authenticate");
            ex.printStackTrace();
        }
        return null;
    }
}
