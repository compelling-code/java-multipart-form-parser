package com.compellingcode.utils.parser.form.multipart.file;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface FileContainer {
	public OutputStream openOutputStream() throws FileNotFoundException;
	public void closeOutputStream() throws IOException;
	
	public InputStream openInputStream() throws FileNotFoundException;
	public void closeInputStream() throws IOException;
}
