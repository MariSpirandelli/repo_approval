package com.main;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;

import com.entity.FileSettings;

public class AuthorsApprovalFileTreeTest extends AuthorsTest{
	
	@Test
	public void FileTreeSuccesstest() {
			
		FileSettings file = filesToApprove.stream()
				.filter(f -> f.getName().equals("testscomclientfollowFollowTest.java"))
				.findFirst()
				.orElse(null);		
		assertNotNull(file);
		
		Set<String> owners = file.getOwners();		
		assertTrue(owners.toString().equals("[ghopper, alovelace]"));
		
		Set<String> impactedFiles = file.getImpactedFiles()
				.stream().map(f -> f.getName()).collect(Collectors.toSet());
		
		assertTrue(impactedFiles.toString().equals("[testscomclienttweetTweetTest.java, testscomclientmessageMessageTest.java]"));
	}
	
	@Test
	public void OwnersFileTreeFailtest() {
		FileSettings file = filesToApprove.stream()
				.filter(f -> f.getName().equals("testscomclientfollowFollowTest.java"))
				.findFirst()
				.orElse(null);		
		assertNotNull(file);
		
		Set<String> owners = file.getOwners();		
		assertFalse(owners.toString().equals("[ghopper, alovelace, mfox]"));		
	}
	
	@Test
	public void ImpactedFilesFileTreeFailtest() {
		FileSettings file = filesToApprove.stream()
				.filter(f -> f.getName().equals("testscomclientfollowFollowTest.java"))
				.findFirst()
				.orElse(null);		
		assertNotNull(file);
		
		Set<String> impactedFiles = file.getImpactedFiles()
				.stream().map(f -> f.getName()).collect(Collectors.toSet());
		
		assertFalse(impactedFiles.toString().equals("[testscomclienttweetTweetTest.java]"));		
	}
}
