package company.tap.gosellapi.internal.data_managers.payment_options.viewmodels;

import java.util.ArrayList;

import company.tap.gosellapi.internal.api.models.Card;
import company.tap.gosellapi.internal.api.models.PaymentOption;
import company.tap.gosellapi.internal.data_managers.payment_options.PaymentOptionsDataManager;
import company.tap.gosellapi.internal.viewholders.CardCredentialsViewHolder;

public class CardCredentialsViewModel
        extends PaymentOptionsBaseViewModel<ArrayList<PaymentOption>, CardCredentialsViewHolder, CardCredentialsViewModel> {
    private ArrayList<PaymentOption> dataOriginal;

    private boolean isShowAddressOnCardCell = false;

    // Card credentials fields
    private String cardNumber;
    private String expirationMonth;
    private String expirationYear;
    private String CVVnumber;
    private String nameOnCard;

    public CardCredentialsViewModel(PaymentOptionsDataManager parentDataManager, ArrayList<PaymentOption> data, int modelType) {
        super(parentDataManager, data, modelType);
        dataOriginal = new ArrayList<>(data);

        this.cardNumber = "";
        this.expirationMonth = "";
        this.expirationYear = "";
        this.CVVnumber = "";
        this.nameOnCard = "";
    }

    public void cardScannerButtonClicked() {
        parentDataManager.cardScannerButtonClicked();
    }

    public void addressOnCardClicked() {
        parentDataManager.addressOnCardClicked();
    }

    public void saveCardSwitchClicked(boolean state) {
        parentDataManager.saveCardSwitchCheckedChanged(state, position + 1);
    }

    public void cardExpirationDateClicked() {
        parentDataManager.cardExpirationDateClicked();
    }

    public void setCardSwitchHeight(int cardSwitchHeight) {
        parentDataManager.setCardSwitchHeight(cardSwitchHeight);
    }

    public void filterByCurrency(String currencyCode) {
        data = new ArrayList<>();
        for (PaymentOption paymentOption : dataOriginal) {
            if (paymentOption.getSupported_currencies().contains(currencyCode)) {
                data.add(paymentOption);
            }
        }

        updateData();
    }

    public void binNumberEntered(String binNumber) { parentDataManager.binNumberEntered(binNumber);}

    public String getCardNumber() {
        return cardNumber;
    }

    public String getExpirationMonth() {
        return expirationMonth;
    }

    public String getExpirationYear() {
        return expirationYear;
    }

    public boolean isShowAddressOnCardCell() {
        return isShowAddressOnCardCell;
    }

    public String getCVVnumber() {
        return CVVnumber;
    }

    public String getNameOnCard() {
        return nameOnCard;
    }

    public void showAddressOnCardCell(boolean isShow) {
        this.isShowAddressOnCardCell = isShow;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setExpirationMonth(String expirationMonth) {
        this.expirationMonth = expirationMonth;
    }

    public void setExpirationYear(String expirationYear) {
        this.expirationYear = expirationYear;
    }

    public void setCVVnumber(String CVVnumber) {
        this.CVVnumber = CVVnumber;
    }

    public void setNameOnCard(String nameOnCard) {
        this.nameOnCard = nameOnCard;
    }
}
