package com.example.capstone.Repository;

import com.example.capstone.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
//@NoRepositoryBean
public interface UserRepository extends JpaRepository<UserEntity, String>
{
        public boolean existsByEmailAndPassword(String email, String password);
}
