package com.compellingcode.utils.parser.form.multipart;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.compellingcode.utils.parser.form.multipart.domain.FormElement;
import com.compellingcode.utils.parser.form.multipart.exception.InvalidMultipartDataException;
import com.compellingcode.utils.parser.form.multipart.exception.UnknownFileContainerTypeException;
import com.compellingcode.utils.parser.form.multipart.file.FileContainer;
import com.compellingcode.utils.parser.form.multipart.file.FileContainerFactory;
import com.compellingcode.utils.parser.form.multipart.file.FileContainerType;

public class MultipartFormParser {
	private byte[] boundary;
	
	private static final byte[] twoNewLines = "\r\n\r\n".getBytes();
	private static final byte[] newline = "\r\n".getBytes();
	private static final byte[] eos = "--".getBytes();
	
	private FileContainerFactory fileFactory;
	private FileContainerType fileType = FileContainerType.TEMPFILE;
	
	public MultipartFormParser(byte[] boundary) {
		this.boundary = boundary;
		fileFactory = new FileContainerFactory();
	}
	
	public MultipartFormParser(byte[] boundary, String tempDir) {
		this.boundary = boundary;
		fileFactory = new FileContainerFactory(tempDir);
	}
	
	public MultipartFormParser(String boundary) {
		this.boundary = boundary.getBytes();
		fileFactory = new FileContainerFactory();
	}
	
	public MultipartFormParser(String boundary, String tempDir) {
		this.boundary = boundary.getBytes();
		fileFactory = new FileContainerFactory(tempDir);
	}
	
	public Set<FormElement> parse(InputStream inputStream) throws InvalidMultipartDataException {
		PushbackInputStream pis = new PushbackInputStream(new BufferedInputStream(inputStream, 16 * 1024), 1024);
		Set<FormElement> elements = new HashSet<FormElement>();
		
		try {
			readBlock(pis, new NullOutputStream(), boundary);
			
			while(!isEnd(pis)) {
				Map<String, String> headers = getHeaders(pis);
				FormElement element;
				
				if(headers.containsKey("content-type")) {
					FileContainer fc = writeFile(pis);
					element = new FormElement(headers.get("name"), headers.get("filename"), headers.get("content-type"), fc);
				} else {
					String value = getValue(pis);
					element = new FormElement(headers.get("name"), value);
				}
				
				elements.add(element);
			}
		} catch(Exception ex) {
			throw new InvalidMultipartDataException(ex);
		}
		
		return elements;
	}
	
	private boolean isEnd(PushbackInputStream pis) throws InvalidMultipartDataException, IOException {
		byte[] eol = new byte[2];
		pis.read(eol);
		
		if(Arrays.equals(eol, eos))
			return true;
		
		if(!Arrays.equals(eol, newline))
			throw new InvalidMultipartDataException("Newline expected but not found");
		
		return false;
	}
	
	private void readBlock(PushbackInputStream pis, OutputStream os, byte[] delimiter) throws IOException, InvalidMultipartDataException {
		while(true) {
			int i = pis.read();
			if(i == -1)
				throw new InvalidMultipartDataException("Unexpected end of stream");
			
			byte b = (byte)i;
			if(b == delimiter[0]) {
				byte[] lookahead = new byte[delimiter.length];
				lookahead[0] = b;
				int count = pis.read(lookahead, 1, delimiter.length - 1);
				if(Arrays.equals(lookahead, delimiter)) {
					break;
				} else {
					pis.unread(lookahead, 1, count);
				}
				
				os.write(b);
			}
		}
	}
	
	private Map<String, String> getHeaders(PushbackInputStream pis) throws IOException, InvalidMultipartDataException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(64 * 1024);
		readBlock(pis, baos, twoNewLines);
		
		byte[] data = baos.toByteArray();
		baos.close();
		
		return processHeaders(new String(data, "UTF-8"));
	}
	
	private Map<String, String> processHeaders(String data) {
		Map<String, String> headers = new HashMap<String, String>();
		
		String[] lines = data.replace("\r", "").split("\n");
		for(String line : lines) {
			String[] header = line.trim().split(":");
			String key = header[0].trim().toLowerCase();
			if(header.length > 1) {
				String[] values = header[1].trim().split(";");
				headers.put(key,  values[0]);
				
				for(int i = 1; i < values.length; i++) {
					String[] parts = values[i].trim().split("=", 2);
					if(parts.length > 1) {
						headers.put(parts[0], parts[1]);
					} else {
						headers.put(parts[0], "");
					}
				}
			} else {
				headers.put(key, "");
			}
		}
		
		return headers;
	}
	
	private String getValue(PushbackInputStream pis) throws InvalidMultipartDataException, IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(64 * 1024);
		readBlock(pis, baos, boundary);
		
		byte[] data = baos.toByteArray();
		baos.close();
		
		return new String(data, "UTF-8");
	}
	
	private FileContainer writeFile(PushbackInputStream pis) throws IOException, InvalidMultipartDataException, UnknownFileContainerTypeException {
		FileContainer fc = fileFactory.getFileContainer(fileType);
		
		BufferedOutputStream bos = new BufferedOutputStream(fc.openOutputStream(), 64 * 1024);
		readBlock(pis, bos, boundary);
		fc.closeOutputStream();
		
		return fc;
	}

	public FileContainerType getFileType() {
		return fileType;
	}

	public void setFileType(FileContainerType fileType) {
		this.fileType = fileType;
	}

}
