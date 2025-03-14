package com.example.HomeSaveHome.BulletinBoard.repository;

import com.example.HomeSaveHome.BulletinBoard.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {

}