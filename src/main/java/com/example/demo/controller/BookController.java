package com.example.demo.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.Book;
import com.example.demo.entity.User;
import com.example.demo.form.BookForm;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.UserRepository;

@Controller
public class BookController {
	@Autowired
	BookRepository repository;
	
	@Autowired
	UserRepository userRepository;
	
	@GetMapping("/books/new")
	public String bookNew(@ModelAttribute BookForm book) {
		return "books/new";
	}
	
	@PostMapping("/books")
	public String create(@Valid @ModelAttribute BookForm form, BindingResult result,
			HttpSession session, RedirectAttributes flash) {
		if (result.hasErrors()) {
			flash.addFlashAttribute("org.springframework.validation.BindingRes ult.bookForm", result);
			flash.addFlashAttribute("bookForm", form);
			return "/books/new";
		}
		Book book = new Book();
		book.setTitle(form.getTitle());
		book.setText(form.getText());
		User user = userRepository.getOne((Integer)session.getAttribute("id"));
		book.setUser(user);
		repository.save(book);
		flash.addFlashAttribute("flash", "投稿が成功しました");
		return "redirect:/books";
	}
	
	@GetMapping("/books")
	public String index(BookForm form, Model model) {
		model.addAttribute("books", repository.findAll());
		String flash = (String) model.getAttribute("flash");
		model.addAttribute("flash", flash);
		return "books/index";
	}
	
	@GetMapping("/books/{id}")
	public String show(@PathVariable int id, Model model) {
		model.addAttribute("book", repository.getOne(id));
		return "books/show";
	}
	
	@GetMapping("/books/{id}/edit")
	public String edit(@PathVariable int id, Model model) {
		model.addAttribute("bookForm", repository.getOne(id));
		return "books/edit";
	}
	
	@PatchMapping("/books/{id}")
	public String update(@Valid @ModelAttribute BookForm form, BindingResult result,
			@PathVariable int id, RedirectAttributes flash) {
		if (result.hasErrors()) {
			flash.addFlashAttribute("org.springframework.validation.BindingRes ult.bookForm", result);
			flash.addFlashAttribute("bookForm", form);
			return "/books/edit";
		}
		Book book = repository.getOne(id);
		book.setTitle(form.getTitle());
		book.setText(form.getText());
		book.setUser(userRepository.getOne(id));
		repository.save(book);
		return "redirect:/books/" + id;
	}
	
	@DeleteMapping("/books/{id}")
	public String update(@PathVariable int id, RedirectAttributes flash, 
			HttpSession session) {
		Book book = repository.getById(id);
		if (book.getUser().getId() == session.getAttribute("id")) {
			repository.delete(book);
			flash.addFlashAttribute("bookDelete", "ID：" + id + "を削除しました");
		}
		return "redirect:/books";
	}
	
	@GetMapping("/books/search")
	public String search(@RequestParam("title") String title, Model model) {
		model.addAttribute("books", repository.findByTitleLike("%" + title + "%"));
		return "books/index";
	}
}






