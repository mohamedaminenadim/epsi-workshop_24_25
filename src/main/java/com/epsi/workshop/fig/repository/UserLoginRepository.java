package com.epsi.workshop.fig.repository;

import com.epsi.workshop.fig.entity.UserEntity;
import com.epsi.workshop.fig.entity.UserLoginEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserLoginRepository extends JpaRepository<UserLoginEntity, Long> {
    Optional<UserLoginEntity> findByUser(UserEntity user);
}
