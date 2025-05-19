package com.expense.ledger.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.expense.ledger.DTO.ExpenseDto;
import com.expense.ledger.LedgerApplication;
import com.expense.ledger.consumer.ExpenseConsumer;
import com.expense.ledger.entities.Expense;
import com.expense.ledger.repository.ExpenseRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@Service
@AllArgsConstructor
public class ExpenseService {

     @Autowired
     private final LedgerApplication ledgerApplication;

     @Autowired
     private final ExpenseConsumer expenseConsumer;

     @Autowired
     private final ExpenseRepository expenseRepository;

     @Autowired
     private final ObjectMapper objectMapper;

     // @Autowired
     // public ExpenseService(LedgerApplication ledgerApplication, ExpenseConsumer
     // expenseConsumer, ExpenseRepository expenseRepository, ObjectMapper
     // objectMapper) {
     // this.ledgerApplication = ledgerApplication;
     // this.expenseConsumer = expenseConsumer;
     // this.expenseRepository = expenseRepository;
     // this.objectMapper = objectMapper;
     // }
     public boolean createExpense(ExpenseDto expenseDto) {
          setCurrency(expenseDto);
          try {
               expenseRepository.save(objectMapper.convertValue(expenseDto, Expense.class));
               return true;
          } catch (Exception e) {
               return false;
          }
     }

     public boolean updateExpense(ExpenseDto expenseDto) {
          Optional<Expense> expense = expenseRepository.findByUserIdAndExternalId(expenseDto.getUserId(),
                    expenseDto.getExternalId());

          if (expense.isEmpty())
               return false;
          Expense expenseEntity = expense.get();
          expenseEntity.setAmount(
                    Strings.isNotBlank(expenseDto.getAmount()) ? expenseDto.getAmount() : expenseEntity.getAmount());
          expenseEntity.setMerchant(Strings.isNotBlank(expenseDto.getMerchant()) ? expenseDto.getMerchant()
                    : expenseEntity.getMerchant());
          expenseEntity.setCurrency(Strings.isNotBlank(expenseDto.getCurrency()) ? expenseDto.getCurrency()
                    : expenseEntity.getCurrency());
          expenseRepository.save(expenseEntity);
          return true;
     }

     public List<ExpenseDto> getExpenses(String userId) {
          List<Expense> expenses = expenseRepository.findByUserId(userId);
          return objectMapper.convertValue(expenses, new TypeReference<List<ExpenseDto>>() {
          });
     }

     private void setCurrency(ExpenseDto expenseDto) {
          if (Objects.isNull(expenseDto.getCurrency())) {
               expenseDto.setCurrency("INR");
          }
     }
}
