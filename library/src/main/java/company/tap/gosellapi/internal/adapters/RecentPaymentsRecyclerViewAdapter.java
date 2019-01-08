package company.tap.gosellapi.internal.adapters;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.card.MaterialCardView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import company.tap.gosellapi.R;
import company.tap.gosellapi.internal.Constants;
import company.tap.gosellapi.internal.api.models.Card;
import company.tap.gosellapi.internal.api.models.SavedCard;

public class RecentPaymentsRecyclerViewAdapter extends RecyclerView.Adapter<RecentPaymentsRecyclerViewAdapter.RecentPaymentsViewHolder> {

    public interface RecentPaymentsRecyclerViewAdapterListener {
        void recentPaymentItemClicked(int position);
    }

    private ArrayList<SavedCard> datasource;
    private RecyclerView parent;
    private int focusedPosition = Constants.NO_FOCUS;
    private RecentPaymentsRecyclerViewAdapterListener listener;


    public RecentPaymentsRecyclerViewAdapter(ArrayList<SavedCard> datasource, RecentPaymentsRecyclerViewAdapterListener listener) {
        this.datasource = datasource;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecentPaymentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gosellapi_viewholder_recent_payments, parent, false);
        return new RecentPaymentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentPaymentsViewHolder holder, int position) {
        SavedCard card = datasource.get(position);
        holder.bind(position, card);
        //holder.setFocused(position == focusedPosition);
    }

    @Override
    public int getItemCount() {
        return datasource.size();
    }

    public void setFocused(boolean focused) {

        setFocused(focused ? focusedPosition : Constants.NO_FOCUS);

    }

    public void clearFocus()
    {
        System.out.println("afterText changed ... adapter.clear "+focusedPosition);
        RecentPaymentsViewHolder oldHolder;
        parent.clearFocus();
        if (focusedPosition != Constants.NO_FOCUS) {
            oldHolder = (RecentPaymentsViewHolder) parent.findViewHolderForAdapterPosition(focusedPosition);
            if (oldHolder != null) {
                oldHolder.setFocused(false);
            }
        }
        listener.recentPaymentItemClicked(Constants.NO_FOCUS);
    }
    private void setFocused(int position) {
        RecentPaymentsViewHolder oldHolder;

        if (focusedPosition != Constants.NO_FOCUS) {
            oldHolder = (RecentPaymentsViewHolder) parent.findViewHolderForAdapterPosition(focusedPosition);
            if (oldHolder != null) {
                oldHolder.setFocused(false);
            }
        }

        focusedPosition = position;
        RecentPaymentsViewHolder newHolder = (RecentPaymentsViewHolder) parent.findViewHolderForAdapterPosition(position);
        if (newHolder != null) {
            newHolder.setFocused(true);
        }

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

    public class RecentPaymentsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private int position;
        private SavedCard card;
        private ImageView itemCheckmark;
        private ImageView logoImageView;
        private RelativeLayout recentPaymentsCardViewLayout;
        private MaterialCardView cardView;
        private MaterialCardView childCard;

        private RecentPaymentsViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        private void bind(int position, SavedCard card) {
            this.position = position;
            this.card = card;

            recentPaymentsCardViewLayout = itemView.findViewById(R.id.recentPaymentsCardViewLayout);

            String cardNumber = String.format(itemView.getResources().getString(R.string.textview_placeholder_last_four_digits), card.getLastFour());
            TextView cardLastDigits = itemView.findViewById(R.id.cardLastDigits);
            cardLastDigits.setText(cardNumber);
            cardView = itemView.findViewById(R.id.parent_card);
            childCard = itemView.findViewById(R.id.childCard);
            itemCheckmark = itemView.findViewById(R.id.itemCheckmark);
            logoImageView = itemView.findViewById(R.id.logoImageView);
            Glide.with(itemView.getContext()).load(card.getImage()).into(logoImageView);
        }

        @Override
        public void onClick(View v) {
            RecentPaymentsRecyclerViewAdapter.this.setFocused(position);
            listener.recentPaymentItemClicked(position);
        }


        private void setFocused(boolean focused) {
            if(focused){
                itemCheckmark.setVisibility(View.VISIBLE);
                cardView.setStrokeWidth(1);
                cardView.setStrokeColor(itemView.getResources().getColor(R.color.vibrant_green));

            }else {
                itemCheckmark.setVisibility(View.INVISIBLE);
                cardView.setStrokeWidth(0);
            }

            recentPaymentsCardViewLayout.setSelected(focused);
        }
    }
}
