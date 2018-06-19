package com.inc.dayary.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.inc.dayary.domain.Diary;
import com.inc.dayary.service.DiaryService;

@Controller
public class DiaryController {
	
	@Autowired
	DiaryService diaryService;
	
	@GetMapping("/")
	public String main(Model model) {
		model.addAttribute("diaryList", diaryService.list());
		return "main";
	}
	
	@GetMapping("/diary/add")
	public String add(Model model) {
		model.addAttribute("diary", new Diary());
		return "diary/add";
	}
	
	@PostMapping("/diary/add")
	public String add(@ModelAttribute @Valid Diary diary,
					  BindingResult result,
					  Model model) {
		if(result.hasErrors()) {
			return "diary/add";
		}
		diaryService.add(diary);
		return "redirect:/";
	}
}