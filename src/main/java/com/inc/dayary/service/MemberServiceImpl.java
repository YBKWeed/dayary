package com.inc.dayary.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.inc.dayary.domain.Member;
import com.inc.dayary.repository.MemberDao;

@Service
public class MemberServiceImpl implements MemberService{
	
	@Autowired
	private MemberDao memberDao;
	
	@Autowired 
	private JavaMailSender javaMailSender;

	@Override
	public Member findOne(String id) {
		return memberDao.findOne(id);
	}

	@Override
	public boolean emailDupCheck(String email) {
		Member member = memberDao.getfindOneByEmail(email);
		return member != null;
	}

	@Override
	public String sendCertifyEmail(String email) {
		String from = "dudqja1003@gmail.com";
		String subject = "Dayary 이메일 인증";
		String emailCode = makeRandomCode();
		String content = "인증코드 : "+emailCode;
		
		
		try {
			MimeMessage msg = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = 
					new MimeMessageHelper(msg, true, "UTF-8");
			helper.setFrom(from);
			helper.setTo(email);
			helper.setSubject(subject);
			helper.setText(content);
			javaMailSender.send(msg);
		} catch (MessagingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		
		return emailCode;
	}
	
	public String makeRandomCode() {
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<4;i++) {
			sb.append((int)(Math.random()*10));
		}
		return sb.toString();
	}

	@Override
	public void signup(Member member) {
		memberDao.signUp(member);
	}
	
}





