package company.tap.gosellapi.internal.api.models;

import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

import company.tap.gosellapi.internal.api.enums.ChargeStatus;
import company.tap.gosellapi.internal.api.responses.BaseResponse;

/**
 * Created by eugene.goltsev on 14.02.2018.
 * <br>
 * Charge response
 */

@RestrictTo(RestrictTo.Scope.LIBRARY)
public class Charge implements BaseResponse{
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("amount")
    @Expose
    private double amount;

    @SerializedName("currency")
    @Expose
    private String currency;

    @SerializedName("customer")
    @Expose
    private Customer customer;

    @SerializedName("live_mode")
    @Expose
    private boolean liveMode;

    @SerializedName("object")
    @Expose
    private String object;

    @SerializedName("authenticate")
    @Expose
    @Nullable private Authenticate authenticate;

    @SerializedName("redirect")
    @Expose
    private TrackingURL redirect;

    @SerializedName("post")
    @Expose
    @Nullable private TrackingURL post;

    @SerializedName("source")
    @Expose
    private Source source;

    @SerializedName("status")
    @Expose
    private ChargeStatus status;

    @SerializedName("threeDSecure")
    @Expose
    private boolean threeDSecure;

    @SerializedName("transaction")
    @Expose
    private TransactionDetails transaction;

    @SerializedName("description")
    @Expose
    @Nullable private String description;

    @SerializedName("metadata")
    @Expose
    @Nullable private HashMap<String, String> metadata;

    @SerializedName("reference")
    @Expose
    @Nullable private Reference reference;

    @SerializedName("receipt")
    @Expose
    @Nullable private Receipt receipt;

    @SerializedName("response")
    @Expose
    @Nullable private Response response;

    @SerializedName("statement_descriptor")
    @Expose
    @Nullable private String statementDescriptor;

    /**
     * @return Charge identifier.
     */
    public String getId() {
        return id;
    }

    /**
     * @return Charge amount.
     */
    public double getAmount() {
        return amount;
    }

    /**
     * @return Transaction currency.
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * @return Customer information.
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * @return Defines whether the charge is in real environment.
     */
    public boolean isLiveMode() {
        return liveMode;
    }

    /**
     * @return Object type. For charge, “charge” always.
     */
    public String getObject() {
        return object;
    }

    /**
     * @return Required authentication options (if any).
     */
    public Authenticate getAuthenticate() {
        return authenticate;
    }

    /**
     * @return Charge redirect.
     */
    public TrackingURL getRedirect() {
        return redirect;
    }

    public @Nullable TrackingURL getPost() {

        return post;
    }

    /**
     * @return Charge source
     */
    public Source getSource() {
        return source;
    }

    /**
     * @return Charge status.
     */
    public ChargeStatus getStatus() {
        return status;
    }

    /**
     * @return Defines if 3D secure is required
     */
    public boolean isThreeDSecure() {
        return threeDSecure;
    }

    /**
     * @return Transaction details.
     */
    public TransactionDetails getTransaction() {
        return transaction;
    }

    /**
     * @return Charge description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return Charge metadata.
     */
    public HashMap<String, String> getMetadata() {
        return metadata;
    }

    /**
     * @return Merchant reference object.
     */
    public Reference getReference() {
        return reference;
    }

    /**
     * @return Receipt settings.
     */
    public Receipt getReceipt() {
        return receipt;
    }

    /**
     * @return Charge response code and message.
     */
    public Response getResponse() {
        return response;
    }

    /**
     * @return Statement descriptor.
     */
    public String getStatementDescriptor() {
        return statementDescriptor;
    }
}
