package test.task.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import test.task.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
