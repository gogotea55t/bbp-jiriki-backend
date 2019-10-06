package io.github.gogotea55t.jiriki.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.github.gogotea55t.jiriki.domain.entity.Users;

@Repository
public interface UserRepository extends JpaRepository<Users, String> {
  public List<Users> findByUserNameLike(String userName);
  
  public Optional<Users> findByTwitterUsers_TwitterId(String twitterId);
}
