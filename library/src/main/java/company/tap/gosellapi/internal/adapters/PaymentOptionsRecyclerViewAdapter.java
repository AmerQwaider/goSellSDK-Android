package company.tap.gosellapi.internal.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import company.tap.gosellapi.internal.Constants;
import company.tap.gosellapi.internal.api.models.Card;
import company.tap.gosellapi.internal.api.models.CardRawData;
import company.tap.gosellapi.internal.data_managers.payment_options.PaymentOptionsDataManager;
import company.tap.gosellapi.internal.data_managers.payment_options.PaymentType;
import company.tap.gosellapi.internal.viewholders_and_viewmodels.PaymentOptionsBaseViewHolder;

public class PaymentOptionsRecyclerViewAdapter extends RecyclerView.Adapter<PaymentOptionsBaseViewHolder> implements PaymentOptionsBaseViewHolder.PaymentOptionsViewHolderFocusedStateInterface {
    public interface PaymentOptionsViewAdapterListener {
        void currencyHolderClicked();
        void recentPaymentItemClicked(int position, Card recentItem);
        void webPaymentSystemViewHolderClicked(int position);
        void cardScannerButtonClicked();
        void saveCardSwitchCheckedChanged(int position, boolean isChecked);
        void cardDetailsFilled(boolean isFilled, CardRawData cardRawData); //pass null, if isFilled = false
    }

    private int focusedPosition = Constants.NO_FOCUS;

    private PaymentOptionsDataManager dataSource;
    private RecyclerView parent;
    private PaymentOptionsViewAdapterListener listener;

    public PaymentOptionsRecyclerViewAdapter(PaymentOptionsDataManager dataSource, PaymentOptionsViewAdapterListener listener) {
        this.dataSource = dataSource;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PaymentOptionsBaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return PaymentOptionsBaseViewHolder.newInstance(parent, PaymentType.getByViewType(viewType), listener, this);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentOptionsBaseViewHolder holder, int position) {
        //noinspection unchecked
        holder.bind(dataSource.getViewModel(position), position == focusedPosition, position);
    }

    @Override
    public int getItemCount() {
        return dataSource.getSize();
    }

    @Override
    public int getItemViewType(int position) {
        return dataSource.getItemViewType(position);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        parent = recyclerView;
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        parent = null;
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull PaymentOptionsBaseViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.unbind();
    }

    //focus interaction between holders
    @Override
    public void setFocused(int position) {
        PaymentOptionsBaseViewHolder oldHolder;

        if (focusedPosition != Constants.NO_FOCUS) {
            oldHolder = (PaymentOptionsBaseViewHolder) parent.findViewHolderForAdapterPosition(focusedPosition);
            if (oldHolder != null) {
                oldHolder.setFocused(false);
            }
        }

        focusedPosition = position;
        PaymentOptionsBaseViewHolder newHolder = (PaymentOptionsBaseViewHolder) parent.findViewHolderForAdapterPosition(focusedPosition);
        if (newHolder != null) {
            newHolder.setFocused(true);
        }
    }

    public void clearFocus() {
        if (focusedPosition != Constants.NO_FOCUS) {
            PaymentOptionsBaseViewHolder oldHolder = (PaymentOptionsBaseViewHolder) parent.findViewHolderForAdapterPosition(focusedPosition);
            if (oldHolder != null) {
                oldHolder.setFocused(false);
            }
        }
        focusedPosition = Constants.NO_FOCUS;
    }

    public int getFocusedPosition() {
        return focusedPosition;
    }
}
