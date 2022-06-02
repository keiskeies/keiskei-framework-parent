package top.keiskeiframework.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.List;


/**
 * @author James Chen right_way@foxmail.com
 */
@Slf4j
public class HttpClientUtils {

    private final static int ERROR_CODE = 400;

    public static String getRequest(String path) {
        return getRequest(path, null, null);
    }


    public static String getRequest(String path, List<NameValuePair> parametersBody, List<NameValuePair> headers) {
        try {
            URIBuilder uriBuilder = new URIBuilder(path);
            if (null != parametersBody) {
                uriBuilder.setParameters(parametersBody);
            }
            HttpGet get = new HttpGet(uriBuilder.build());

            if (null != headers) {
                for (NameValuePair h : headers) {
                    get.addHeader(h.getName(), h.getValue());
                }
            }

            HttpClient client = HttpClientBuilder.create().build();
            HttpResponse response = client.execute(get);
            int code = response.getStatusLine().getStatusCode();
            if (code >= ERROR_CODE) {
                throw new RuntimeException("Could not access protected resource. Server returned http code: " + code);
            }
            get.releaseConnection();
            return EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * <p>
     * 发送GET请求
     * </p>
     *
     * @param path           请求路径
     * @param parametersBody GET参数
     * @return 。
     */
    public static HttpResponse getRequestResponse(String path, List<NameValuePair> parametersBody) {
        try {
            URIBuilder uriBuilder = new URIBuilder(path);
            uriBuilder.setParameters(parametersBody);
            HttpGet get = new HttpGet(uriBuilder.build());
            HttpClient client = HttpClientBuilder.create().build();
            HttpResponse response = client.execute(get);
            int code = response.getStatusLine().getStatusCode();
            if (code >= ERROR_CODE) {
                throw new RuntimeException("Could not access protected resource. Server returned http code: " + code);
            }
            get.releaseConnection();
            return response;
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 发送POST请求（普通表单形式）
     *
     * @param path     请求路径
     * @param formData form表单数据
     * @param headers  请求头
     * @return ，
     */
    public static String postForm(String path, List<NameValuePair> formData, List<NameValuePair> headers) {
        HttpEntity entity = new UrlEncodedFormEntity(formData, StandardCharsets.UTF_8);
        return postRequest(path, "application/x-www-form-urlencoded", entity, headers);
    }

    /**
     * 发送POST请求（JSON形式）
     *
     * @param path    。
     * @param json    。
     * @param headers 。
     * @return 。
     */
    public static String postJSON(String path, String json, List<NameValuePair> headers) {
        StringEntity entity = new StringEntity(json, StandardCharsets.UTF_8);
        return postRequest(path, "application/json", entity, headers);
    }

    /**
     * 发送POST请求
     *
     * @param path      。
     * @param mediaType 。
     * @param entity    。
     * @param headers   。
     * @return 。
     */
    public static String postRequest(String path, String mediaType, HttpEntity entity, List<NameValuePair> headers) {
        log.debug("[postRequest] resourceUrl: {}", path);
        HttpPost post = new HttpPost(path);
        post.addHeader("Content-Type", mediaType);
        post.addHeader("Accept", "application/json");

        if (null != headers) {
            for (NameValuePair h : headers) {
                post.addHeader(h.getName(), h.getValue());
            }
        }

        post.setEntity(entity);
        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpResponse response = client.execute(post);
            int code = response.getStatusLine().getStatusCode();
            if (code >= ERROR_CODE) {
                throw new RuntimeException(EntityUtils.toString(response.getEntity()));
            }
            return EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            post.releaseConnection();
        }
    }
}
