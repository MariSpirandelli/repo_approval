package com.business;

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

public class FileTreeLoader {
	
	private String sourceFolder;
	
	public FileTreeLoader(String sourceFolder){
		this.sourceFolder = sourceFolder;
	}
	
	/**
	 * Triggers the build of FileSetting with their owners and dependencies
	 */
	public Set<FileSettings> loadFiles(){
		Set<FileSettings> filesToApprove = new HashSet<>();
		
		File file = new File("./");
		
		if(file.exists()){
			Set<String> owners = new HashSet<>();
			readFileContentAsList(file, owners, "OWNERS");
			file = new File(this.sourceFolder);
			buildFileTree(filesToApprove, file, owners);
		}
		
		return filesToApprove;
	}
	
	/**
	 * Reads directories and files building in memory files owners and dependencies
	 * @param files
	 * @param file
	 * @param owners
	 */
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
				createOrUpdateFileSetting(files, file, parentOwners);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Reads all file contents as a Set of data
	 * @param file
	 * @param list
	 * @param fileName
	 */
	private void readFileContentAsList(File file, Set<String> list, String fileName){
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
	
	/**
	 * Creates or Updates a FileSetting with it's name, owners and the impacted files when it has any changes 
	 * @param files
	 * @param file
	 * @param owners
	 */
	private void createOrUpdateFileSetting(Set<FileSettings> files, File file, Set<String> owners){
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
			readFileContentAsList(file.getParentFile(), _owners, "OWNERS");
			_owners.addAll(owners);
			
			Set<String> _dependencies = new HashSet<>();
			readFileContentAsList(file.getParentFile(), _dependencies, "DEPENDENCIES");
			_dependencies = _dependencies.stream().map(dep -> dep.replace("/", "")).collect(Collectors.toSet());
			
			FileSettings file_setting = new FileSettings(fileName.substring(1), _owners, new HashSet<FileSettings>());
			files.add(file_setting);
			getImpactedFiles(file_setting, files, _dependencies);			
			
		}
	}
	/**
	 * Builds the relation between dependencies and FileSettings impacted files. 
	 * It makes it easier to find those files which needs to have it's approval checked when there are changes on the file.  
	 * @param current
	 * @param files
	 * @param dependencies
	 */
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
}
