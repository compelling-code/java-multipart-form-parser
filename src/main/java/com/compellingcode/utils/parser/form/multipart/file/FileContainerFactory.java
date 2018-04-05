package com.compellingcode.utils.parser.form.multipart.file;

import java.io.IOException;

import com.compellingcode.utils.parser.form.multipart.exception.UnknownFileContainerTypeException;

public class FileContainerFactory {

	private String tempDir;
	
	public FileContainerFactory() {
		this.tempDir = "/tmp";
	}

	public FileContainerFactory(String tempDir) {
		this.tempDir = tempDir;
	}
	
	public FileContainer getFileContainer(FileContainerType type) throws UnknownFileContainerTypeException, IOException {
		if(type == FileContainerType.TEMPFILE)
			return new TemporaryFileContainer(tempDir);
		else if(type == FileContainerType.MEMORY)
			return new MemoryFileContainer();
		
		throw new UnknownFileContainerTypeException(type.toString());
	}

}
