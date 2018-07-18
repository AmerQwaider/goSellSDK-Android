package company.tap.gosellapi.internal.viewholders;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import company.tap.gosellapi.R;
import company.tap.gosellapi.internal.adapters.CardSystemsRecyclerViewAdapter;
import company.tap.gosellapi.internal.api.callbacks.APIRequestCallback;
import company.tap.gosellapi.internal.api.callbacks.GoSellError;
import company.tap.gosellapi.internal.api.facade.GoSellAPI;
import company.tap.gosellapi.internal.api.models.PaymentOption;
import company.tap.gosellapi.internal.api.responses.BINLookupResponse;
import company.tap.gosellapi.internal.data_managers.payment_options.viewmodels.CardCredentialsViewModel;
import company.tap.tapcardvalidator_android.CardBrand;
import company.tap.tapcardvalidator_android.CardValidator;
import company.tap.tapcardvalidator_android.DefinedCardBrand;

public class CardCredentialsViewHolder
        extends PaymentOptionsBaseViewHolder<ArrayList<PaymentOption>, CardCredentialsViewHolder, CardCredentialsViewModel> {
    private final static int BIN_NUMBER_LENGTH = 6;

    ImageButton cardScannerButton;
    private View addressOnCardLayout;
    private SwitchCompat saveCardSwitch;
    private ArrayList<PaymentOption> data;

    private EditText cardNumberField;
    private EditText CVVField;
    private EditText expirationDateField;
    private EditText nameOnCardField;

    CardCredentialsViewHolder(View view) {
        super(view);

        cardScannerButton = itemView.findViewById(R.id.cardScannerButton);
        addressOnCardLayout = itemView.findViewById(R.id.addressOnCardLayout);
        saveCardSwitch = itemView.findViewById(R.id.saveCardSwitch);

        // Configure edit fields
        cardNumberField = itemView.findViewById(R.id.cardNumberField);

        // Card number field
        cardNumberField.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateCardNumber(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // CVVField
        CVVField = itemView.findViewById(R.id.CVVField);

        // Date field
        expirationDateField = itemView.findViewById(R.id.expirationDateField);

        expirationDateField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    viewModel.cardExpirationDateClicked();
                    expirationDateField.clearFocus();
                }
            }
        });

        // Name on card
        nameOnCardField = itemView.findViewById(R.id.nameOnCardField);
    }

    @Override
    public void bind(ArrayList<PaymentOption> data) {

        this.data = data;

        cardScannerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.cardScannerButtonClicked();
            }
        });

        addressOnCardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.addressOnCardClicked();
            }
        });

        saveCardSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                viewModel.saveCardSwitchClicked(isChecked);
            }
        });

        Log.e("TEST", "CARD NUMBER " + viewModel.getCardNumber());
        if (!viewModel.getCardNumber().isEmpty()) {
            cardNumberField.setText(viewModel.getCardNumber());
        }

        Log.e("TEST", "EXPIRATION DATE " + viewModel.getExpirationMonth() + "/" + viewModel.getExpirationYear());
        if (!viewModel.getExpirationMonth().isEmpty() && !viewModel.getExpirationYear().isEmpty()) {
            String expirationDate = viewModel.getExpirationMonth() + "/" + viewModel.getExpirationYear();
            expirationDateField.setText(expirationDate);
        }

        Log.e("TEST", "CVV NUMBER " + viewModel.getCVVnumber());
//        if (!viewModel.getCVVnumber().isEmpty()) {
//            CVVField.setText(viewModel.getCVVnumber());
//        }

        Log.e("TEST", "NAME ON CARD " + viewModel.getNameOnCard());
//        if (!viewModel.getNameOnCard().isEmpty()) {
//            nameOnCardField.setText(viewModel.getNameOnCard());
//        }

        RelativeLayout saveCardSwitchContainer = itemView.findViewById(R.id.saveCardSwitchContainer);
        saveCardSwitchContainer.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        viewModel.setCardSwitchHeight(saveCardSwitchContainer.getMeasuredHeight());

        initCardSystemsRecyclerView();
    }

    @Override
    public void setFocused(boolean isFocused) {
        itemView.setSelected(isFocused);
    }

    private void initCardSystemsRecyclerView() {
        RecyclerView cardSystemsRecyclerView = itemView.findViewById(R.id.cardSystemsRecyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.VERTICAL, false);
        cardSystemsRecyclerView.setLayoutManager(linearLayoutManager);

        CardSystemsRecyclerViewAdapter adapter = new CardSystemsRecyclerViewAdapter(data);
        cardSystemsRecyclerView.setAdapter(adapter);
    }

    private void updateCardSystemsRecyclerView(CardBrand brand) {
        RecyclerView cardSystemsRecyclerView = itemView.findViewById(R.id.cardSystemsRecyclerView);

        CardSystemsRecyclerViewAdapter adapter = (CardSystemsRecyclerViewAdapter) cardSystemsRecyclerView.getAdapter();
        adapter.updateForCardBrand(brand);
    }

    public void updateAddressOnCardView(boolean isShow) {
        if (isShow) {
            addressOnCardLayout.setVisibility(View.VISIBLE);
        } else {
            addressOnCardLayout.setVisibility(View.GONE);
        }
    }

    private void validateCardNumber(String cardNumber) {
        DefinedCardBrand brand = CardValidator.validate(cardNumber);
        updateCardSystemsRecyclerView(brand.getCardBrand());

        if (cardNumber.length() == BIN_NUMBER_LENGTH) {

            GoSellAPI.getInstance().retrieveBINLookupBINLookup(cardNumber, new APIRequestCallback<BINLookupResponse>() {
                @Override
                public void onSuccess(int responseCode, BINLookupResponse serializedResponse) {
                }

                @Override
                public void onFailure(GoSellError errorDetails) {
                }
            });
        }
    }
}