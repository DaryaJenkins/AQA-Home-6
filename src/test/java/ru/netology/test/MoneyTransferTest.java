package ru.netology.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.pages.DashboardPage;
import ru.netology.pages.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.DataHelper.getAuthInfo;
import static ru.netology.data.DataHelper.getVerificationCodeFor;

class MoneyTransferTest {

    private DashboardPage dashboardPage;
    private DataHelper.AuthInfo info;
    private DataHelper.VerificationCode verificationCode;
    private DataHelper.CardInfo firstCard;
    private DataHelper.CardInfo secondCard;

    private int firstBalanceBefore;
    private int secondBalanceBefore;

    @BeforeEach
    void setUp() {
        info = getAuthInfo();
        verificationCode = getVerificationCodeFor(info);
        firstCard = DataHelper.getFirstCard();
        secondCard = DataHelper.getSecondCard();

        var loginPage = open("http://localhost:9999", LoginPage.class);
        var verificationPage = loginPage.validLogin(info);
        dashboardPage = verificationPage.validVerify(verificationCode);

        firstBalanceBefore = dashboardPage.getCardBalance(firstCard.getId());
        secondBalanceBefore = dashboardPage.getCardBalance(secondCard.getId());
    }

    @Test
    void shouldTransferMoneyBetweenTwoCards() {
        var transferAmount = 1500;
        var transferPage = dashboardPage.selectCardToTransfer(secondCard.getId());
        dashboardPage = transferPage.transferToAnotherCard(transferAmount, firstCard.getNumber());

        var firstBalanceAfter = dashboardPage.getCardBalance(firstCard.getId());
        var secondBalanceAfter = dashboardPage.getCardBalance(secondCard.getId());

        assertEquals(firstBalanceBefore - transferAmount, firstBalanceAfter);
        assertEquals(secondBalanceBefore + transferAmount, secondBalanceAfter);
    }

    @Test
    void checkOverdraftTransfer() {
        var transferAmount = 20000;
        var transferPage = dashboardPage.selectCardToTransfer(firstCard.getId());
        dashboardPage = transferPage.transferToAnotherCard(transferAmount, secondCard.getNumber());

        var firstBalanceAfter = dashboardPage.getCardBalance(firstCard.getId());
        var secondBalanceAfter = dashboardPage.getCardBalance(secondCard.getId());

        assertEquals(firstBalanceBefore, firstBalanceAfter);
        assertEquals(secondBalanceBefore, secondBalanceAfter);
    }
}