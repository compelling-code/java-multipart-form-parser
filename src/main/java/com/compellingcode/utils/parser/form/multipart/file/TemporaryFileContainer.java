package com.compellingcode.utils.parser.form.multipart.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class TemporaryFileContainer implements FileContainer {
	
	private static final String tempPrefix = "MFP-";
	private static final String tempSuffix = ".tmp";
	
	private File tempFile;
	
	private OutputStream outputStream = null;
	private InputStream inputStream = null;
	
	public TemporaryFileContainer(String tempDir) throws IOException {
		this.tempFile = File.createTempFile(tempPrefix, tempSuffix, new File(tempDir));
	}

	public OutputStream openOutputStream() throws FileNotFoundException {
		outputStream = new BufferedOutputStream(new FileOutputStream(tempFile), 64 * 1024);
		return new BufferedOutputStream(outputStream, 64 * 1024);
	}

	public void closeOutputStream() throws IOException {
		if(outputStream != null) {
			outputStream.close();
			outputStream = null;
		}
		
	}

	public InputStream openInputStream() throws FileNotFoundException {
		inputStream = new BufferedInputStream(new FileInputStream(tempFile), 64 * 1024);
		return inputStream;
	}
	
	public void closeInputStream() throws IOException {
		if(inputStream != null) {
			inputStream.close();
			inputStream = null;
		}
	}

}
