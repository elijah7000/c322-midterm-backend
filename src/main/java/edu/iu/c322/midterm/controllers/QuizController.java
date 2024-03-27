package edu.iu.c322.midterm.controllers;

import edu.iu.c322.midterm.model.Question;
import edu.iu.c322.midterm.model.Quiz;
import edu.iu.c322.midterm.repository.FileRepository;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/quizzes")
@CrossOrigin
public class QuizController {
    private FileRepository fileRepository;

    public QuizController(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @PostMapping
    public int add(@RequestBody Quiz quiz) {
        try {
            return fileRepository.addQuiz(quiz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @GetMapping
    public List<Quiz> findAll() {
        try {
            List<Quiz> quizzes = fileRepository.findAllQuiz();
            for (Quiz quiz : quizzes) {
                quiz.setQuestions(fileRepository.find(quiz.getQuestionIds()));
            }
            return quizzes;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    @GetMapping("/{id}")
    public Quiz get(@PathVariable int id) {
        try {
            Quiz quiz = fileRepository.getQuiz(id);
            if (quiz != null) {
                quiz.setQuestions(fileRepository.find(quiz.getQuestionIds()));
            }
            return quiz;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/{id}")
    @CrossOrigin
    public Quiz put(@PathVariable int id, @RequestBody Quiz updatedQuiz) {
        try {
            Quiz existingQuiz = fileRepository.getQuiz(id);
            if (existingQuiz == null) {
                Quiz newQuiz = new Quiz(updatedQuiz.getTitle(), updatedQuiz.getQuestionIds());
                newQuiz.setId(id);
                fileRepository.updateQuiz(newQuiz);
                newQuiz.setQuestions(fileRepository.find(newQuiz.getQuestionIds()));
                return newQuiz;
            }
            existingQuiz.setTitle(updatedQuiz.getTitle());
            existingQuiz.setQuestionIds(updatedQuiz.getQuestionIds());
            fileRepository.updateQuiz(existingQuiz);
            existingQuiz.setQuestions(fileRepository.find(existingQuiz.getQuestionIds()));
            return existingQuiz;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }







}
