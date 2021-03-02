package com.ys.photoapp.api.users.shared;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import feign.Response;
import feign.codec.ErrorDecoder;

@Component
public class FeignErrorDecoder implements ErrorDecoder {

	Environment env;

	@Autowired
	public FeignErrorDecoder(Environment env) {
		super();
		this.env = env;
	}

	@Override
	public Exception decode(String methodKey, Response response) {
		// TODO Auto-generated method stub
		switch (response.status()) {
		case 400:
			break;
		case 404: {
			if (methodKey.contains("getAlbums")) {
				return new ResponseStatusException(HttpStatus.valueOf(response.status()),
						env.getProperty("albums.exception.albums-albums-not-found"));
			}

			break;
		}
		default:
			return new Exception(response.reason());

		}
		return null;
	}

}
