package com.business;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.entity.FileSettings;

public class AuthorsApproval {
	
	private Set<FileSettings> filesToApprove;
	
	public AuthorsApproval(Set<FileSettings> filesToApprove){
		this.filesToApprove = filesToApprove;
	}
	
	/**
	 * Triggers the files approval checking
	 * @param owners
	 * @param changedFiles
	 * @return boolean meaning if it was approved or not
	 */
	public boolean checkApproval(List<String> owners, List<String> changedFiles){		
		boolean approved = true;		
		for(String changedFile: changedFiles){
			Optional<FileSettings> fileSetting = filesToApprove.stream()
					.filter(file -> 
						file.getName().contains(changedFile.replace("/", ""))
					)
					.findFirst();
			if(fileSetting.isPresent()){
				approved = isApproved(owners, fileSetting.get());
				if(!approved){
					break;
				}
			}else{
				approved = false;
				break;
			}
		}
		return approved;
	}
	
	/**
	 * Checks if the informed file is approved as well as the files which depends on it recursively. 
	 * @param owners
	 * @param fileSetting
	 * @return boolean meaning if it was approved or not
	 */
	private boolean isApproved(List<String> owners, FileSettings fileSetting){
		boolean approved = false;
		
		for(String owner: owners){
			if(fileSetting.getOwners().contains(owner)){
				approved = true;
				break;
			}
		}
			
		if(approved && !fileSetting.getImpactedFiles().isEmpty()){
			for(FileSettings impactedFile: fileSetting.getImpactedFiles()){
				approved = isApproved(owners, impactedFile);
				if(!approved){
					break;
				}
			}
		}		
		return approved;
	}
	
}
