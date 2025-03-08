package com.example.HomeSaveHome.BulletinBoard.service;

import com.example.HomeSaveHome.BulletinBoard.entity.Board;
import com.example.HomeSaveHome.BulletinBoard.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardService {
    @Autowired
    private BoardRepository boardRepository;

    public List<Board> getAllBoards() {
        return boardRepository.findAll();
    }

    public Board getBoardById(Long id) {
        return boardRepository.findById(id).orElse(null);
    }

    public Board createBoard(Board board) {
        return boardRepository.save(board);
    }

    public void deleteBoard(Long id) {
        boardRepository.deleteById(id);
    }
}