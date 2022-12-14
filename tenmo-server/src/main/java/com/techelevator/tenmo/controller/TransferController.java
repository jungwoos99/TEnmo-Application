package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.TransferDTO;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.exceptions.InsufficientFundsException;
import com.techelevator.tenmo.exceptions.InvalidTransactionAmountException;
import com.techelevator.tenmo.exceptions.InvalidTransferException;
import com.techelevator.tenmo.exceptions.TransferNotFoundException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@PreAuthorize("hasRole('ROLE_USER')")
@RequestMapping("/transfer")
public class TransferController {

    private TransferDao transferDao;
    private UserDao userDao;
    private AccountDao accountDao;

    public TransferController(TransferDao transferDao, UserDao userDao, AccountDao accountDao) {
        this.transferDao = transferDao;
        this.userDao = userDao;
        this.accountDao = accountDao;
    }

    @GetMapping(path = "/view/users")
    public Map<Integer,String> viewUsers() {
        return transferDao.getUsersAndUserIds();
    }

    @GetMapping(path = "/view/all")
    public List<Transfer> transferList(Principal principal) {
        return transferDao.viewTransfers(userDao.findIdByUsername(principal.getName()));
    }

    @GetMapping(path = "/view/{id}")
    public Transfer transfer(@PathVariable int id, Principal principal) throws TransferNotFoundException {
        return transferDao.viewTransferDetails(userDao.findIdByUsername(principal.getName()), id);
    }

    @PostMapping(path = "/send/{id}")
    public Transfer sendMoney(@PathVariable int id, @Valid @RequestBody TransferDTO transferDTO, Principal principal) throws InsufficientFundsException, InvalidTransferException, InvalidTransactionAmountException {
        return transferDao.sendTypeTransfer(transferDTO.getAmount(), userDao.findIdByUsername(principal.getName()), id);
    }

    @PostMapping(path = "/int")
    public String getInt(@RequestBody String string) {
        return string + 100;
    }

    @PreAuthorize("permitAll()")
    @GetMapping(path = "/test")
    public String getString(@RequestBody String string) {
        return string;
    }

}
