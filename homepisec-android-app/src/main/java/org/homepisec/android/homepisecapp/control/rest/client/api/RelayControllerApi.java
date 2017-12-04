/*
 * Api Documentation
 * Api Documentation
 *
 * OpenAPI spec version: 1.0
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package org.homepisec.android.homepisecapp.control.rest.client.api;

import org.homepisec.android.homepisecapp.control.rest.client.ApiCallback;
import org.homepisec.android.homepisecapp.control.rest.client.ApiClient;
import org.homepisec.android.homepisecapp.control.rest.client.ApiException;
import org.homepisec.android.homepisecapp.control.rest.client.ApiResponse;
import org.homepisec.android.homepisecapp.control.rest.client.Configuration;
import org.homepisec.android.homepisecapp.control.rest.client.Pair;
import org.homepisec.android.homepisecapp.control.rest.client.ProgressRequestBody;
import org.homepisec.android.homepisecapp.control.rest.client.ProgressResponseBody;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;



import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RelayControllerApi {
    private ApiClient apiClient;

    public RelayControllerApi() {
        this(Configuration.getDefaultApiClient());
    }

    public RelayControllerApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Build call for switchRelayUsingPOST
     * @param relayId relayId (optional)
     * @param value value (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call switchRelayUsingPOSTCall(String relayId, Boolean value, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/api/relays/switch";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (relayId != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "relayId", relayId));
        if (value != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "value", value));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "*/*"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] {  };
        return apiClient.buildCall(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }
    
    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call switchRelayUsingPOSTValidateBeforeCall(String relayId, Boolean value, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        
        com.squareup.okhttp.Call call = switchRelayUsingPOSTCall(relayId, value, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * switchRelay
     * 
     * @param relayId relayId (optional)
     * @param value value (optional)
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public void switchRelayUsingPOST(String relayId, Boolean value) throws ApiException {
        switchRelayUsingPOSTWithHttpInfo(relayId, value);
    }

    /**
     * switchRelay
     * 
     * @param relayId relayId (optional)
     * @param value value (optional)
     * @return ApiResponse&lt;Void&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Void> switchRelayUsingPOSTWithHttpInfo(String relayId, Boolean value) throws ApiException {
        com.squareup.okhttp.Call call = switchRelayUsingPOSTValidateBeforeCall(relayId, value, null, null);
        return apiClient.execute(call);
    }

    /**
     * switchRelay (asynchronously)
     * 
     * @param relayId relayId (optional)
     * @param value value (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call switchRelayUsingPOSTAsync(String relayId, Boolean value, final ApiCallback<Void> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = switchRelayUsingPOSTValidateBeforeCall(relayId, value, progressListener, progressRequestListener);
        apiClient.executeAsync(call, callback);
        return call;
    }
}
