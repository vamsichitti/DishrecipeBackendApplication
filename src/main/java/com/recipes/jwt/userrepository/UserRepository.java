package com.recipes.jwt.userrepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.recipes.jwt.userentity.DAOUser;


@Repository
public interface UserRepository extends JpaRepository<DAOUser, Long> {
	DAOUser findByUsername(String username);

}