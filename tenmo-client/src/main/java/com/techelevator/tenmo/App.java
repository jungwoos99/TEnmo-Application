package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;
import com.techelevator.tenmo.services.TransferService;

import java.math.BigDecimal;


public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private final TransferService transferService = new TransferService(API_BASE_URL);
    private final AccountService accountService = new AccountService(API_BASE_URL);

    private AuthenticatedUser currentUser;

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }
    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

	private void viewCurrentBalance() {
		// TODO Auto-generated method stub
        consoleService.showBalance("Your current balance is: $",
                accountService.getAccountBalance(currentUser));
	}

	private void viewTransferHistory() {
		// TODO Auto-generated method stub
        consoleService.viewPastTransfers(transferService.getTransfers(currentUser), Math.toIntExact(currentUser.getUser().getId()), transferService.getListOfUsers(currentUser), accountService.getAccountHolders(currentUser));
        int transferId = consoleService.promptForIdInt("\nPlease select a transfer Id or enter 0 to return: ");
        if(transferId == 0) {
            mainMenu();
        } else {
            consoleService.viewTransfer(transferService.getTransfers(currentUser), transferId, transferService.getListOfUsers(currentUser), accountService.getAccountHolders(currentUser));
        }
	}

	private void viewPendingRequests() {
		// TODO Auto-generated method stub
        System.out.println("Feature coming soon on a phone near you.");
	}

	private void sendBucks() {
		// TODO Auto-generated method stub
        consoleService.showUsersAndUserIds(transferService.getListOfUsers(currentUser));
        int accountTo = consoleService.promptForIdInt("Please select a user ID: ");
        BigDecimal amount = consoleService.promptForBigDecimal("Please enter a dollar amount: $");
        Transfer transfer = (transferService.send(transferService.makeTransferDTO(accountTo, amount), currentUser));
        consoleService.viewTransfer(transferService.getTransfers(currentUser), transfer.getTransferId(), transferService.getListOfUsers(currentUser), accountService.getAccountHolders(currentUser));
    }

	private void requestBucks() {
		// TODO Auto-generated method stub
        System.out.println("Feature coming soon on a phone near you.");
	}

}
