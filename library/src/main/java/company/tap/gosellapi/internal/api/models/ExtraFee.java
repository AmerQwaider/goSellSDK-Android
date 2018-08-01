package company.tap.gosellapi.internal.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import company.tap.gosellapi.internal.api.enums.AmountModificatorType;

/**
 * Created by eugene.goltsev on 27.04.2018.
 * <br>
 * Model for Customer object
 */

public final class ExtraFee extends AmountModificator {

    @SerializedName("currency")
    @Expose
    private String currency;

    public String getCurrency() {
        return currency;
    }

    public ExtraFee(AmountModificatorType type, double value, String currency) {
        super(type, value);
        this.currency = currency;
    }
}
