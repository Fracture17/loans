package com.virtualpairprogrammers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Spy;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class RepaymentAmountTests {
    @Spy
    LoanApplication loan;

    LoanCalculatorController controller;
    LoanRepository repository;
    RestTemplate template;
    JavaMailSender sender;

    @Before
    public void setup() {
        controller = new LoanCalculatorController();

        repository = mock(LoanRepository.class);
        controller.setData(repository);

        template = mock(RestTemplate.class);
        controller.setRestTemplate(template);

        sender = mock(JavaMailSender.class);
        controller.setMailSender(sender);

        loan = spy(new LoanApplication());
    }

    @Test
    public void checkALoanRepayment() {
        doReturn(new BigDecimal(7)).when(loan).getInterestRate();
        loan.setTermInMonths(24);
        loan.setPrincipal(1000);

        controller.processNewLoanApplication(loan);

        assertEquals(new BigDecimal(48), loan.getRepayment());
    }

    @Test
    public void check1YearLoanWholePounds() {
        doReturn(new BigDecimal(10)).when(loan).getInterestRate();
        loan.setTermInMonths(12);
        loan.setPrincipal(1200);

        controller.processNewLoanApplication(loan);

        assertEquals(new BigDecimal(110), loan.getRepayment());
    }

    @Test
    public void check2YearLoanWholePounds() {
        doReturn(new BigDecimal(10)).when(loan).getInterestRate();
        loan.setTermInMonths(24);
        loan.setPrincipal(1200);

        controller.processNewLoanApplication(loan);

        assertEquals(new BigDecimal(60), loan.getRepayment());
    }

    @Test
    public void check5YearLoanWholeRounding() {
        doReturn(new BigDecimal(6.5)).when(loan).getInterestRate();
        loan.setTermInMonths(60);
        loan.setPrincipal(5000);

        controller.processNewLoanApplication(loan);

        assertEquals(new BigDecimal(111), loan.getRepayment());
    }
}
