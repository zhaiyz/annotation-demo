package com.zhaiyz.annotationdemo.util;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 类扫描器
 */
public class Scanner {

	/** 类加载器 */
	private static ClassLoader loader = Scanner.class.getClassLoader();

	/**
	 * 扫描类
	 * 
	 * @param packname
	 *            包名
	 * @return 类
	 */
	public static List<Class<?>> scan(String packname) {

		packname = packname.replace('.', '/');

		Enumeration<URL> urls;
		try {
			/* 获取资源列表 */
			urls = loader.getResources(packname);

		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		List<Class<?>> classList = new LinkedList<Class<?>>();
		/* 遍历 */
		while (urls.hasMoreElements()) {

			URL url = urls.nextElement();
			String protocol = url.getProtocol();

			if ("jar".equals(protocol)) {
				/* 如果是jar包内的资源 */
				classList.addAll(scanFromJar(url, packname));
			} else if ("file".equals(protocol)) {
				/* 如果是文件资源 */
				classList.addAll(scanFromDir(url, packname));
			}
		}

		return classList;
	}

	/**
	 * 获取Jar包类资源
	 * 
	 * @param url
	 *            资源URL
	 * @param pack
	 *            包名
	 * @return 类
	 */
	private static List<Class<?>> scanFromJar(URL url, String pack) {

		JarFile jar;
		try {
			/* 获取Jar包文件对象 */
			JarURLConnection conn = (JarURLConnection) url.openConnection();
			jar = conn.getJarFile();

		} catch (IOException e) {
			return new LinkedList<Class<?>>();
		}

		return scanFromJar(jar, pack);
	}

	/**
	 * 获取Jar包类资源
	 * 
	 * @param jar
	 *            JarFile对象
	 * @param pack
	 *            包名
	 * @return 类
	 */
	public static List<Class<?>> scanFromJar(JarFile jar, String pack) {

		List<Class<?>> classList = new LinkedList<Class<?>>();

		/* 这里取到的是Jar包里所有的文件，需要过滤 */
		Enumeration<JarEntry> entrys = jar.entries();
		/* 遍历 */
		while (entrys.hasMoreElements()) {

			String name = entrys.nextElement().getName();

			if (name.startsWith(pack) && name.endsWith(".class")) {

				String className = name.substring(0, name.length() - 6);
				className = className.replace('/', '.');

				try {
					/* 加载类对象 */
					Class<?> c = loader.loadClass(className);
					classList.add(c);

				} catch (ClassNotFoundException e) {
				}
			}
		}
		return classList;
	}

	/**
	 * 获取文件夹类资源
	 * 
	 * @param url
	 *            资源URL
	 * @param pack
	 *            包名
	 * @return 类
	 */
	private static List<Class<?>> scanFromDir(URL url, String pack) {

		File dir;
		try {
			/* 转行为file对象 */
			dir = new File(url.toURI());
		} catch (URISyntaxException e) {
			return new LinkedList<Class<?>>();
		}

		return scanFromDir(dir, pack);
	}

	/**
	 * 获取文件夹类资源
	 * 
	 * @param dir
	 *            文件夹对象
	 * @param pack
	 *            包名
	 * @return 类
	 */
	public static List<Class<?>> scanFromDir(File dir, String pack) {

		List<Class<?>> classList = new LinkedList<Class<?>>();

		File[] files = dir.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				/* 递归 */
				classList.addAll(scanFromDir(file, pack + '/' + file.getName()));

			} else if (file.getName().endsWith(".class")) {

				String fileName = file.getName();
				String className = pack + '/' + fileName.substring(0, fileName.length() - 6);
				className = className.replace('/', '.');

				try {
					/* 加载类对象 */
					Class<?> c = loader.loadClass(className);
					classList.add(c);

				} catch (ClassNotFoundException e) {
				}
			}
		}
		return classList;
	}
}