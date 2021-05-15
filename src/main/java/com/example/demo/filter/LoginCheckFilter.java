package com.example.demo.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.ui.Model;

public class LoginCheckFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String requestURL = httpRequest.getRequestURI();
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		HttpSession session = httpRequest.getSession();
		Integer id = (Integer) session.getAttribute("id");
		if (requestURL.endsWith("/users/login") || requestURL.endsWith("/") || 
					requestURL.endsWith("/users/registration")) {
			if (id == null) {
				chain.doFilter(request, response);
			} else {
				if (requestURL.endsWith("/users/login")) {
					httpResponse.sendRedirect("/users/" + id);
				}else {
					chain.doFilter(request, response);
				}
			}
		} else if (requestURL.endsWith("/h2-console")) {
			chain.doFilter(request, response);
		}else {
			if (id == null) {
				httpResponse.sendRedirect("/users/login");
			} else {
				chain.doFilter(request, response);
			}
		}
	}
}

class LoginCheck {
	private static void loginCheck(HttpSession session, Model model) {
		Integer id = (Integer)session.getAttribute("id");
		if (id == null) {
			model.addAttribute("loginCheck", "ログアウト中");
		}else {
			model.addAttribute("loginCheck", "ログイン中");
		}
	}
}
