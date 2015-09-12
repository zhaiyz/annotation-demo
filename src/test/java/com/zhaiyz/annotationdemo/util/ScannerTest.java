package com.zhaiyz.annotationdemo.util;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.zhaiyz.annotationdemo.annotation.Table;
import com.zhaiyz.annotationdemo.bean.User;

public class ScannerTest {

	@Test
	public void testScan() {
		List<Class<?>> classes = Scanner.scan("com.zhaiyz.annotationdemo");
		Assert.assertTrue(classes.contains(User.class));
	}

	@Test
	public void testScanHasTableAnnotationClass() {
		List<Class<?>> classes = Scanner.scan("com.zhaiyz.annotationdemo");
		for (Class<?> cls : classes) {
			if (User.class.equals(cls)) {
				Assert.assertNotNull(cls.getAnnotation(Table.class));
			}
		}
	}
}
