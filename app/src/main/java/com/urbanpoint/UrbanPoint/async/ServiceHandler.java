package com.urbanpoint.UrbanPoint.async;

import android.content.Context;
import android.util.Log;


import com.urbanpoint.UrbanPoint.utils.Constants;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class ServiceHandler {

    static String response = null;
    //public final static int GET = 1;
    // public final static int POST = 2;

    public final static String GET = "GET";
    public final static String POST = "POST";

    public ServiceHandler() {

    }

    /**
     * Making service call
     *
     * @url - url to make request
     * @method - http request method
     */
    public String makeServiceCall(String url, String method) {
        return this.makeServiceCall(url, method, null, null);
    }

    /**
     * Making service call
     *
     * @url - url to make request
     * @method - http request method
     * @params - http request params
     */
    public String makeServiceCall(String url, String method,
                                  String params, Context context) {


        // Checking http request method type
        if (method.equalsIgnoreCase(POST)) {
            // adding post params
            if (params != null && url != null) {
                response = doPost(params, url);
                // httpPost.setEntity(new StringEntity(params, "UTF-8"));
            }
        } else {
            response = doGet(url);
        }
        return response;


    }


    private String doPost(String jsonParam, String serviceUrl) {
        Log.e("doPost", "doPost called : URL : " + serviceUrl + "| jsonParam : " + jsonParam.toString());
        HttpURLConnection urlConn;
        DataOutputStream printout;
        DataInputStream input;
        URL obj = null;
        String tempResponse = "";
        boolean error = false;
        try {
            obj = new URL(serviceUrl);
            urlConn = (HttpURLConnection) obj.openConnection();
            //add reuqest header
            urlConn.setRequestMethod(POST);
            // urlConn.setRequestProperty(Common.HEADER_VALUE_USER_AGENT, Common.ANDROID);
            urlConn.setConnectTimeout(Constants.CONNECTION_TIMEOUT);
            urlConn.setReadTimeout(Constants.CONNECTION_TIMEOUT);
            urlConn.setRequestProperty(Constants.HEADER_CONTENT_TYPE, Constants.HEADER_VALUE_APPLICATION_JSON);
            urlConn.setRequestProperty(Constants.HEADER_ACCEPT, Constants.HEADER_VALUE_APPLICATION_JSON);
            urlConn.setDoOutput(true);
            urlConn.setDoInput(true);

            // Send POST output.
            printout = new DataOutputStream(urlConn.getOutputStream());
            printout.write(jsonParam.toString().getBytes());
            printout.flush();
            printout.close();
            int responseCode = urlConn.getResponseCode();
            Log.e("doPost", "Request responseCode :" + responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK) {
                tempResponse = readStream(urlConn.getInputStream());
            }
        } catch (MalformedURLException e) {
            error = true;
            e.printStackTrace();
        } catch (IOException e) {
            error = true;
            e.printStackTrace();
        } catch (Exception e) {
            error = true;
            e.printStackTrace();
        } finally {
            if (error) {
                tempResponse = "";
            }
        }
        return tempResponse;
    }

    private String doGet(String serviceUrl) {
        Log.e("doGet", "doGet called : URL : " + serviceUrl);
        HttpURLConnection urlConn;
        URL obj = null;
        String tempResponse = "";
        boolean error = false;
        try {
            obj = new URL(serviceUrl);
            urlConn = (HttpURLConnection) obj.openConnection();
            //add reuqest header
            urlConn.setRequestMethod(GET);
            // urlConn.setRequestProperty(Common.HEADER_VALUE_USER_AGENT, Common.ANDROID);
            urlConn.setConnectTimeout(Constants.CONNECTION_TIMEOUT);
            urlConn.setReadTimeout(Constants.CONNECTION_TIMEOUT);

            urlConn.setDoOutput(true);
            urlConn.setDoInput(true);

            int responseCode = urlConn.getResponseCode();
            Log.e("doGet", "doGet : Request responseCode :" + responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK) {
                tempResponse = readStream(urlConn.getInputStream());
            } else {
                tempResponse = "" + responseCode;
            }
        } catch (MalformedURLException e) {
            error = true;
            e.printStackTrace();
        } catch (IOException e) {
            error = true;
            e.printStackTrace();
        } catch (Exception e) {
            error = true;
            e.printStackTrace();
        } finally {
            if (error) {
                tempResponse = "";
            }
        }
        return tempResponse;
    }

    private String readStream(InputStream inputStream) {

        ///  Log.e("readStream", "readStream called :");
        String result = "";
        BufferedReader reader = null;
        StringBuffer sb = new StringBuffer("");
        String line = "";
        String NL = System.getProperty(Constants.LINE_SEPARATOR);
        try {
            reader = new BufferedReader(new InputStreamReader(inputStream));
            while ((line = reader.readLine()) != null) {
                sb.append(line + NL);
            }
            result = sb.toString();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        Log.e("readStream", "readStream called   :result " + result);
        return result;
    }
}
