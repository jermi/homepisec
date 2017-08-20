package org.homepisec.sensor.rest.client.api;

import org.homepisec.sensor.rest.client.ApiClient;

import org.homepisec.sensor.rest.client.model.EnrichedEvent;
import org.homepisec.sensor.rest.client.model.EventDeviceReading;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2017-08-20T10:24:19.852+02:00")
@Component("org.homepisec.sensor.rest.client.api.ReadingsControllerApi")
public class ReadingsControllerApi {
    private ApiClient apiClient;

    public ReadingsControllerApi() {
        this(new ApiClient());
    }

    @Autowired
    public ReadingsControllerApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * getReadings
     * 
     * <p><b>200</b> - OK
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not Found
     * @return List&lt;EnrichedEvent&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public List<EnrichedEvent> getReadingsUsingGET() throws RestClientException {
        Object postBody = null;
        
        String path = UriComponentsBuilder.fromPath("/api/readings").build().toUriString();
        
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] accepts = { 
            "application/json;charset&#x3D;UTF-8"
        };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = { 
            "*/*"
        };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] {  };

        ParameterizedTypeReference<List<EnrichedEvent>> returnType = new ParameterizedTypeReference<List<EnrichedEvent>>() {};
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    /**
     * postReadings
     * 
     * <p><b>200</b> - OK
     * <p><b>201</b> - Created
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>404</b> - Not Found
     * @param event event
     * @return Boolean
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public Boolean postReadingsUsingPOST(EventDeviceReading event) throws RestClientException {
        Object postBody = event;
        
        // verify the required parameter 'event' is set
        if (event == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'event' when calling postReadingsUsingPOST");
        }
        
        String path = UriComponentsBuilder.fromPath("/api/readings").build().toUriString();
        
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] accepts = { 
            "application/json;charset&#x3D;UTF-8"
        };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = { 
            "application/json;charset&#x3D;UTF-8"
        };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] {  };

        ParameterizedTypeReference<Boolean> returnType = new ParameterizedTypeReference<Boolean>() {};
        return apiClient.invokeAPI(path, HttpMethod.POST, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
}
