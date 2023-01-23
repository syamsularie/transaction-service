package com.kezbek.transaction.service;

import com.kezbek.transaction.model.request.TransactionRequest;
import com.kezbek.transaction.model.response.UserTransactionResponse;

public interface TransactionService {
    UserTransactionResponse getCashback(TransactionRequest transactionRequest);
}
