package company.tap.gosellapi.open.controllers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import company.tap.gosellapi.R;
import company.tap.gosellapi.internal.activities.GoSellPaymentActivity;
import company.tap.gosellapi.internal.api.callbacks.APIRequestCallback;
import company.tap.gosellapi.internal.api.callbacks.GoSellError;
import company.tap.gosellapi.internal.api.facade.GoSellAPI;
import company.tap.gosellapi.internal.api.models.Address;
import company.tap.gosellapi.internal.api.models.Merchant;
import company.tap.gosellapi.internal.api.requests.PaymentOptionsRequest;
import company.tap.gosellapi.internal.api.responses.PaymentOptionsResponse;
import company.tap.gosellapi.internal.api.responses.SupportedCurreciesResponse;
import company.tap.gosellapi.internal.data_managers.PaymentDataManager;
import company.tap.gosellapi.open.buttons.PayButtonView;
import company.tap.gosellapi.open.data_manager.PaymentDataSource;
import company.tap.gosellapi.open.delegate.SessionDelegate;
import company.tap.gosellapi.open.enums.TransactionMode;
import company.tap.gosellapi.open.models.AuthorizeAction;
import company.tap.gosellapi.open.models.Customer;
import company.tap.gosellapi.open.models.Destinations;
import company.tap.gosellapi.open.models.PaymentItem;
import company.tap.gosellapi.open.models.Receipt;
import company.tap.gosellapi.open.models.Reference;
import company.tap.gosellapi.open.models.Shipping;
import company.tap.gosellapi.open.models.TapCurrency;
import company.tap.gosellapi.open.models.Tax;

/**
 * The type Sdk session.
 */
//public class SDKSession implements View.OnClickListener {
public class SDKSession {

    private PayButtonView payButtonView;
    private PaymentDataSource paymentDataSource;
    private Activity activityListener;
    private int SDK_REQUEST_CODE;
    private CardInfo cardInfo;

    private static SessionDelegate sessionDelegate;
    private Activity context;

    private boolean sessionActive;
    private boolean paymentOtionsResponseReady;
    private boolean isWaitingPaymentOtionsResponse;

    private BigDecimal lastSDKAmount;

    /**
     * Instantiates a new Sdk session.
     */
    public SDKSession() {
        instantiatePaymentDataSource();
        sessionActive = false;
    }


    /**
     * Error Type
     */

    public enum ErrorTypes {
        SDK_NOT_CONFIGURED_WITH_VALID_CONTEXT,
        INTERNET_NOT_AVAILABLE,
        INTERNET_AVAILABLE,
        CONNECTIVITY_MANAGER_ERROR,

    }

    /**
     * Sets button view.
     *
     * @param buttonView       the button view
     * @param activity         the activity
     * @param SDK_REQUEST_CODE the sdk request code
     */
    public void setButtonView(View buttonView, Activity activity, int SDK_REQUEST_CODE) {

        this.SDK_REQUEST_CODE = SDK_REQUEST_CODE;

        if (buttonView instanceof PayButtonView) {
            this.payButtonView = (PayButtonView) buttonView;
        }
        this.activityListener = activity;
    }

    public void setPayButtonLoaderVisible() {
        payButtonView.getLoadingView().setVisibility(ThemeObject.getInstance().isPayButtLoaderVisible() ? View.VISIBLE : View.INVISIBLE);
    }


    /**
     * Instantiate payment data source.
     */
    public void instantiatePaymentDataSource() {

        this.paymentDataSource = company.tap.gosellapi.open.data_manager.PaymentDataSource.getInstance();

    }


    private void persistPaymentDataSource() {
        PaymentDataManager.getInstance().setExternalDataSource(paymentDataSource);
    }

    /**
     * set amount
     */
    public void setAmount(BigDecimal amount) {
        System.out.println("amount ... " + amount);
        paymentDataSource.setAmount(amount);
    }

    /**
     * set transaction currency
     *
     * @param tapCurrency the tap currency
     */
    public void setTransactionCurrency(TapCurrency tapCurrency) {
        paymentDataSource.setTransactionCurrency(tapCurrency);
    }


    /**
     * set transaction mode
     *
     * @param transactionMode the transaction mode
     */
    public void setTransactionMode(TransactionMode transactionMode) {
        paymentDataSource.setTransactionMode(transactionMode);
    }

    /**
     * set customer
     *
     * @param customer the customer
     */
    public void setCustomer(Customer customer) {
        paymentDataSource.setCustomer(customer);
    }

    /**
     * set payment items
     *
     * @param paymentItems the payment items
     */
    public void setPaymentItems(ArrayList<PaymentItem> paymentItems) {
        paymentDataSource.setPaymentItems(paymentItems);
    }

