package com.example.tproject.rules;



import com.example.tproject.model.RuleResult;
import com.example.tproject.model.Transaction;
import com.example.tproject.model.UserProfile;

import java.util.List;

public interface FraudRule {

    RuleResult apply(UserProfile user,
                     Transaction tx,
                     List<Transaction> history);
}
