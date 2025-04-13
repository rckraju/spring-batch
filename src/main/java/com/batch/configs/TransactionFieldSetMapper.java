package com.batch.configs;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

import com.batch.entities.Transaction;

public class TransactionFieldSetMapper implements FieldSetMapper<Transaction> {
	
	@Override
    public Transaction mapFieldSet(FieldSet fieldSet) {
        Transaction transaction = new Transaction();
        transaction.setAccountNumber(fieldSet.readString("accountNumber"));
        transaction.setTrxAmount(fieldSet.readBigDecimal("trxAmount"));
        transaction.setDescription(fieldSet.readString("description"));
        transaction.setTrxDate(LocalDate.parse(fieldSet.readString("trxDate")));
        transaction.setTrxTime(LocalTime.parse(fieldSet.readString("trxTime")));
        transaction.setCustomerId(fieldSet.readString("customerId"));
        return transaction;
    }
}
