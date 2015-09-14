package com.zhaiyz.annotationdemo.util;

import java.lang.reflect.Field;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.zhaiyz.annotationdemo.annotation.Column;
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
				Table table = cls.getAnnotation(Table.class);
				Assert.assertTrue("user".equals(table.name()));
				Assert.assertNotNull(cls.getAnnotation(Table.class));
			}
		}
	}

	@Test
	public void testScanHasColumnAnnotationField() {
		List<Class<?>> classes = Scanner.scan("com.zhaiyz.annotationdemo");
		for (Class<?> cls : classes) {
			if (User.class.equals(cls)) {
				Field[] fields = cls.getDeclaredFields();
				for (Field field : fields) {
					Column column = field.getAnnotation(Column.class);
					if ("id".equals(field.getName())) {
						Assert.assertEquals(1, column.size());
					} else if ("name".equals(field.getName())) {
						Assert.assertEquals(-1, column.size());
					}
					Assert.assertTrue(field.isAnnotationPresent(Column.class));
				}
			}
		}
	}
}
