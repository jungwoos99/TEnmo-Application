package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.techelevator.tenmo.model.Account;

@Component
public class JdbcAccountDAO implements AccountDao {

    private JdbcTemplate jdbcTemplate;
    private UserDao userDao;

    public JdbcAccountDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public BigDecimal getAccountBalance (int id) {
        String sql = "SELECT balance FROM account WHERE user_id = ?";
        BigDecimal balance = jdbcTemplate.queryForObject(sql, BigDecimal.class, id);
        return balance;
    }

    @Override
    public Map<Integer, Integer> getAccountAndUserIds() {
        Map<Integer, Integer> accountIdAndUserId = new HashMap<>();
        String sql = "SELECT account_id, user_id, balance FROM account;";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql);
        while(result.next()) {
            Account account = mapRowToAccount(result);
            accountIdAndUserId.put(account.getAccountId(),account.getUserId());
        }
        return accountIdAndUserId;
    }

    @Override
    public int getAccountIdWithUserId(int userId) {
        String sql = "SELECT account_id FROM account WHERE user_id = ?;";
        return jdbcTemplate.queryForObject(sql, Integer.class, userId);
    }

    private Account mapRowToAccount(SqlRowSet result) {
        Account account = new Account();
        account.setBalance(result.getBigDecimal("balance"));
        account.setAccountId(result.getInt("account_id"));
        account.setUserId(result.getInt("user_id"));
        return account;
    }
}
