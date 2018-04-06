package com.compellingcode.utils.parser.form.multipart;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.compellingcode.utils.parser.form.multipart.domain.FormElement;
import com.compellingcode.utils.parser.form.multipart.file.FileContainerType;

import junit.framework.TestCase;

public class MultipartFormParserTest extends TestCase {
	
	@Test
	public void testMultipartFormData() throws Exception {
		InputStream is = this.getClass().getResourceAsStream("/com/compellingcode/utils/parser/form/multipart/testdata.txt");
		if(is == null)
			throw new Exception("file not found");
		
		MultipartFormParser parser = new MultipartFormParser("-----------------------------9051914041544843365972754266");
		parser.setFileType(FileContainerType.MEMORY);
		List<FormElement> elements = parser.parse(is);
		
		is.close();
		
		assertTrue(elements.size() == 4);
		assertTrue(elements.get(0).isFile() == false);
		assertTrue(elements.get(1).isFile() == true);
		assertTrue(elements.get(2).isFile() == true);
		assertTrue(elements.get(3).isFile() == true);
		
		assertTrue(elements.get(0).getName().equals("text"));
		assertTrue(elements.get(1).getName().equals("file1"));
		assertTrue(elements.get(2).getName().equals("file2"));
		assertTrue(elements.get(3).getName().equals("file3"));
		
		assertTrue(elements.get(1).getFileName().equals("a.txt"));
		assertTrue(elements.get(2).getFileName().equals("a.html"));
		assertTrue(elements.get(3).getFileName().equals("test.txt"));
		
		is = elements.get(1).getFile().openInputStream();
		byte[] data = new byte[65535];
		int count = is.read(data);
		elements.get(1).getFile().closeInputStream();
		String value = new String(Arrays.copyOfRange(data, 0, count), "UTF-8");
		assertEquals("Content of a.txt.\r\n", value);
		
		is = elements.get(2).getFile().openInputStream();
		data = new byte[65535];
		count = is.read(data);
		elements.get(2).getFile().closeInputStream();
		value = new String(Arrays.copyOfRange(data, 0, count), "UTF-8");
		assertEquals("<!DOCTYPE html><title>Content of a.html.</title>\r\n", value);
		
		is = elements.get(3).getFile().openInputStream();
		data = new byte[65535];
		count = is.read(data);
		elements.get(3).getFile().closeInputStream();
		assertEquals(count, -1);
		
		assertEquals(elements.get(1).getMimeType(), "text/plain");
		assertEquals(elements.get(2).getMimeType(), "text/html");
		assertEquals(elements.get(3).getMimeType(), "text/plain");
	}
	
}
