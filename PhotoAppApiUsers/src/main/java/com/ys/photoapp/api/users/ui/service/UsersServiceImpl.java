package com.ys.photoapp.api.users.ui.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ys.photoapp.api.users.data.AlbumsServiceClient;
import com.ys.photoapp.api.users.data.UserEntity;
import com.ys.photoapp.api.users.data.UserRepo;
import com.ys.photoapp.api.users.shared.UsersDto;
import com.ys.photoapp.api.users.ui.model.AlbumResponseModel;

import feign.FeignException;

@Service
public class UsersServiceImpl implements UsersService {

	UserRepo userRepo;
	BCryptPasswordEncoder bCryptEncd;
//	RestTemplate	restTemplate;
	Environment	env;
	AlbumsServiceClient	albumsClient;

	Logger logger	=	LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	public UsersServiceImpl(UserRepo userRepo, 
			BCryptPasswordEncoder bCryptEncd, 
//			RestTemplate restTemplate,
			AlbumsServiceClient	albumsClient,
			Environment	 env) {
		super();
		this.userRepo = userRepo;
		this.bCryptEncd = bCryptEncd;
//		this.restTemplate	=	restTemplate;
		this.albumsClient=albumsClient;
		this.env=env;
	}

	@Override
	public UsersDto createUser(UsersDto userDetails) {
		// TODO Auto-generated method stub
		userDetails.setUserId(UUID.randomUUID().toString());
		userDetails.setEncryptedPassword(bCryptEncd.encode(userDetails.getPassword()));

		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		UserEntity userEntity = modelMapper.map(userDetails, UserEntity.class);

		userRepo.save(userEntity);

		UsersDto returnValue = modelMapper.map(userEntity, UsersDto.class);

		return returnValue;
	}

	public List<UsersDto> getAllUsers() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		List<UsersDto> returnValues = modelMapper.map(userRepo.findAll(), new TypeToken<List<UsersDto>>() {
		}.getType());
		return returnValues;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		UserEntity userEntity = userRepo.findByEmail(username);
		if (userEntity == null)
			throw new UsernameNotFoundException(username);

		return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), true, true, true, true,
				new ArrayList<>());
	}

	@Override
	public UsersDto getUserDetailsByEmail(String email) {
		// TODO Auto-generated method stub

		UserEntity userEntity = userRepo.findByEmail(email);
		if (userEntity == null)
			throw new UsernameNotFoundException(email);

		return new ModelMapper().map(userEntity, UsersDto.class);
	}

	@Override
	public UsersDto getUserByUserId(String userId) {
		UserEntity userEntity = userRepo.findByUserId(userId);
		if(userEntity==null) {
			throw new UsernameNotFoundException("User Not Found");
		}
		
		UsersDto userDto	=	new ModelMapper().map(userEntity,UsersDto.class);
//		
//		String albumsUrl	=	String.format(env.getProperty("albums.url"),userId);
//		
//		ResponseEntity<List<AlbumResponseModel>> albumsListResponse = 
//				restTemplate.exchange(albumsUrl, 
//						org.springframework.http.HttpMethod.GET,
//						null, 
//						new ParameterizedTypeReference<List<AlbumResponseModel>>() {});
		logger.info("Before calling the albums microservice");	
		List<AlbumResponseModel>	albumsList	=	albumsClient.getAlbums(userId);
		logger.info("After calling the albums microservice");	
//		
//		try {
//			albumsClient.getAlbums(userId);
//		} catch (FeignException e) {
//			// TODO Auto-generated catch block
//			logger.error(e.getLocalizedMessage());
//		}
		
		userDto.setAlbums(albumsList);
		
		return userDto;
	}

}
