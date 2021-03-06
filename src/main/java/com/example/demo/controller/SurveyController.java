package com.example.demo.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.entity.Survey;
import com.example.demo.repository.SurveyRepository;

@Controller
public class SurveyController {
	@Autowired
	SurveyRepository repository;
	
	@GetMapping("/surveys")
	public String index(Model model) {
		model.addAttribute("surveys", repository.findAll());
		return "survey/index";
	}
	
	@GetMapping("/surveys/new")
	public String newSurvey(@ModelAttribute Survey survey) {
		return "survey/new";
	}
	
	@PostMapping("/surveys")
	public String create(@Valid @ModelAttribute Survey survey, BindingResult result) {
		if (result.hasErrors()) {
			return newSurvey(survey);
		}
		repository.save(survey);
		return "redirect:/surveys/" + survey.getId();
	}
	
	@GetMapping("/surveys/{id}")
	public String show(@PathVariable int id, Model model) {
		model.addAttribute("survey", repository.getOne(id));
		return "survey/show";
	}
	
	@GetMapping("/surveys/{id}/edit")
	public String edit(@PathVariable int id, Model model) {
		model.addAttribute("survey", repository.getOne(id));
		return "survey/edit";
	}
	
	@PutMapping("/surveys/{id}")
	public String update(@PathVariable int id, @ModelAttribute Survey survey) {
		repository.save(survey);
		return "redirect:/surveys/" + survey.getId();
	}
	
	@DeleteMapping("/surveys/{id}")
	public String destroy(@PathVariable int id) {
		repository.deleteById(id);
		return "redirect:/surveys";
	}
	
	@RequestMapping("searches/{comment}")
	public String searchLike(@PathVariable String comment, Model model) {
		model.addAttribute("survey", repository.findByCommentLike("%" + comment + "%"));
		return "survey/index";
	}
	
	
	
}











