package com.inc.dayary.controller;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.inc.dayary.domain.Member;
import com.inc.dayary.service.MemberService;

@Controller
public class MemberController {
	
	@Autowired
	private MemberService memberService;
	
	@GetMapping("/member/signup")
	public String signup(Model model) {
		model.addAttribute("member", new Member());
		return "member/signup";
	}
	
	@PostMapping("/member/signup")
	public String signup(@ModelAttribute 
			             @Valid Member member,
			             BindingResult result,
			             HttpSession session) {
		if(memberService.findOne(member.getId()) != null) {
			result.addError(new FieldError(
				"idError", "id", "중복된 아이디입니다"));
		}
		
		if(!member.isPasswordVaild()) {
			FieldError error = 
					new FieldError("passwordError", 
							       "passwordConfirm", 
				        "패스워드가 일치하지 않습니다");
			result.addError(error);
		}
		if(!member.getEmail().equals(
				(String)session.getAttribute("email"))) {
			FieldError error = 
					new FieldError("emailNotEqualsError", 
							       "email", 
								   "인증받은 메일로 가입해 주세요");
			result.addError(error);
		}
		if(!member.getEmailCode().equals(
				(String)session.getAttribute("emailCode"))) {
			FieldError error = 
					new FieldError("emailCodeError", 
							       "emailCode", 
								   "이메일코드가 일치하지 않습니다");
			result.addError(error);
		}
		
		if(result.hasErrors()) {
			return "member/signup";
		}
		
		memberService.signup(member);
		session.invalidate();
		return "redirect:/";
	}
	
	@PostMapping("/member/dupcheck")
	@ResponseBody
	public String dupcheck(@RequestParam String id) {
		Member member = memberService.findOne(id);
		if(member == null) {
			return "n";
		}else {
			return "y";
		}
	}
	
	@PostMapping("/member/emailcertify")
	@ResponseBody
	public String emailcertify(@RequestParam String email, 
			                   HttpSession session){
		if(email.length() == 0) {
			return "null";
		}
		if(!emailValidator(email)) {
			return "incorrect";
		}
		if(memberService.emailDupCheck(email)) {
			return "duplicated";
		}
		String emailCode = null;
		try {
			emailCode = memberService.sendCertifyEmail(email);
		}catch(Exception e) {
			e.printStackTrace();
			return "error";
		}
		session.setAttribute("email", email);
		session.setAttribute("emailCode", emailCode);
		return "success";
		
	}
	
	@GetMapping("/member/signin")
	public String signin(Model model) {
		model.addAttribute("member", new Member());
		return "member/signin";
	}
	
	@PostMapping("/member/signin")
	public String signin(@ModelAttribute Member member, 
			             BindingResult result,
			             HttpServletRequest request) {
		Member savedMember = memberService.findOne(member.getId());
		if(savedMember == null) {
			result.addError(
					new FieldError("notExsitId", "id", 
							       "존재하지 않는 아이디 입니다"));
		}else if(!savedMember.getPassword().equals(
				member.getPassword())) {
			result.addError(
					new FieldError("passwordNotSame", "password", 
							       "비밀번호가 일치하지 않습니다"));
		}
		if(result.hasErrors()) {
			return "member/signin";
		}
		request.getSession().invalidate();
		request.getSession().setAttribute("member", member);
		
		return "redirect:/";
	}
	
	@GetMapping("/member/signout")
	public String signout(HttpSession session) {
		session.invalidate();
		return "redirect:/member/signin";
	}

	private boolean emailValidator(String email) {
		return Pattern
				.compile("([A-Za-z0-9]+@[A-Za-z0-9]+.[A-Za-z]{2,10})")
				.matcher(email).matches();
	}
}