package company.tap.gosellapi.open.interfaces;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import company.tap.gosellapi.open.enums.FeesOptions;
import company.tap.gosellapi.open.enums.TransactionMode;
import company.tap.gosellapi.open.models.AuthorizeAction;
import company.tap.gosellapi.open.models.Customer;
import company.tap.gosellapi.open.models.Destination;
import company.tap.gosellapi.open.models.PaymentItem;
import company.tap.gosellapi.open.models.Receipt;
import company.tap.gosellapi.open.models.Reference;
import company.tap.gosellapi.open.models.Shipping;
import company.tap.gosellapi.open.models.Tax;
import company.tap.gosellapi.open.models.TapCurrency;

/**
 * The interface Payment data source.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
public interface PaymentDataSource {

    /**
     * Transaction currency. @return the currency
     */
    @NonNull     TapCurrency            getCurrency();

    /**
     * Customer. @return the customer
     */
    @NonNull     Customer                getCustomer();

    /**
     * Amount. Either amount or items should return nonnull value. If both return nonnull, then items is preferred. @return the amount
     */
    @Nullable    BigDecimal              getAmount();

    /**
     * List of items to pay for. Either amount or items should return nonnull value. If both return nonnull, then items is preferred. @return the items
     */
    @Nullable    ArrayList<PaymentItem>  getItems();

    /**
     * Transaction mode. If you return null in this method, it will be treated as PURCHASE. @return the transaction mode
     */
    @Nullable    TransactionMode         getTransactionMode();

    /**
     * List of taxes. Optional. Note: specifying taxes will affect total payment amount. @return the taxes
     */
    @Nullable    ArrayList<Tax>          getTaxes();

    /**
     * Shipping list. Optional. Note: specifying shipping will affect total payment amount. @return the shipping
     */
    @Nullable    ArrayList<Shipping>     getShipping();

    /**
     * Tap will post to this URL after transaction finishes. Optional. @return the post url
     */
    @Nullable    String                  getPostURL();

    /**
     * Description of the payment. @return the payment description
     */
    @Nullable    String                  getPaymentDescription();

    /**
     * If you would like to pass additional information with the payment, pass it here. @return the payment metadata
     */
    @Nullable    HashMap<String, String> getPaymentMetadata();

    /**
     * Payment reference. Implement this property to keep a reference to the transaction on your backend. @return the payment reference
     */
    @Nullable    Reference               getPaymentReference();

    /**
     * Payment statement descriptor. @return the payment statement descriptor
     */
    @Nullable    String                  getPaymentStatementDescriptor();

    /**
     * Defines if 3D secure check is required. @return the requires 3 d secure
     */
    @Nullable    boolean                 getRequires3DSecure();

    /**
     * Receipt dispatch settings. @return the receipt settings
     */
    @Nullable    Receipt                 getReceiptSettings();

    /**
     * Action to perform after authorization succeeds. Used only if transaction mode is AUTHORIZE_CAPTURE. @return the authorize action
     */
    @Nullable    AuthorizeAction         getAuthorizeAction();

    /**
     *  The Destination array contains list of Merchant desired destinations accounts to receive money from payment transactions
     */

    @Nullable
    ArrayList<Destination>                getDestination();

    /**
     * Fees Options set by Merchant to allow SDK to calculate Fees as Extra and add it to total amount or not calculate it becuase it
     * is already included in the total amount
     * @return
     */
    @NonNull
    FeesOptions                           getFeesOptions();
}
