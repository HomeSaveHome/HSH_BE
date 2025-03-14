package com.example.HomeSaveHome.BulletinBoard.controller;

import com.example.HomeSaveHome.BulletinBoard.entity.Board;
import com.example.HomeSaveHome.BulletinBoard.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class BoardController {

    private final BoardRepository boardRepository;

    @Autowired
    public BoardController(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }



    @GetMapping("/boards")
    public String showBoards(Model model) {
        List<Board> boards = boardRepository.findAll();
        model.addAttribute("boards", boards);
        return "boards.html";
    }


}
