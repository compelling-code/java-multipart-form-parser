package com.compellingcode.utils.parser.form.multipart.domain;

public class FormElement {
	
	private boolean isFile;
	private String name;
	private String fileName;
	private String tempFile;
	private String mimeType;
	private String value;
	
	public FormElement(String name, String fileName, String tempFile, String mimeType) {
		isFile = true;
		this.name = name;
		this.fileName = fileName;
		this.tempFile = tempFile;
		this.mimeType = mimeType;
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

	public String getTempFile() {
		return tempFile;
	}

	public void setTempFile(String tempFile) {
		this.tempFile = tempFile;
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

}
