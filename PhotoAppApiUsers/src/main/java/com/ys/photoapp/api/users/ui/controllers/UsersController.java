package com.ys.photoapp.api.users.ui.controllers;

import java.util.List;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ys.photoapp.api.users.shared.UsersDto;
import com.ys.photoapp.api.users.ui.model.CreateUserResponseModel;
import com.ys.photoapp.api.users.ui.model.CreateUsersRequestModel;
import com.ys.photoapp.api.users.ui.model.UserResponseModel;
import com.ys.photoapp.api.users.ui.service.UsersServiceImpl;

@RestController
@RequestMapping("/users")
public class UsersController {

	@Autowired
	private Environment env;

	@Autowired
	UsersServiceImpl userService;

	@GetMapping("/status/check")
	public String status() {
		return "Working on :- " + env.getProperty("local.server.port") + "\n" +
		// "token = "+env.getProperty("token.secret")+
				"testprov value :- " + env.getProperty("test.config.prop");
	}

	@PostMapping(consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }, produces = {
			MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<CreateUserResponseModel> createUsers(
			@RequestBody @Valid CreateUsersRequestModel userDetails) {

		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		UsersDto userDto = modelMapper.map(userDetails, UsersDto.class);

		UsersDto createdUser = userService.createUser(userDto);

		CreateUserResponseModel returnValue = modelMapper.map(createdUser, CreateUserResponseModel.class);

		return ResponseEntity.status(HttpStatus.CREATED).body(returnValue);
	}

	@GetMapping
	public List<UsersDto> getAllUsers() {
		return userService.getAllUsers();
	}

	@GetMapping(value = "/{userId}",
			produces = { MediaType.APPLICATION_XML_VALUE,
MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<UserResponseModel> getUser(@PathVariable("userId") String userId) {

		UsersDto userDto = userService.getUserByUserId(userId);
		UserResponseModel returnValue = new ModelMapper().map(userDto, UserResponseModel.class);

		return ResponseEntity.status(HttpStatus.OK).body(returnValue);
	}

}
