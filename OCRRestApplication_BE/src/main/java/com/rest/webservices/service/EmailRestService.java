package com.rest.webservices.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.rest.webservices.EmailData;

@RestController
public class EmailRestService {

	
	 @Autowired
	 private JavaMailSender sender;
	  
	 @Autowired
	 private SpringTemplateEngine springTemplateEngine;
	
	@PostMapping(path="/validatecheque")
	//@GetMapping(path="/validatecheque")
	@CrossOrigin(origins = "*", allowCredentials = "true", allowedHeaders = "*")
	public ResponseEntity<String> sendEmailService(@RequestBody EmailData data) {
		
		data.setAccNo("1234567890");
		data.setAmount("1000");
		data.setBankName("XYZ Bank Limited");
		data.setBankUser("SWAPNIL THITE");
		data.setEmail("swapnil.thite19@gmail.com");
		data.setMobile("1234567890");
		
		if(data !=null) {
			String user = data.getBankUser();
			String amount = data.getAmount();
			String chequeNo = data.getChequeNumber();
			String bankName = data.getBankName();
			String accNo = data.getAccNo();
			String email = data.getEmail();
			String mobile = data.getMobile();
			String approve = "http://10.252.47.168:8008/OnlineChequeSubmissionProject/api/verify?amount="+amount+"&bankname="+bankName+"&cq_no="+chequeNo+"&acc_no="+accNo+"&flag_approval=true";
			String disapprove = "http://10.252.47.168:8008/OnlineChequeSubmissionProject/api/verify?amount="+amount+"&bankname="+bankName+"&cq_no="+chequeNo+"&acc_no="+accNo+"&flag_approval=false";
			
			final Context ctx = new Context();
			ctx.setVariable("bankUser", user);
			ctx.setVariable("amount", amount);
			ctx.setVariable("chequeNo", chequeNo);
			ctx.setVariable("bankName", bankName);
			ctx.setVariable("accNo", accNo);
			ctx.setVariable("approve", approve);
			ctx.setVariable("disapprove", disapprove);
			 MimeMessage message = sender.createMimeMessage();
		    MimeMessageHelper helper = new MimeMessageHelper(message);
		    final String htmlContent = this.springTemplateEngine.process("emailTemplate", ctx);
			try {
				helper.setTo(email);
				helper.setSubject( "Request for Cheque Approval");
				//helper.setText(approve);
				helper.setText(htmlContent, true);
				sender.send(message);
				return new ResponseEntity<String>("Email Sent !!!",HttpStatus.OK);
			} catch (MessagingException e) {
				return new ResponseEntity<String>("Error Occured!!",HttpStatus.EXPECTATION_FAILED);
			}
		}
		return new ResponseEntity<String>("Error Occured!!",HttpStatus.EXPECTATION_FAILED);
	}
}
