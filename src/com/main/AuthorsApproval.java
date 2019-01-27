package com.main;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.entity.FileSettings;

public class AuthorsApproval {
	
	private Set<FileSettings> filesToApprove;
	
	private String sourceFolder;
	
	public AuthorsApproval(String sourceFolder){
		this.sourceFolder = sourceFolder;
		loadFiles();
	}
	
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
	
	private void loadFiles(){
		filesToApprove = new HashSet<>();
		
		File file = new File("./");
		
		if(file.exists()){
			Set<String> owners = new HashSet<>();
			addList(file, owners, "OWNERS");
			file = new File(this.sourceFolder);
			buildFileTree(filesToApprove, file, owners);
		}		
	}
	
	private void buildFileTree(Set<FileSettings> files, File file, Set<String> owners){
		try {
			Set<String> parentOwners = new HashSet<>();
			if(file.isDirectory()){			
				List<Path> subFiles = Files.list(file.toPath()).collect(Collectors.toList());
				for(int i=0; i< subFiles.size(); i++){
					buildFileTree(files, subFiles.get(i).toFile(), owners);
				}
			}else if(file.getName().contains(".java")){
				parentOwners.addAll(owners);
				addFileSetting(files, file, parentOwners);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void addList(File file, Set<String> list, String fileName){
		File[] dataFile = file.listFiles((dir, name) -> name.equals(fileName));
		if(dataFile != null && dataFile.length > 0){	
			for(File owner: dataFile ){
				List<String> fileNames;
				try {
					fileNames = Files.readAllLines(Paths.get(owner.getPath()));
					list.addAll(fileNames);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}			
		}
	}
	
	private void addFileSetting(Set<FileSettings> files, File file, Set<String> owners){
		String fileName = file.getPath().toString().replace("\\", "");
		Optional<FileSettings> fileSetting = files.stream()
		.filter((f)-> {			
			return f.getName().contains(fileName) || fileName.contains(f.getName());
		})
		.findFirst();
		
		if(fileSetting.isPresent()){
			fileSetting.get().setName(fileName);
			fileSetting.get().getOwners().addAll(owners);
		}else{			
			Set<String> _owners = new HashSet<>();
			addList(file.getParentFile(), _owners, "OWNERS");
			_owners.addAll(owners);
			
			Set<String> _dependencies = new HashSet<>();
			addList(file.getParentFile(), _dependencies, "DEPENDENCIES");
			_dependencies = _dependencies.stream().map(dep -> dep.replace("/", "")).collect(Collectors.toSet());
			
			FileSettings file_setting = new FileSettings(fileName.substring(1), _owners, new HashSet<FileSettings>());
			files.add(file_setting);
			getImpactedFiles(file_setting, files, _dependencies);			
			
		}
	}
	
	private void getImpactedFiles(FileSettings current, Set<FileSettings> files, Set<String> dependencies){		
		dependencies.forEach(dep -> {
			if(!current.getName().contains(dep)){
				FileSettings impactedFile = files.stream()
					.filter(f -> 
						f.getName().contains(dep)
					)
					.findFirst()
					.orElse(null);
			
				if(impactedFile == null) {
					impactedFile = new FileSettings(dep, new HashSet<String>(), new HashSet<FileSettings>());
					files.add(impactedFile);
				}
				
				impactedFile.getImpactedFiles().add(current);
			}
		});
	}

	public Set<FileSettings> getFilesToApprove(){
		return this.filesToApprove;
	}
}
