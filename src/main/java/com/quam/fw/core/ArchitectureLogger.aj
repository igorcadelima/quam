package com.quam.fw.core;

import java.io.File;
import java.lang.Class;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;

import com.quam.fw.element.Element;
import com.quam.fw.element.ElementFactory;
import com.quam.fw.logger.LogHelper;

public final aspect ArchitectureLogger {

	/**
	 * Picks the main method
	 */
	pointcut mainMethod(): 
		execution(public static void main(String[]));

	before() : mainMethod(){
		scanDirs(thisJoinPointStaticPart.getClass().getClassLoader());
	}

	/**
	 * @param classLoader
	 *            Class loader used to load the application.
	 */
	private void scanDirs(final ClassLoader classLoader) {
		if (!(classLoader instanceof URLClassLoader))
			return;

		URL[] rootUrls = ((URLClassLoader) classLoader).getURLs();

		File location;
		for (URL url : rootUrls) {

			try {
				location = new File(url.toURI());
			} catch (URISyntaxException e) {
				// TODO
				// LOGGER.error(e.toString());
				return;
			}

			if (location.isDirectory()) {

				location = new File(location.getPath().replace("bin", "src"));

				// Get the classes from the passed folder and its sub folders
				getClasses(null, location);
			}
		}
	}

	/**
	 * Get the classes from the passed folder and its sub folders. If parent is
	 * null, then the passed folder is a child of the root.
	 * 
	 * @param parent
	 *            Parent folder
	 * @param location
	 *            Directory
	 */
	private void getClasses(String parent, File location) {
		File[] files = location.listFiles();
		String className, path;
		StringBuilder builder;

		for (File file : files) {
			builder = new StringBuilder();
			builder.append(parent).append(".").append(file.getName());

			path = (parent == null ? file.getName() : builder.toString());

			if (file.isDirectory()) {
				getClasses(path, file);
			} else if (file.getName().endsWith(".java")) {
				className = path.replace(".java", "");

				try {
					Class<? extends Class> c = (Class<? extends Class>) Class
							.forName(className);

					Element qElement = ElementFactory.newElement(c);
 
					LogHelper.log(LogHelper.ARCHITECTURE, qElement);

				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
