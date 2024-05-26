package com.example.capstone.Repository;

import com.example.capstone.Entity.MLEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MLRepository extends JpaRepository<MLEntity, Long>
{
}

