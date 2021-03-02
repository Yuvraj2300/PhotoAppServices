package com.ys.photoapp.api.users.data;

import org.springframework.data.repository.CrudRepository;

public interface UserRepo extends CrudRepository<UserEntity, Long> {
	UserEntity findByEmail(String Email);
	UserEntity findByUserId(String userId);
}
