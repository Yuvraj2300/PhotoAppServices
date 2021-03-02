package com.ys.dev.photoapp.api.account.ui;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("accounts")
public class AccountController {

	@GetMapping("/check")
	public	String	checkWorking() {
		return "Working";
	}
}
