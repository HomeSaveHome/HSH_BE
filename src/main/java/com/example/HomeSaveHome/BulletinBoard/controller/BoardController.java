package com.example.HomeSaveHome.BulletinBoard.controller;

import com.example.HomeSaveHome.BulletinBoard.entity.Board;
import com.example.HomeSaveHome.BulletinBoard.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/boards")
public class BoardController {
    @Autowired
    private BoardService boardService;

    @GetMapping
    public List<Board> getAllBoards() {
        return boardService.getAllBoards();
    }

    @GetMapping("/{id}")
    public Board getBoardById(@PathVariable Long id) {
        return boardService.getBoardById(id);
        // what to do if i wanna display articles?
    }

    @PostMapping
    public Board createBoard(@RequestBody Board board) {
        return boardService.createBoard(board);
    }

    @DeleteMapping("/{id}")
    public void deleteBoard(@PathVariable Long id) {
        boardService.deleteBoard(id);
    }
}