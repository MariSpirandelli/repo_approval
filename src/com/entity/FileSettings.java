package com.entity;

import java.util.Set;

public class FileSettings {

	private String name;
	private Set<String> owners;
	private Set<FileSettings> impactedFiles;
	
	public FileSettings(String name, Set<String> owners, Set<FileSettings> impactedFiles){
		this.name = name;
		this.owners = owners;
		this.impactedFiles = impactedFiles;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Set<String> getOwners() {
		return owners;
	}
	public void setOwners(Set<String> owners) {
		this.owners = owners;
	}
	public Set<FileSettings> getImpactedFiles() {
		return impactedFiles;
	}
	public void setImpactedFiles(Set<FileSettings> impactedFiles) {
		this.impactedFiles = impactedFiles;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((impactedFiles == null) ? 0 : impactedFiles.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((owners == null) ? 0 : owners.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FileSettings other = (FileSettings) obj;
		if (impactedFiles == null) {
			if (other.impactedFiles != null)
				return false;
		} else if (!impactedFiles.equals(other.impactedFiles))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (owners == null) {
			if (other.owners != null)
				return false;
		} else if (!owners.equals(other.owners))
			return false;
		return true;
	}
}
