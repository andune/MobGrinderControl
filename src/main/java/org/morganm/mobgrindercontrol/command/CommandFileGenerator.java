/**
 * 
 */
package org.morganm.mobgrindercontrol.command;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/** Class used at compile time to generate a file that automatically puts any
 * Command objects into it for use later by runtime.
 * 
 * @author morganm
 *
 */
@SuppressWarnings("rawtypes")
public class CommandFileGenerator
{
	public static final String packageName = "org.morganm.mobgrindercontrol.commands";
	
	public static void main(String[] args) throws Exception {
		String pkg = packageName;
		if( args.length < 1 ) {
			System.out.println("Missing required argument to output file");
			return;
		}
		
		File file = new File(args[0]);
		FileWriter writer = new FileWriter(file);
		
		Class[] classes = getClasses(pkg);
		if( classes != null )
			for(Class clazz : classes) {
//				System.out.println("Found class: "+clazz);
				writer.write(clazz.getName() + "\n");
				
				/*
				String className = clazz.getName();
				int index = className.lastIndexOf('.');
				String commandName = className.substring(index+1).toLowerCase();
				System.out.println("Command name = "+commandName);
				*/
			}
		
		writer.close();
	}
	
   /**
     * Scans all classes accessible from the context class loader which belong to the given package and subpackages.
     *
     * @param packageName The base package
     * @return The classes
     * @throws ClassNotFoundException
     * @throws IOException
     */
	private static Class[] getClasses(String packageName)
            throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<File>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList<Class> classes = new ArrayList<Class>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes.toArray(new Class[classes.size()]);
    }
	
	
    /**
     * Recursive method used to find all classes in a given directory and subdirs.
     *
     * @param directory   The base directory
     * @param packageName The package name for classes found inside the base directory
     * @return The classes
     * @throws ClassNotFoundException
     */
	private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
		List<Class> classes = new ArrayList<Class>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }
    

}
