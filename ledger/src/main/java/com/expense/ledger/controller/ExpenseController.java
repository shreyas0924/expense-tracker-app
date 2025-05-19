package com.expense.ledger.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import com.expense.ledger.DTO.ExpenseDto;
import com.expense.ledger.service.ExpenseService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@RestController
@RequestMapping("/expense/v1")
public class ExpenseController {

     @Autowired
     private final ExpenseService expenseService;

     @GetMapping(path = "/getExpense")
     public ResponseEntity<List<ExpenseDto>> getExpense(@RequestParam(value = "user_id") @NonNull String userId) {
          try {
               List<ExpenseDto> expenseDtoList = expenseService.getExpenses(userId);
               return new ResponseEntity<>(expenseDtoList, HttpStatus.OK);
          } catch (Exception ex) {
               return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
          }
     }

     @PostMapping(path = "/addExpense")
     public ResponseEntity<Boolean> addExpenses(@RequestHeader(value = "X-User-Id") @NonNull String userId,
               @RequestBody ExpenseDto expenseDto) {
          try {
               expenseDto.setUserId(userId);
               return new ResponseEntity<>(expenseService.createExpense(expenseDto), HttpStatus.OK);
          } catch (Exception ex) {
               return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
          }
     }

}