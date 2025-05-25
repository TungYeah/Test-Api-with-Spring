package vn.minhtung.decentralization.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.minhtung.decentralization.entity.Todo;

import java.util.Optional;

@Repository
public interface TodoRepository extends JpaRepository<Todo,Long > {
    Optional<Todo> findByUsername(String username);
}
