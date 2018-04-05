package com.compellingcode.utils.parser.form.multipart.domain;

import com.compellingcode.utils.parser.form.multipart.file.FileContainer;

public class FormElement {
	
	private boolean isFile;
	private String name;
	private String fileName;
	private String mimeType;
	private String value;
	private FileContainer file;
	
	public FormElement(String name, String fileName, String mimeType, FileContainer file) {
		isFile = true;
		this.name = name;
		this.fileName = fileName;
		this.mimeType = mimeType;
		this.file = file;
	}
	
	public FormElement(String name, String value) {
		isFile = false;
		this.name = name;
		this.value = value;
	}

	public boolean isFile() {
		return isFile;
	}

	public void setFile(boolean isFile) {
		this.isFile = isFile;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public FileContainer getFile() {
		return file;
	}

	public void setFile(FileContainer file) {
		this.file = file;
	}

}
