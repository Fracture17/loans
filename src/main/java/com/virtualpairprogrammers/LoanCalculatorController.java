package com.virtualpairprogrammers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoanCalculatorController {

	@Autowired
	private LoanRepository data;
	
	@Autowired
	private JavaMailSender mailSender;

	private RestTemplate restTemplate = new RestTemplate();
	
	// Render the form
	@RequestMapping(value="/",method=RequestMethod.GET)
	public ModelAndView renderNewLoanForm()
	{
		LoanApplication loan = new LoanApplication();
		return new ModelAndView("newApplication","form",loan);
	} 

	@RequestMapping(value="/",method=RequestMethod.POST)
	public ModelAndView processNewLoanApplication(LoanApplication loan)
	{
		data.save(loan);

		URI location = restTemplate.postForLocation("http://loans.virtualpairprogrammers.com/loanApplication", loan); //this line sends the loan for approval request, which could take up to 24 hours
		
		BigDecimal applicableRate = loan.getInterestRate().divide(new BigDecimal("100"));
		applicableRate = applicableRate.add(new BigDecimal("1"));
		
		BigDecimal totalRepayable = new BigDecimal(loan.getPrincipal() * Double.parseDouble(applicableRate.toString()) * loan.getTermInMonths() / 12);
		BigDecimal repayment = totalRepayable.divide(new BigDecimal("" + loan.getTermInMonths()),RoundingMode.UP);
		loan.setRepayment(repayment);
		
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(loan.getName());
		message.setSubject("Thank you for your loan application.");
		message.setText("We're currently processing your request, and will send you a further email when we have a decision.");
		mailSender.send(message);
		
		return new ModelAndView("requestAccepted");
	} 
	
	
}
