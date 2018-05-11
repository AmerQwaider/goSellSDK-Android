package company.tap.gosellapi.internal.viewholders_and_viewmodels;

import android.view.View;

import java.util.HashMap;

import company.tap.gosellapi.internal.data_managers.payment_options.viewmodels.CurrencyViewModel;

public class CurrencyViewHolder
        extends PaymentOptionsBaseViewHolder<HashMap<String, Double>, CurrencyViewHolder, CurrencyViewModel> {

    CurrencyViewHolder(View view) {
        super(view);
    }

    @Override
    public void bind(HashMap<String, Double> data) {

    }

    @Override
    public void setFocused(boolean isFocused) { }
}
