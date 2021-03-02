package com.ys.photoapp.api.users.data;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.ys.photoapp.api.users.ui.model.AlbumResponseModel;

import feign.FeignException;
import feign.hystrix.FallbackFactory;

@FeignClient(name = "albums-ws", fallbackFactory = AlbumsFallbackFactory.class)
public interface AlbumsServiceClient {
	@GetMapping("/users/{id}/albums")
	public List<AlbumResponseModel> getAlbums(@PathVariable String id);
}

@Component
class AlbumsFallbackFactory implements FallbackFactory<AlbumsServiceClient> {
//anushka_j3138
	@Override
	public AlbumsServiceClient create(Throwable cause) {
		// TODO Auto-generated method stub
		return new ALbumsServiceClientFallback(cause);
	}

}

class ALbumsServiceClientFallback implements AlbumsServiceClient {
	private final Throwable cause;

	Logger logger = LoggerFactory.getLogger(ALbumsServiceClientFallback.class);

	public ALbumsServiceClientFallback(Throwable cause) {
		super();
		this.cause = cause;
	}

	@Override
	public List<AlbumResponseModel> getAlbums(String id) {

		if (cause instanceof FeignException && ((FeignException) cause).status() == 404) {
			logger.error("404 error took place when getAlbums was called by the feing client with user id :" + id
					+ ". Error message :" + cause.getLocalizedMessage());
		} else {
			logger.error(
					"Some error ocurred in the feign client while calling getAlbums Method " + cause.getLocalizedMessage().toString());
		}

		return new ArrayList<>();
	}

}