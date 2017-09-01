import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HttpRequestHandler {
    private static HttpClient httpClient = HttpClientBuilder.create().build();
    private static List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();

    static HttpResponse sendGetRequest(String request) throws IOException {
        HttpGet httpGetRequest = new HttpGet(request);
        return httpClient.execute(httpGetRequest);
    }

    public static HttpResponse sendPostrequest(String request, ArrayList<NameValuePair> parameters) throws IOException {
        HttpPost httpPostRequest = new HttpPost(request);

        for (NameValuePair parameter : parameters) {
            urlParameters.add(new BasicNameValuePair(parameter.getName(), parameter.getValue()));
        }

        httpPostRequest.setEntity(new UrlEncodedFormEntity(urlParameters));
        return httpClient.execute(httpPostRequest);
    }


    static String printResponse(HttpResponse response) throws IOException {

        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        return result.toString();
    }

    static boolean saveResponseToFile(HttpResponse response, File destinationFile) throws Exception {
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            InputStream inputStream = entity.getContent();
            FileOutputStream output = new FileOutputStream(destinationFile);
            int l;
            byte[] tmp = new byte[2048];
            while ((l = inputStream.read(tmp)) != -1) {
                output.write(tmp, 0, l);
            }
            output.close();
            inputStream.close();
            return true;
        }
        return false;
    }

}
