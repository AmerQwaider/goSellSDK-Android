package company.tap.gosellapi.api.facade;

import company.tap.gosellapi.api.model.Charge;
import company.tap.gosellapi.api.model.Token;
import company.tap.gosellapi.api.requests.CardRequest;
import company.tap.gosellapi.api.requests.CreateChargeRequest;
import company.tap.gosellapi.api.requests.CreateTokenRequest;
import company.tap.gosellapi.api.requests.UpdateChargeRequest;
import company.tap.gosellapi.api.responses.CardResponse;

/**
 * Facade class to use Android SDK
 * <br>
 * This is a singleton class, use it by {@link #getInstance(String secretKey)} method.
 */
public final class GoSellAPI {
    private APIService apiHelper;

    private GoSellAPI() {
        apiHelper = RetrofitHelper.getApiHelper();
    }

    private static class SingletonHolder {
        private static final GoSellAPI INSTANCE = new GoSellAPI();
    }

    /**
     * Use this method to get instance of {@link GoSellAPI}
     * @param secretKey A secret key from your dashboard
     * @return {@link GoSellAPI} instance to perform requests
     */
    public static GoSellAPI getInstance(String secretKey) {
        RetrofitHelper.setAuthenticationKey(secretKey);
        return SingletonHolder.INSTANCE;
    }

    /**
     * Creates {@link Token} with {@link CardRequest} source
     * @param card {@link CardRequest}, modified representation of {@link CardResponse} model. Use {@link CardRequest.Builder} to build this card instance
     * @param requestCallback {@link APIRequestCallback} parametrized with {@link Token} model
     */
    public void createToken(final CardRequest card, final APIRequestCallback<Token> requestCallback) {
        apiHelper.createToken(new CreateTokenRequest
                .Builder(card)
                .build())
            .enqueue(new BaseCallback<>(requestCallback));
    }

    /**
     * Retrieves {@link Token} by id
     * <br>
     * @param tokenId Id field from {@link Token#getId()} method
     * @param requestCallback {@link APIRequestCallback} parametrized with {@link Token} model
     */
    public void retrieveToken(final String tokenId, final APIRequestCallback<Token> requestCallback) {
        apiHelper.retrieveToken(tokenId)
                .enqueue(new BaseCallback<>(requestCallback));
    }

    /**
     * Creates {@link Charge} from {@link CreateChargeRequest}
     * @param createChargeRequest {@link CreateChargeRequest} instance. Use {@link CreateChargeRequest.Builder} to obtain this instance
     * @param requestCallback {@link APIRequestCallback} parametrized with {@link Charge} model
     */
    public void createCharge(final CreateChargeRequest createChargeRequest, final APIRequestCallback<Charge> requestCallback) {
        apiHelper.createCharge(createChargeRequest)
                .enqueue(new BaseCallback<>(requestCallback));
    }

    /**
     * Retrieves {@link Charge} by id
     * <br>
     * Method is not using in Android SDK at the moment
     * @param chargeId Id field from {@link Charge#getId()} method
     * @param requestCallback {@link APIRequestCallback} parametrized with {@link Charge} model
     */
    public void retrieveCharge(final String chargeId, final APIRequestCallback<Charge> requestCallback) {
        apiHelper.retrieveCharge(chargeId)
                .enqueue(new BaseCallback<>(requestCallback));
    }

    /**
     * Updates {@link Charge} by id with {@link UpdateChargeRequest}
     * <br>
     * Method is not using in Android SDK at the moment
     * @param chargeId Id field from {@link Charge#getId()} method
     * @param updateChargeRequest {@link UpdateChargeRequest} instance. Use {@link UpdateChargeRequest.Builder} to obtain this instance
     * @param requestCallback {@link APIRequestCallback} parametrized with {@link Charge} model
     */
    public void updateCharge(final String chargeId, final UpdateChargeRequest updateChargeRequest, final APIRequestCallback<Charge> requestCallback) {
        apiHelper.updateCharge(chargeId, updateChargeRequest)
                .enqueue(new BaseCallback<>(requestCallback));
    }
}
