package company.tap.gosellapi.internal.viewholders;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import company.tap.gosellapi.R;
import company.tap.gosellapi.internal.api.models.PaymentOption;
import company.tap.gosellapi.internal.data_managers.payment_options.viewmodels.WebPaymentViewModel;

public class WebPaymentViewHolder
        extends PaymentOptionsBaseViewHolder<PaymentOption, WebPaymentViewHolder, WebPaymentViewModel> {
    private ImageView paymentSystemIcon;

    WebPaymentViewHolder(final View itemView) {
        super(itemView);
        paymentSystemIcon = itemView.findViewById(R.id.paymentSystemIcon);
    }

    @Override
    public void bind(PaymentOption data) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.itemClicked();
            }
        });

        Glide.with(itemView.getContext()).load(data.getImage()).into(paymentSystemIcon);
    }

    @Override
    public void setFocused(boolean isFocused) {
        if (isFocused) {
            itemView.setBackgroundResource(R.color.vibrant_green);
        } else {
            itemView.setBackground(null);
        }
    }
}
