package company.tap.gosellapi.internal.api.facade;

import java.util.ArrayList;

import company.tap.gosellapi.internal.api.api_service.APIService;
import company.tap.gosellapi.internal.api.callbacks.APIRequestCallback;
import company.tap.gosellapi.internal.api.callbacks.BaseCallback;
import company.tap.gosellapi.internal.api.callbacks.GoSellError;
import company.tap.gosellapi.internal.api.responses.BaseResponse;
import company.tap.gosellapi.internal.api.responses.InitResponse;
import retrofit2.Call;

class DataManager {
    private APIService apiHelper;

    //all requests are wrapped in DelayedRequest, until init() would be finished
    private boolean initIsRunning;
    private ArrayList<DelayedRequest> delayedRequests;
    private InitResponse initResponse;

    DataManager(APIService apiHelper) {
        this.apiHelper = apiHelper;
        delayedRequests = new ArrayList<>();
    }

    void request(DelayedRequest delayedRequest) {
        delayedRequests.add(delayedRequest);
        if (initResponse == null) {
            if (!initIsRunning) {
                init();
            }
        } else {
            runDelayedRequests();
        }
    }

    private void init() {
        initIsRunning = true;
        apiHelper
                .init()
                .enqueue(new BaseCallback<>(new APIRequestCallback<InitResponse>() {
                    @Override
                    public void onSuccess(int responseCode, InitResponse serializedResponse) {
                        initIsRunning = false;
                        initResponse = serializedResponse;
                        runDelayedRequests();
                    }

                    @Override
                    public void onFailure(GoSellError errorDetails) {
                        initIsRunning = false;
                        failDelayedRequests(errorDetails);
                    }
                }));
    }

    InitResponse getInitResponse() {
        return initResponse;
    }

    private void runDelayedRequests() {
        for (DelayedRequest delayedRequest : delayedRequests) {
            delayedRequest.run();
        }
        delayedRequests.clear();
    }

    private void failDelayedRequests(GoSellError errorDetails) {
        for (DelayedRequest delayedRequest : delayedRequests) {
            delayedRequest.fail(errorDetails);
        }
        delayedRequests.clear();
    }

    static class DelayedRequest<T extends BaseResponse> {
        private Call<T> request;
        private APIRequestCallback<T> requestCallback;

        DelayedRequest(Call<T> request, APIRequestCallback<T> requestCallback) {
            this.request = request;
            this.requestCallback = requestCallback;
        }

        void run() {
            request.enqueue(new BaseCallback<>(requestCallback));
        }

        void fail(GoSellError errorDetails) {
            requestCallback.onFailure(errorDetails);
        }
    }
}
