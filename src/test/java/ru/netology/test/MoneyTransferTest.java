package ru.netology.test;

import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.pages.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.DataHelper.getAuthInfo;

class MoneyTransferTest {
    @Test
    void shouldTransferMoneyBetweenTwoCards() {
        var info = getAuthInfo();
        var verificationCode = DataHelper.getVerificationCodeFor(info);
        var firstCard = DataHelper.getFirstCard();
        var secondCard = DataHelper.getSecondCard();

        var loginPage = open("http://localhost:9999", LoginPage.class);
        var verificationPage = loginPage.validLogin(info);
        var dashboardPage = verificationPage.validVerify(verificationCode);

        var firstBalanceBefore = dashboardPage.getCardBalance(firstCard.getId());
        var secondBalanceBefore = dashboardPage.getCardBalance(secondCard.getId());

        var transferAmount = 1500;
        var transferPage = dashboardPage.selectCardToTransfer(secondCard.getId());
        dashboardPage = transferPage.transferToAnotherCard(Integer.valueOf(transferAmount), firstCard.getNumber());

        var firstBalanceAfter = dashboardPage.getCardBalance(firstCard.getId());
        var secondBalanceAfter = dashboardPage.getCardBalance(secondCard.getId());

        assertEquals(firstBalanceBefore - transferAmount, firstBalanceAfter);
        assertEquals(secondBalanceBefore + transferAmount, secondBalanceAfter);
    }
}