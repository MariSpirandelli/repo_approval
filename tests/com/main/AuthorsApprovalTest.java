package com.main;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class AuthorsApprovalTest extends AuthorsTest{	
	

	@Test
	public void ApprovalSuccessTest() {
		List<String> owners = Arrays.asList("alovelace,ghopper".split(","));
		List<String> changedFiles = Arrays.asList("tests/com/client/follow/FollowTest.java,tests/com/client/user/UserTest.java".split(","));
		
		assertTrue(authorsApproval.checkApproval(owners, changedFiles));		
	}
	
	@Test
	public void ApprovalFailTestWrongOwner() {
		List<String> owners = Arrays.asList("alovelace".split(","));
		List<String> changedFiles = Arrays.asList("tests/com/client/user/UserTest.java".split(","));
		
		assertFalse(authorsApproval.checkApproval(owners, changedFiles));		
	}
	
	@Test
	/**
	 * it is missing Message.java owner
	 */
	public void ApprovalFailTestMissingDependencieOwner() {
		List<String> owners = Arrays.asList("alovelace".split(","));
		List<String> changedFiles = Arrays.asList("tests/com/client/follow/FollowTest.java".split(","));
		
		assertFalse(authorsApproval.checkApproval(owners, changedFiles));		
	}

}
