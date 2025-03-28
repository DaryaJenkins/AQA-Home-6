package ru.netology.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;

public class TransferPage {
    private SelenideElement amountInput = $("[data-test-id=amount] input");
    private SelenideElement transferFrom = $("[data-test-id=from] input");
    private SelenideElement transferButton = $("[data-test-id=action-transfer]");

    public DashboardPage transferToAnotherCard(int amount, String fromCardNumber) {
        amountInput.setValue(String.valueOf(amount));
        transferFrom.setValue(fromCardNumber);
        transferButton.click();
        return page(DashboardPage.class);
    }
}
