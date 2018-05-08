package company.tap.gosellapi.internal.view_holders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import company.tap.gosellapi.R;
import company.tap.gosellapi.internal.adapters.PaymentOptionsRecyclerViewAdapter;
import company.tap.gosellapi.internal.data_source.payment_options.PaymentOptionsBaseModel;
import company.tap.gosellapi.internal.data_source.payment_options.PaymentType;

public abstract class PaymentOptionsBaseViewHolder<T extends PaymentOptionsBaseModel> extends RecyclerView.ViewHolder {
    //interface for focus interaction between holders
    public interface PaymentOptionsViewHolderFocusedStateInterface {
        void setFocused(int position);
    }

    final PaymentOptionsRecyclerViewAdapter.PaymentOptionsViewAdapterListener adapterListener;
    final PaymentOptionsViewHolderFocusedStateInterface focusedStateInterface;
    int position;

    public static PaymentOptionsBaseViewHolder newInstance(ViewGroup parent, @NonNull PaymentType paymentType, PaymentOptionsRecyclerViewAdapter.PaymentOptionsViewAdapterListener listener, PaymentOptionsViewHolderFocusedStateInterface focusedStateInterface) {
        View view = null;
        switch (paymentType) {
            case CURRENCY:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gosellapi_viewholder_currency, parent, false);
                return new CurrencyViewHolder(view, focusedStateInterface, listener);
            case RECENT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gosellapi_viewholder_recent_section, parent, false);
                return new RecentSectionViewHolder(view, focusedStateInterface, listener);
            case WEB:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gosellapi_viewholder_payment_systems, parent, false);
                return new WebPaymentSystemsViewHolder(view, focusedStateInterface, listener);
            case CARD:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gosellapi_viewholder_card_credentials, parent, false);
                return new CardCredentialsViewHolder(view, focusedStateInterface, listener);
            default:
                return new DummyViewHolder(view, focusedStateInterface, listener);
        }
    }

    PaymentOptionsBaseViewHolder(View itemView, PaymentOptionsViewHolderFocusedStateInterface focusedStateInterface, PaymentOptionsRecyclerViewAdapter.PaymentOptionsViewAdapterListener adapterListener) {
        super(itemView);
        this.focusedStateInterface = focusedStateInterface;
        this.adapterListener = adapterListener;
    }

    public final void bind(T data, boolean isFocused, int position) {
        this.position = position;
        bind(data);
        setFocused(isFocused);
    }

    abstract void bind(T data);

    public void unbind() {

    }

    public abstract void setFocused(boolean isFocused);
}
