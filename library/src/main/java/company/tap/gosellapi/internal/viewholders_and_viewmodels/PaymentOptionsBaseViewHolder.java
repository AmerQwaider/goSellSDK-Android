package company.tap.gosellapi.internal.viewholders_and_viewmodels;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import company.tap.gosellapi.R;
import company.tap.gosellapi.internal.adapters.PaymentOptionsRecyclerViewAdapter;
import company.tap.gosellapi.internal.data_managers.payment_options.PaymentType;
import company.tap.gosellapi.internal.data_managers.payment_options.viewmodels.PaymentOptionsBaseViewModel;

//T - data, K - this holder, Q - model
public abstract class PaymentOptionsBaseViewHolder<T, K extends PaymentOptionsBaseViewHolder<T, K, Q>, Q extends PaymentOptionsBaseViewModel<T, K, Q>> extends RecyclerView.ViewHolder {
    //interface for focus interaction between holders
    public interface PaymentOptionsViewHolderFocusedStateInterface {
        void setFocused(int position);
    }

    final PaymentOptionsRecyclerViewAdapter.PaymentOptionsViewAdapterListener adapterListener;
    final PaymentOptionsViewHolderFocusedStateInterface focusedStateInterface;
    Q viewModel;
    int position;

    public static PaymentOptionsBaseViewHolder newInstance(ViewGroup parent, @NonNull PaymentType paymentType, PaymentOptionsRecyclerViewAdapter.PaymentOptionsViewAdapterListener listener, PaymentOptionsViewHolderFocusedStateInterface focusedStateInterface) {
        View view;
        switch (paymentType) {
            case CURRENCY:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gosellapi_viewholder_currency, parent, false);
                return new CurrencyViewHolder(view, focusedStateInterface, listener);
            case RECENT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gosellapi_viewholder_recent_section, parent, false);
                return new RecentSectionViewHolder(view, focusedStateInterface, listener);
            case WEB:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gosellapi_viewholder_payment_systems, parent, false);
                return new WebPaymentViewHolder(view, focusedStateInterface, listener);
            case CARD:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gosellapi_viewholder_card_credentials, parent, false);
                return new CardCredentialsViewHolder(view, focusedStateInterface, listener);
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gosellapi_viewholder_empty, parent, false);
                return new EmptyViewHolder(view, focusedStateInterface, listener);
        }
    }

    PaymentOptionsBaseViewHolder(View itemView, PaymentOptionsViewHolderFocusedStateInterface focusedStateInterface, PaymentOptionsRecyclerViewAdapter.PaymentOptionsViewAdapterListener adapterListener) {
        super(itemView);
        this.focusedStateInterface = focusedStateInterface;
        this.adapterListener = adapterListener;
    }

    public final void bind(Q viewModel, boolean isFocused, int position) {
        this.viewModel = viewModel;
        this.position = position;

        //noinspection unchecked
        viewModel.registerHolder((K) this, position);

        bind(viewModel.getData());
        setFocused(isFocused);
    }

    abstract void bind(T data);

    public final void unbind() {
        viewModel.unregisterHolder();
    }

//    public abstract Bundle getSavedState();

    public abstract void setFocused(boolean isFocused);
}
