package company.tap.gosellapi.internal.api.requests;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.ArrayList;

import company.tap.gosellapi.open.enums.TransactionMode;
import company.tap.gosellapi.open.models.PaymentItem;
import company.tap.gosellapi.open.models.Shipping;
import company.tap.gosellapi.open.models.Tax;
import company.tap.gosellapi.internal.utils.AmountCalculator;
import company.tap.gosellapi.open.models.TapCurrency;
@RestrictTo(RestrictTo.Scope.LIBRARY)
public final class PaymentOptionsRequest {

    @SerializedName("transaction_mode")
    @Expose
    @NonNull private TransactionMode transactionMode;

    @SerializedName("items")
    @Expose
    @Nullable private ArrayList<PaymentItem> items;

    @SerializedName("shipping")
    @Expose
    @Nullable private ArrayList<Shipping> shipping;

    @SerializedName("taxes")
    @Expose
    @Nullable private ArrayList<Tax> taxes;

    @SerializedName("customer")
    @Expose
    @Nullable private String customer;

    @SerializedName("currency")
    @Expose
    @NonNull private TapCurrency currency;

    @SerializedName("total_amount")
    @Expose
    @NonNull private BigDecimal totalAmount;

    public PaymentOptionsRequest(@Nullable TransactionMode transactionMode,
                                 @Nullable BigDecimal amount,
                                 @Nullable ArrayList<PaymentItem> items,
                                 @Nullable ArrayList<Shipping> shipping,
                                 @Nullable ArrayList<Tax> taxes,
                                 @Nullable TapCurrency currency,
                                 @Nullable String customer) {

        this.transactionMode    = transactionMode == null ? TransactionMode.PURCHASE : transactionMode;
        this.shipping           = shipping;
        this.taxes              = taxes;
        this.currency           = currency;
        this.customer           = customer;

        if (items != null && items.size() > 0) {

            this.items          = items;
            this.totalAmount    = AmountCalculator.calculateTotalAmountOf(items, taxes, shipping);
        }
        else {

            this.items = null;

            BigDecimal plainAmount = amount == null ? BigDecimal.ZERO : amount;
            this.totalAmount = AmountCalculator.calculateTotalAmountOf(new ArrayList<PaymentItem>(), taxes, shipping).add(plainAmount);
        }
    }
}
