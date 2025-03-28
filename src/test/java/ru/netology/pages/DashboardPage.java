package ru.netology.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.page;

public class DashboardPage {

    private ElementsCollection cards = $$(".list__item div");
    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";

    public DashboardPage() {
    }

    public int getCardBalance(String id) {
        SelenideElement card = cards.findBy(attribute("data-test-id", id));
        var text = card.text();
        return extractBalance(text);
    }

    public TransferPage selectCardToTransfer(String id) {
        SelenideElement card = cards.findBy(attribute("data-test-id", id));
        card.$("[data-test-id=action-deposit]").click();
        return page(TransferPage.class);
    }

    private int extractBalance(String text) {
        var start = text.indexOf(balanceStart);
        var finish = text.indexOf(balanceFinish);
        var value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value);
    }
}