    /**
     * set payment tax
     *
     * @param taxes the taxes
     */
    public void setTaxes(ArrayList<Tax> taxes) {
        paymentDataSource.setTaxes(taxes);
    }

    /**
     * set payment shipping
     *
     * @param shipping the shipping
     */
    public void setShipping(ArrayList<Shipping> shipping) {
        paymentDataSource.setShipping(shipping);
    }

    /**
     * set post url
     *
     * @param postURL the post url
     */
    public void setPostURL(String postURL) {
        paymentDataSource.setPostURL(postURL);
    }

    /**
     * set payment description
     *
     * @param paymentDescription the payment description
     */
    public void setPaymentDescription(String paymentDescription) {
        paymentDataSource.setPaymentDescription(paymentDescription);
    }

    /**
     * set payment meta data
     *
     * @param paymentMetadata the payment metadata
     */
    public void setPaymentMetadata(HashMap<String, String> paymentMetadata) {
        paymentDataSource.setPaymentMetadata(paymentMetadata);
    }

    /**
     * set payment reference
     *
     * @param paymentReference the payment reference
     */
    public void setPaymentReference(Reference paymentReference) {
        paymentDataSource.setPaymentReference(paymentReference);
    }

    /**
     * set payment statement descriptor
     *
     * @param setPaymentStatementDescriptor the set payment statement descriptor
     */
    public void setPaymentStatementDescriptor(String setPaymentStatementDescriptor) {
        paymentDataSource.setPaymentStatementDescriptor(setPaymentStatementDescriptor);
    }


    /**
     * enable or disable saving card.
     *
     * @param saveCardStatus
     */
    public void isUserAllowedToSaveCard(boolean saveCardStatus) {
        System.out.println("isUserAllowedToSaveCard >>> " + saveCardStatus);
        paymentDataSource.isUserAllowedToSaveCard(saveCardStatus);
    }

    /**
     * enable or disable 3dsecure
     *
     * @param is3DSecure the is 3 d secure
     */
    public void isRequires3DSecure(boolean is3DSecure) {
        System.out.println("isRequires3DSecure >>> " + is3DSecure);
        paymentDataSource.isRequires3DSecure(is3DSecure);
    }

    /**
     * set payment receipt
     *
     * @param receipt the receipt
     */
    public void setReceiptSettings(Receipt receipt) {
        paymentDataSource.setReceiptSettings(receipt);
    }


    /**
     * set Authorize Action
     *
     * @param authorizeAction the authorize action
     */
    public void setAuthorizeAction(AuthorizeAction authorizeAction) {
        paymentDataSource.setAuthorizeAction(authorizeAction);
    }

    /**
     * set Destination
     */

    public void setDestination(Destinations destination) {
        paymentDataSource.setDestination(destination);
    }

    /**
     * set Merchant ID
     *
     * @param merchantId
     */

    public void setMerchantID(String merchantId) {
        if (merchantId != null && merchantId.trim().length() != 0)
            paymentDataSource.setMerchant(new Merchant(merchantId));
        else
            paymentDataSource.setMerchant(null);
    }

    public void listAllCards(@NonNull String customer_id, @NonNull SessionDelegate sessionDelegate) {

        if (sessionDelegate == null) return;

        if (customer_id == null || customer_id.trim().length() == 0) {
            sessionDelegate.invalidCustomerID();
            return;
        }
        APIsExposer.getInstance().listAllCards(customer_id, sessionDelegate);
    }

    /**
     * call payment methods API
     */
    public void getPaymentOptions() {
        System.out.println("getPaymentOptions : " + isInternetConnectionAvailable());
        switch (isInternetConnectionAvailable()) {
            case SDK_NOT_CONFIGURED_WITH_VALID_CONTEXT:
                if (getSDKContext() != null)
                    showDialog(getSDKContext().getResources().getString(R.string.sdk_error), getSDKContext().getResources().getString(R.string.sdk_not_configure_correctly));
                else
                    showDialog("SDK Error", "SDK Not Configured Correctly");
                break;
            case CONNECTIVITY_MANAGER_ERROR:
                if (getSDKContext() != null)
                    showDialog(getSDKContext().getResources().getString(R.string.sdk_error), getSDKContext().getResources().getString(R.string.device_has_aproblem_in_connectivity_manager));
                else
                    showDialog("SDK Error", "Device has a problem in Connectivity manager");
                break;
            case INTERNET_NOT_AVAILABLE:
                if (getSDKContext() != null)
                    showDialog(getSDKContext().getResources().getString(R.string.sdk_error), getSDKContext().getResources().getString(R.string.internet_connection_is_not_available));
                else
                    showDialog("Connection Error", "Internet connection is not available");
                break;
            case INTERNET_AVAILABLE:
                startPayment();
                break;
        }
    }

