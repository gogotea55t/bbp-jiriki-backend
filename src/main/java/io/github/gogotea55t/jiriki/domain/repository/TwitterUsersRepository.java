package io.github.gogotea55t.jiriki.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.github.gogotea55t.jiriki.domain.entity.TwitterUsers;

@Repository
public interface TwitterUsersRepository extends JpaRepository<TwitterUsers, String> {}
