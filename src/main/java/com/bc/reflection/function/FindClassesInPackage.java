package com.bc.reflection.function;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

public class FindClassesInPackage{
    
    public List<Class> apply(String packageName) {
        try{
            return Arrays.asList(this.getClasses(packageName));
        }catch(IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets all classes within a given package. 
     * <p><b>Note</b> that it should only work for classes found locally, 
     * getting really ALL classes is impossible.
     * </p>
     * Scans all classes accessible from the context class loader which belong 
     * to the given package and sub-packages.
     *
     * @param packageName The base package
     * @return The classes
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public Class[] getClasses(String packageName)
            throws ClassNotFoundException, IOException {

        final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        assert classLoader != null;

        final String path = packageName.replace('.', '/');

        final Enumeration<URL> resources = classLoader.getResources(path);

        final List<File> dirs = new ArrayList<>();

        while (resources.hasMoreElements()) {

            final URL resource = resources.nextElement();

            dirs.add(new File(resource.getFile()));
        }

        final List<Class> classes = new ArrayList<>();

        for (File directory : dirs) {

            classes.addAll(findClasses(directory, packageName));
        }

        return classes.toArray(new Class[classes.size()]);
    }

    /**
     * Recursive method used to find all classes in a given directory and sub directories.
     *
     * @param directory   The base directory
     * @param packageName The package name for classes found inside the base directory
     * @return The classes
     * @throws ClassNotFoundException
     */

    private List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {

        final List<Class> classes = new ArrayList<>();

        if (!directory.exists()) {
            return classes;
        }

        final File[] files = directory.listFiles();

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
