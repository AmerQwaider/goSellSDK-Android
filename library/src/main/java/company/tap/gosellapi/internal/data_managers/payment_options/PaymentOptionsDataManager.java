package company.tap.gosellapi.internal.data_managers.payment_options;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import company.tap.gosellapi.internal.Constants;
import company.tap.gosellapi.internal.api.models.Card;
import company.tap.gosellapi.internal.api.models.CardRawData;
import company.tap.gosellapi.internal.api.models.PaymentOption;
import company.tap.gosellapi.internal.api.responses.PaymentOptionsResponse;
import company.tap.gosellapi.internal.data_managers.GlobalDataManager;
import company.tap.gosellapi.internal.data_managers.payment_options.viewmodels.CardCredentialsViewModel;
import company.tap.gosellapi.internal.data_managers.payment_options.viewmodels.CurrencyViewModel;
import company.tap.gosellapi.internal.data_managers.payment_options.viewmodels.PaymentOptionsBaseViewModel;
import company.tap.gosellapi.internal.data_managers.payment_options.viewmodels.RecentSectionViewModel;
import company.tap.gosellapi.internal.data_managers.payment_options.viewmodels.WebPaymentViewModel;

public class PaymentOptionsDataManager {
    //outer interface (for fragment, containing recyclerView)
    public interface PaymentOptionsDataListener {
        void startCurrencySelection(HashMap<String, Double> currencies, String selectedCurrency);
        void startOTP();
        void startWebPayment();
        void startScanCard();
        void cardDetailsFilled(boolean isFilled, CardRawData cardRawData);
        void addressOnCardClicked();
    }

    private PaymentOptionsDataListener listener;

    private PaymentOptionsResponse paymentOptionsResponse;
    private ArrayList<PaymentOptionsBaseViewModel> dataListOriginal;
    private ArrayList<PaymentOptionsBaseViewModel> dataListFiltered;
    private int focusedPosition = Constants.NO_FOCUS;
    
    public PaymentOptionsDataManager(PaymentOptionsResponse paymentOptionsResponse) {
        this.paymentOptionsResponse = paymentOptionsResponse;
        new DataFiller().fill();
    }

    //region data for adapter
    public int getSize() {
        return dataListFiltered.size();
    }

    public int getItemViewType(int position) {
        return dataListFiltered.get(position).getModelType();
    }

    public PaymentOptionsBaseViewModel getViewModel(int position) {
        return dataListFiltered.get(position);
    }

    public PaymentOptionsDataManager setListener(PaymentOptionsDataListener listener) {
        this.listener = listener;
        return this;
    }
    //endregion

    //region callback actions from child viewModels
    public void currencyHolderClicked(int position, HashMap<String, Double> currencies) {
        CurrencySectionData currencySectionData = ((CurrencyViewModel)dataListFiltered.get(position)).getData();
        listener.startCurrencySelection(currencies, currencySectionData.getSelectedCurrencyCode());
    }

    public void recentPaymentItemClicked(int position, Card recentItem) {
        setFocused(position);
        listener.startOTP();
    }

    public void webPaymentSystemViewHolderClicked(int position) {
        setFocused(position);
        listener.startWebPayment();
    }

    public void cardScannerButtonClicked() {
        listener.startScanCard();
    }

    public void saveCardSwitchCheckedChanged(int position, boolean isChecked) {
        //show or hide save card details by modifying dataSource?
    }

    public void cardDetailsFilled(boolean isFilled, CardRawData cardRawData) {
        listener.cardDetailsFilled(isFilled, cardRawData);
    }

    public void addressOnCardClicked() {
        listener.addressOnCardClicked();
    }
    //endregion

    //region update actions from activity
    private PaymentOptionsBaseViewModel getViewModelByType(PaymentType type) {
        for (PaymentOptionsBaseViewModel viewModel : dataListFiltered) {
            if (viewModel.getModelType() == type.getViewType()) {
                return viewModel;
            }
        }

        return null;
    }

    public void currencySelectedByUser(String userChoiceCurrency) {
        PaymentOptionsBaseViewModel baseViewModel = getViewModelByType(PaymentType.CURRENCY);
        if (baseViewModel == null || !(baseViewModel instanceof CurrencyViewModel)) return;

        CurrencyViewModel currencyViewModel = (CurrencyViewModel) baseViewModel;
        CurrencySectionData currencySectionData = currencyViewModel.getData();

        currencySectionData.setUserChoiceData(userChoiceCurrency, currencySectionData.getData().get(userChoiceCurrency));
        currencyViewModel.updateData();
    }
    //endregion

