package company.tap.gosellapi.internal.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Iterator;

import company.tap.gosellapi.R;
import company.tap.gosellapi.internal.api.enums.PaymentType;
import company.tap.gosellapi.internal.api.models.PaymentOption;
import company.tap.tapcardvalidator_android.CardBrand;


public class CardSystemsRecyclerViewAdapter extends RecyclerView.Adapter<CardSystemViewHolder> {

    private ArrayList<PaymentOption> data;
    private  ArrayList<PaymentOption> initialData;

    public CardSystemsRecyclerViewAdapter(ArrayList<PaymentOption> data) {
        this.data = data;
        filterPaymentOptions();


    }

    @NonNull
    @Override
    public CardSystemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gosellapi_viewholder_card_system, parent, false);
        return new CardSystemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardSystemViewHolder holder, int position) {
        PaymentOption option = data.get(position);
        Glide.with(holder.itemView.getContext()).load(option.getImage()).into(holder.cardSystemIcon);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private void filterPaymentOptions(){

        for(Iterator<PaymentOption> it = data.iterator(); it.hasNext();) {
            PaymentOption p = it.next();
            System.out.println("payment type : " + p.getName());
            if(p.getPaymentType() != PaymentType.CARD) {
                it.remove();
            }
        }
       this.initialData = (ArrayList<PaymentOption>) data.clone();

    }

    public void updateForCardBrand(CardBrand brand) {

        if(brand == null) {
            data = new ArrayList<>(initialData);
            notifyDataSetChanged();
            return;
        }

        data.clear();

        for(PaymentOption option : initialData) {

           ArrayList<CardBrand> cardBrands = option.getSupportedCardBrands();
            System.out.println("brand :"+ brand);

           if(cardBrands.contains(brand)) {
               data.add(option);
           }
        }
        if(data.size() == 0)
            data = new ArrayList<>(initialData);

        notifyDataSetChanged();
    }
}

class CardSystemViewHolder extends RecyclerView.ViewHolder {
    ImageView cardSystemIcon;

    CardSystemViewHolder(View itemView) {
        super(itemView);

        cardSystemIcon = itemView.findViewById(R.id.cardSystemIcon);
    }
}