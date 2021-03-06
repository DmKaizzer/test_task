package test.task.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import test.task.entity.AppUser;

@Repository
public interface UserRepository extends JpaRepository<AppUser, String>{
    AppUser findByLogin(String login);
}