    public void startPayment() {
        paymentOtionsResponseReady = false;
        persistPaymentDataSource();

        System.out.println(" this.paymentDataSource.getTransactionMode() : " + this.paymentDataSource.getTransactionMode());

        // check trx_mode
        if (this.paymentDataSource.getTransactionMode() == null) {
            sessionDelegate.invalidTransactionMode();
            return;
        }

        // create payment options request
        PaymentOptionsRequest request = new PaymentOptionsRequest(
                this.paymentDataSource.getTransactionMode(),
                this.paymentDataSource.getAmount(),
                this.paymentDataSource.getItems(),
                this.paymentDataSource.getShipping(),
                this.paymentDataSource.getTaxes(),
                this.paymentDataSource.getCurrency().getIsoCode(),
                this.paymentDataSource.getCustomer().getIdentifier(),
                (this.paymentDataSource.getMerchant() != null) ? this.paymentDataSource.getMerchant().getId() : null
        );

        // retrieve payment options
        GoSellAPI.getInstance().getPaymentOptions(request,
                new APIRequestCallback<PaymentOptionsResponse>() {

                    @Override
                    public void onSuccess(int responseCode, PaymentOptionsResponse serializedResponse) {
                        System.out.println(" payment options response: " + serializedResponse.getCurrency());
                        paymentOtionsResponseReady = true;
                        if (isWaitingPaymentOtionsResponse)
                            openSDK(lastSDKAmount);
                    }

                    @Override
                    public void onFailure(GoSellError errorDetails) {
                        paymentOtionsResponseReady = false;

                        if (ThemeObject.getInstance().isPayButtLoaderVisible()) {

                            if (payButtonView != null)
                                payButtonView.getLoadingView().setForceStop(true);

                            sessionDelegate.sdkError(errorDetails);
                        } else {
                            sessionDelegate.sdkError(errorDetails);
                        }

                    }
                });
    }

    /**
     * check PaymentOptionsResponse then open sdk
     *
     * @param amount
     */
    public void startSDK(BigDecimal amount) {
        // check if no session is running
        if (sessionActive) return;

        // check if transaction mode is null
        if (getTransactionMode() == null) {
            sessionDelegate.invalidTransactionMode();
            {
                stopPayButtonLoader();
                return;
            }
        }

        // start session
        sessionActive = true;

        // check if payment options is available
        if (!paymentOtionsResponseReady) {
            // store amount
            lastSDKAmount = amount;
            // wait till payment options is ready
            isWaitingPaymentOtionsResponse = true;
        } else {
            isWaitingPaymentOtionsResponse = false;
            openSDK(amount);
        }
    }

    /**
     * Now PaymentOptionsResponse response is ready, open SDK
     *
     * @param amount
     */
    private void openSDK(BigDecimal amount) {

        if (lastSDKAmount == null) return;

        // check if amount changed
        if (amount.compareTo(paymentDataSource.getAmount()) != 0) { // refresh supported currencies exchange rate with new amount
            // get exchange rates for new amount
            GoSellAPI.getInstance().getSupportedCurrencies(amount, new APIRequestCallback<SupportedCurreciesResponse>() {
                @Override
                public void onSuccess(int responseCode, SupportedCurreciesResponse serializedResponse) {
                    System.out.println(" supported currencies : " + serializedResponse.to.get(0));
                    switch (getTransactionMode()) {
                        case PURCHASE:
                        case AUTHORIZE_CAPTURE:
                        case SAVE_CARD:
                        case TOKENIZE_CARD:
                            if (payButtonView != null)
                                payButtonView.getLoadingView().setForceStop(true);
                            startMainActivity();  // start SDK Main activity.
                            break;
                        case TOKENIZE_CARD_NO_UI:  // use SDK without UI (Card Form)
                            if (cardInfo != null) {
                                APIsExposer.getInstance().startToknizingCard(
                                        cardInfo.cardNumber,
                                        cardInfo.expirationMonth,
                                        cardInfo.expirationYear,
                                        cardInfo.cvc,
                                        cardInfo.cardholderName,
                                        cardInfo.address,
                                        sessionDelegate);
                                stopPayButtonLoader();
                                sessionActive = false;
                            } else {
                                stopPayButtonLoader();
                                sessionActive = false;
                                sessionDelegate.invalidCardDetails();
                            }
                            break;
                        case SAVE_CARD_NO_UI:
                            if (cardInfo != null) {
                                APIsExposer.getInstance().startSavingCard(
                                        cardInfo.cardNumber,
                                        cardInfo.expirationMonth,
                                        cardInfo.expirationYear,
                                        cardInfo.cvc,
                                        cardInfo.cardholderName,
                                        cardInfo.address,
                                        sessionDelegate);
                                stopPayButtonLoader();
                                sessionActive = false;
                            } else {
                                stopPayButtonLoader();
                                sessionActive = false;
                                sessionDelegate.invalidCardDetails();
                            }
                            break;
                    }
                }

                @Override
                public void onFailure(GoSellError errorDetails) {
                    stopPayButtonLoader();
                    sessionDelegate.sdkError(errorDetails
                    );
                }
            });
        } else {  // if same amount passed then open SDK directly
            // start SDK directly
            startMainActivity();
        }
    }


