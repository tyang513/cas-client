package com.talkingdata.security.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataInput;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

/**
 * @author tao.yang
 * @date 2019-07-01
 */
public class CommonUtils {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(CommonUtils.class);

    private static ObjectMapper objectMapper = new ObjectMapper();

    /**
     *
     * @param json
     * @return
     * @throws IOException
     */
    public static Map<String, Object> readValue2Map(String json) throws IOException {
        return objectMapper.readValue(json, Map.class);
    }

    /**
     * Determines whether the String is null or of length 0.
     *
     * @param string the string to check
     * @return true if its null or length of 0, false otherwise.
     */
    public static boolean isEmpty(final String string) {
        return string == null || string.length() == 0;
    }

    /**
     * Contacts the remote URL and returns the response.
     *
     * @param constructedUrl the url to contact.
     * @param encoding the encoding to use.
     * @return the response.
     */
    public static String getResponseFromServer(final URL constructedUrl, final String encoding) {
        return getResponseFromServer(constructedUrl, HttpsURLConnection.getDefaultHostnameVerifier(), encoding);
    }

    /**
     * Contacts the remote URL and returns the response.
     *
     * @param constructedUrl the url to contact.
     * @param hostnameVerifier Host name verifier to use for HTTPS connections.
     * @param encoding the encoding to use.
     * @return the response.
     */
    public static String getResponseFromServer(final URL constructedUrl, final HostnameVerifier hostnameVerifier, final String encoding) {
        URLConnection conn = null;
        try {
            conn = constructedUrl.openConnection();
            if (conn instanceof HttpsURLConnection) {
                ((HttpsURLConnection)conn).setHostnameVerifier(hostnameVerifier);
            }
            logger.info("Content-Type", conn.getContentType());
            String returnString = readerFormInputStream(conn.getInputStream(), encoding);
            return returnString;
        } catch (final Exception e) {
            String errorMessage = "";
            int responseCode = -1;
            if (conn != null && conn instanceof HttpURLConnection) {
                HttpURLConnection httpURLConnection = ((HttpURLConnection)conn);
                try {
                    responseCode = httpURLConnection.getResponseCode();
                    errorMessage = readerFormInputStream(httpURLConnection.getErrorStream(), null);
                    logger.error(" http status code : {}, error message : {}  url : {}", responseCode, errorMessage, constructedUrl.toString());
                } catch (IOException e1) {
                    logger.error("", e1);
                }
            }
            throw new RuntimeException("http status code : " + responseCode + " error message : " + errorMessage + " url : " + constructedUrl.toString(), e);
        } finally {
            if (conn != null && conn instanceof HttpURLConnection) {
                ((HttpURLConnection)conn).disconnect();
            }
        }
    }

    /**
     * Contacts the remote URL and returns the response.
     *
     * @param url the url to contact.
     * @param encoding the encoding to use.
     * @return the response.
     */
    public static String getResponseFromServer(final String url, String encoding) {
        try {
            return getResponseFromServer(new URL(url), encoding);
        } catch (final MalformedURLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     *
     * @param inputStream
     * @param encoding
     * @return
     * @throws IOException
     */
    public static String readerFormInputStream(final InputStream inputStream, String encoding) throws IOException {
        final BufferedReader in;
        if (isEmpty(encoding)) {
            in = new BufferedReader(new InputStreamReader(inputStream));
        } else {
            in = new BufferedReader(new InputStreamReader(inputStream, encoding));
        }
        String line;
        final StringBuilder stringBuffer = new StringBuilder(255);

        while ((line = in.readLine()) != null) {
            stringBuffer.append(line);
            stringBuffer.append("\n");
        }
        return stringBuffer.toString();
    }

}
