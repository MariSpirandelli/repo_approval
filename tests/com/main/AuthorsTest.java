package com.main;

import org.junit.BeforeClass;

public class AuthorsTest {
	
	protected static String SOURCE_FOLDER = "./tests";
	protected static AuthorsApproval authorsApproval;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		authorsApproval = new AuthorsApproval(SOURCE_FOLDER);
	}

}