    public void stopPayButtonLoader() {
        // stop loader
        if (ThemeObject.getInstance().isPayButtLoaderVisible()) {
            if (payButtonView != null)
                payButtonView.getLoadingView().setForceStop(true);
        }
    }

    private ErrorTypes isInternetConnectionAvailable() {
        Context ctx = getSDKContext();
        if (ctx == null) return ErrorTypes.SDK_NOT_CONFIGURED_WITH_VALID_CONTEXT;

        ConnectivityManager connectivityManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected())
                return ErrorTypes.INTERNET_AVAILABLE;
            else
                return ErrorTypes.INTERNET_NOT_AVAILABLE;
        }
        return ErrorTypes.CONNECTIVITY_MANAGER_ERROR;
    }

    /**
     * launch goSellSDK activity
     */
    private void startMainActivity() {

        stopPayButtonLoader();

        if (getListener() != null)
            getListener().sessionIsStarting();

        if (context != null) {
            sessionActive = false;
            Intent intent = new Intent(context, GoSellPaymentActivity.class);
            context.startActivityForResult(intent, SDK_REQUEST_CODE);
        } else if (payButtonView != null && payButtonView.getContext() != null) {
            sessionActive = false;
            Intent intent = new Intent(payButtonView.getContext(), GoSellPaymentActivity.class);
            activityListener.startActivityForResult(intent, SDK_REQUEST_CODE);
        } else if (getListener() != null) {
            sessionActive = false;
            getListener().sessionFailedToStart();
        }

    }

    public void addSessionDelegate(SessionDelegate _sessionDelegate) {
        sessionDelegate = _sessionDelegate;

    }

    public void startPayButtonLoader() {
        if (ThemeObject.getInstance().isPayButtLoaderVisible()) {
            if (payButtonView != null)
                payButtonView.getLoadingView().start();
        }
    }


    public static SessionDelegate getListener() {
        return sessionDelegate;
    }


    private void showDialog(String title, String message) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getSDKContext());
        dialogBuilder.setTitle(title);
        dialogBuilder.setMessage(message);
        dialogBuilder.setCancelable(false);

        dialogBuilder.setPositiveButton(getSDKContext().getResources().getString(R.string.close), (dialog, which) -> System.out.println("dialog ok button clicked...."));

        dialogBuilder.show();

    }

    private Context getSDKContext() {
        if (context != null) return context;
        else if (payButtonView != null && payButtonView.getContext() != null)
            return payButtonView.getContext();
        return null;
    }


    public TransactionMode getTransactionMode() {
        if (this.paymentDataSource != null)
            return this.paymentDataSource.getTransactionMode();
        else
            return null;
    }

    public void setCardInfo(@NonNull String cardNumber,
                            @NonNull String expirationMonth,
                            @NonNull String expirationYear,
                            @NonNull String cvc,
                            @NonNull String cardholderName,
                            @Nullable Address address) {
        this.cardInfo = new CardInfo(cardNumber, expirationMonth, expirationYear, cvc, cardholderName, address);
    }

    /**
     * Card details holder in case of Merchant will not use the SDK UI.
     */
    class CardInfo {
        String cardNumber;
        String expirationMonth;
        String expirationYear;
        String cvc;
        String cardholderName;
        Address address;

        CardInfo(@NonNull String cardNumber,
                 @NonNull String expirationMonth,
                 @NonNull String expirationYear,
                 @NonNull String cvc,
                 @NonNull String cardholderName,
                 @Nullable Address address) {
            this.cardNumber = cardNumber;
            this.expirationMonth = expirationMonth;
            this.expirationYear = expirationYear;
            this.cvc = cvc;
            this.cardholderName = cardholderName;
            this.address = address;
        }

    }
}
