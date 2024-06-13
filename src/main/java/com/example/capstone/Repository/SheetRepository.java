package com.example.capstone.Repository;

import com.example.capstone.Entity.SheetEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SheetRepository extends JpaRepository<SheetEntity, Long>
{
}
