package com.consume.consumeApi.controller;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class sendOtp {

    public void sendOtpToPhone(String phone, String otp) {
        try {
            URL url = new URL("https://api.mobitechtechnologies.com/sms/sendsms");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("h_api_key", "e7e486b6a627a27fa938c8c8cbb12e47ca15bfdf915a63df28483980a5dc117f");
            con.setDoOutput(true);

            Map<String, String> data = new HashMap<>();
            data.put("mobile", phone);
            data.put("response_type", "json");
            data.put("sender_name", "23107");
            data.put("service_id", "0");
            data.put("message", "Your otp Number is " + otp);

            String jsonInputString = new Gson().toJson(data);

            try (OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;

                while ((responseLine = br.readLine()) != null){
                    response.append(responseLine.trim());

                }
                System.out.println(response);
                if (response.toString().contains("Success")){
                    System.out.println("You otp has been sent");
                }else {
                    System.out.println("Failed to send Otp");
                }

            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        }
}
