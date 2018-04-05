package com.compellingcode.utils.parser.form.multipart.file;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MemoryFileContainer implements FileContainer {
	
	private byte[] data = null;
	
	private ByteArrayOutputStream outputStream = null;
	private ByteArrayInputStream inputStream = null;

	public OutputStream openOutputStream() {
		outputStream = new ByteArrayOutputStream();
		return outputStream;
	}

	public void closeOutputStream() throws IOException {
		if(outputStream != null) {
			data = outputStream.toByteArray();
			outputStream.close();
			outputStream = null;
		}
	}

	public InputStream openInputStream() throws FileNotFoundException {
		if(data == null)
			throw new FileNotFoundException("Data not yet loaded");
		
		inputStream = new ByteArrayInputStream(data);
		return inputStream;
	}

	public void closeInputStream() throws IOException {
		if(inputStream != null) {
			inputStream.close();
			inputStream = null;
		}
	}

}