    //region focus interaction between holders
    private void setFocused(int position) {
        if (focusedPosition != Constants.NO_FOCUS) {
            dataListFiltered.get(focusedPosition).setViewFocused(false);
        }

        focusedPosition = position;
        dataListFiltered.get(focusedPosition).setViewFocused(true);
    }

    public void clearFocus() {
        if (focusedPosition != Constants.NO_FOCUS) {
            dataListFiltered.get(focusedPosition).setViewFocused(false);
        }
        focusedPosition = Constants.NO_FOCUS;
    }

    public boolean isPositionInFocus(int position) {
        return position == focusedPosition;
    }
    //endregion

    //save/restore state
    public void saveState() {
        for (PaymentOptionsBaseViewModel viewModel : dataListFiltered) {
            viewModel.saveState();
        }
    }

    private final class DataFiller {
        private void fill() {
            dataListOriginal = new ArrayList<>();
            for (PaymentType paymentType : PaymentType.values()) {
                switch (paymentType) {
                    case CURRENCY:
                        addCurrencies();
                        break;
                    case RECENT:
                        addRecent();
                        break;
                    case WEB:
                        addWeb();
                        break;
                    case CARD:
                        addCard();
                        break;
                }
            }

            dataListFiltered = new ArrayList<>(dataListOriginal);
        }

        private void addCurrencies() {
            HashMap<String, Double> supportedCurrencies = paymentOptionsResponse.getSupported_currencies();
            if (supportedCurrencies != null && supportedCurrencies.size() > 0) {
                String initialCurrency = paymentOptionsResponse.getCurrency_code();
                double initialAmount = GlobalDataManager.getInstance().getPaymentInfo().getTotal_amount();
                CurrencySectionData currencySectionData = new CurrencySectionData(supportedCurrencies, initialCurrency, initialAmount);

                dataListOriginal.add(new CurrencyViewModel(PaymentOptionsDataManager.this, currencySectionData, PaymentType.CURRENCY.getViewType()));
            }
        }

        private void addRecent() {
            ArrayList<Card> recentCards = new ArrayList<>();//paymentOptionsResponse.getCards();

            // TODO - temporary Card filler
            for(int i = 0; i < 20; i++) {
                recentCards.add(createCardData());
            }

            if (recentCards != null && recentCards.size() > 0) {
                dataListOriginal.add(new RecentSectionViewModel(PaymentOptionsDataManager.this, recentCards, PaymentType.RECENT.getViewType()));
            }
        }

        private void addWeb() {
            ArrayList<PaymentOption> paymentOptions = paymentOptionsResponse.getPayment_options();

            if (paymentOptions == null || paymentOptions.size() == 0) {
                return;
            }

            for (PaymentOption paymentOption : paymentOptions) {
                if (paymentOption.getPayment_type().equalsIgnoreCase(CardPaymentType.WEB.getValue())) {
                    for (int i = 0; i < 20; i++) {
                        dataListOriginal.add(new WebPaymentViewModel(PaymentOptionsDataManager.this, paymentOption, PaymentType.WEB.getViewType()));
                    }
                }
            }
        }

        private void addCard() {
            ArrayList<PaymentOption> paymentOptions = paymentOptionsResponse.getPayment_options();
            ArrayList<PaymentOption> paymentOptionsCards = new ArrayList<>();

            if (paymentOptions == null || paymentOptions.size() == 0) {
                return;
            }

            for (PaymentOption paymentOption : paymentOptions) {
                if (paymentOption.getPayment_type().equalsIgnoreCase(CardPaymentType.CARD.getValue())) {
                    paymentOptionsCards.add(paymentOption);
                }
            }

            if (paymentOptionsCards.size() > 0) {
                dataListOriginal.add(new CardCredentialsViewModel(PaymentOptionsDataManager.this, paymentOptionsCards, PaymentType.CARD.getViewType()));
            }
        }

        // TODO remove after real data will be received
        private String cardJSONString = "{\n" +
                "      \"id\": \"crd_q3242434\",\n" +
                "      \"object\": \"card\",\n" +
                "      \"last4\": \"1025\",\n" +
                "      \"exp_month\": \"02\",\n" +
                "      \"exp_year\": \"25\",\n" +
                "      \"brand\": \"VISA\",\n" +
                "      \"name\": \"test\",\n" +
                "      \"bin\": \"415254\"\n" +
                "    }";

        private Card createCardData() {
            Gson mapper = new Gson();
            return mapper.fromJson(cardJSONString, Card.class);
        }
    }
}
