package com.ferbo.gestion.api.business;

import com.ferbo.gestion.api.response.FailModelResponse;
import com.ferbo.gestion.api.tool.DataSourceManager;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.google.gson.Gson;

public abstract class AbstractGestionApiBL 
{
    private static Logger log = LogManager.getLogger(AbstractGestionApiBL.class);

    protected String charset = "UTF-8";
    protected String basePath = null;
    protected String user = null;
    protected String password = null;

    protected String auth = null;
    protected byte[] encodedAuth = null;
    protected String authHeaderValue = null;
    
    protected CloseableHttpClient httpClient = null;

    public AbstractGestionApiBL() {
        basePath = DataSourceManager.getJndiParameter("gestionapi/api");
        user = DataSourceManager.getJndiParameter("gestionapi/user");
        password = DataSourceManager.getJndiParameter("gestionapi/password");
        
        auth = String.format("%s:%s", user, password);
        encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
        authHeaderValue = "Basic " + new String(encodedAuth);
        
        httpClient = HttpClients.createDefault();
    }
    
    protected String getResponseBody(CloseableHttpResponse response) {
        String bodyResponse = null;
        HttpEntity entity = null;
        String resultContent;

        try {
            entity = response.getEntity();
            resultContent = EntityUtils.toString(entity);
            bodyResponse = new String(resultContent.getBytes(), "UTF-8");
        } catch (ParseException | IOException e) {
            log.error("Problema para obtener la respueta del SGP-API...", e);
        }

        return bodyResponse;
    }

    protected String getErrorMessage(CloseableHttpResponse response) {
        Gson gson = null;
        String jsonResponse = null;
        FailModelResponse failMessage = null;
        StringBuilder sbMessage = new StringBuilder();

        jsonResponse = this.getResponseBody(response);
        log.error("Error en SGP-API: {}", jsonResponse);
        gson = new Gson();
        if (jsonResponse == null || "".equalsIgnoreCase(jsonResponse.trim())) {
            return "No hay mensaje de respuesta de SGP-API.";
        }

        failMessage = gson.fromJson(jsonResponse, FailModelResponse.class);
        if (failMessage == null) {
            return "No hay mensaje de respuesta de SGP-API.";
        }

        return sbMessage.append(failMessage.getCodigoError()).append(" - ").append(failMessage.getMensajeError()).toString();
    }
    
    protected HttpGet createGetRequest(String url) {
    	HttpGet request = null;
    	request = new HttpGet(url);
        request.addHeader("Accept-Charset", charset);
        request.addHeader("Authorization", authHeaderValue);
    	return request;
    }

    protected HttpUriRequest createGetRequest(String url, String jsonBody) {
        HttpGetWithBody request = null;
        request = new HttpGetWithBody(url);
        request.addHeader("Accept-Charset", charset);
        request.addHeader("Authorization", authHeaderValue);
        request.addHeader("Content-Type", "application/json");

        // Añadir el cuerpo
        if (jsonBody != null) {
            try {
                request.setEntity(new StringEntity(jsonBody, charset));
            } catch (Exception e) {
                log.error("Error al setear el body", e);
            }
        }

        return request;
    }

    protected HttpPost createPostRequest(String url) {
        HttpPost request = null;
        request = new HttpPost(url);
        request.addHeader("Accept-Charset", charset);
        request.addHeader("Authorization", authHeaderValue);
        return request;
    }

    protected HttpDelete createDeleteRequest(String url) {
        HttpDelete request = null;
        request = new HttpDelete(url);
        request.addHeader("Accept-Charset", charset);
        request.addHeader("Authorization", authHeaderValue);
        return request;
    }

}
