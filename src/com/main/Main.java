package com.main;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		AuthorsApproval authorsApproval = new AuthorsApproval("./src");
		
		Scanner scanner = new Scanner(System.in);
		System.out.print("$ ");
		String command = scanner.next();
		while(!command.equals("exit")){
			if(command.equals("validate_approvals")){
				String owners_command = scanner.next();
				String owners = scanner.next();
				String changedFiles_command = scanner.next();
				String changedFiles = scanner.next();
				
				if(owners_command.equals("--approvers") && changedFiles_command.equals("--changed-files")){
					List<String> ownersList = Arrays.asList(owners.split(","));
					List<String> changedFilesList = Arrays.asList(changedFiles.split(","));
					
					boolean approved = authorsApproval.checkApproval(ownersList, changedFilesList);
					
					if(approved){
						System.out.println("Approved");
					}else{
						System.out.println("Insufficient approvals");
					}
					System.out.print("$ ");
					command = scanner.next();
				}else{
					System.out.println("Uknown command.");
					break;
				}				
			}
		}
		System.out.println("Your validations are done!");
		scanner.close();
	}

}
