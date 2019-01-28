package com.main;

import java.util.Set;

import org.junit.BeforeClass;

import com.business.AuthorsApproval;
import com.business.FileTreeLoader;
import com.entity.FileSettings;

public class AuthorsTest {
	
	protected static String SOURCE_FOLDER = "./tests";
	protected static FileTreeLoader fileTreeLoader;
	protected static Set<FileSettings> filesToApprove;
	protected static AuthorsApproval authorsApproval;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		fileTreeLoader = new FileTreeLoader(SOURCE_FOLDER);
		filesToApprove = fileTreeLoader.loadFiles();
		authorsApproval = new AuthorsApproval(filesToApprove);
	}

}
