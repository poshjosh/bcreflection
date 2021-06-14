package com.bc.reflection.function;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.function.Function;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author hp
 * @see https://stackoverflow.com/questions/520328/can-you-find-all-classes-in-a-package-using-reflection
 */
public class FindClassesInPackage implements Function<String, List<Class<?>>>{

    private static final Logger LOG = Logger.getLogger(FindClassesInPackage.class.getName());
    
    @Override
    public List<Class<?>> apply(String packageName) {
        try{
            return Collections.unmodifiableList(this.getClasses(packageName));
        }catch(IOException | ClassNotFoundException e) {
            LOG.log(Level.WARNING, "Failed to find classes in package: " + packageName, e);
            return Collections.EMPTY_LIST;
        }
    }

    /**
     * Attempts to list all the classes in the specified package as determined
     * by the context class loader
     * 
     * @param packageName
     *            the package name to search
     * @return a list of classes that exist within that package
     * @throws ClassNotFoundException if something went wrong
     * @throws IOException
     */
    public List<Class<?>> getClasses(String packageName) 
            throws ClassNotFoundException, IOException {
        
        final ArrayList<Class<?>> classes = new ArrayList<>();

        final ClassLoader cld = Thread.currentThread()
                .getContextClassLoader();

        if (cld == null) {
            throw new ClassNotFoundException("Can't get class loader.");
        }

        final Enumeration<URL> resources = cld.getResources(packageName.replace('.', '/'));

        URLConnection connection;

        for (URL url = null; resources.hasMoreElements()
                && ((url = resources.nextElement()) != null);) {

                connection = url.openConnection();

            if (connection instanceof JarURLConnection) {

                checkJarFile((JarURLConnection) connection, packageName, classes);

            } else if (connection.getClass().getCanonicalName().equals("sun.net.www.protocol.file.FileURLConnection")) {

                checkDirectory(new File(URLDecoder.decode(url.getPath(), "UTF-8")), packageName, classes);
            } else {
                throw new IOException(packageName + " ("
                        + url.getPath()
                        + ") does not appear to be a valid package");
            }    
        }

        List<Class<?>> result = classes.isEmpty() ? Collections.EMPTY_LIST : Collections.unmodifiableList(classes);
        
        if(LOG.isLoggable(Level.INFO)) {
            LOG.log(Level.INFO, "In package: {0}, found classes: {1}", new Object[]{packageName, result});
        }
        
        return result;
    }
    
    /**
     * Private helper method
     * 
     * @param directory
     *            The directory to start with
     * @param pckgname
     *            The package name to search for. Will be needed for getting the
     *            Class object.
     * @param classes
     *            if a file isn't loaded but still is in the directory
     * @throws ClassNotFoundException
     */
    private void checkDirectory(File directory, String pckgname, List<Class<?>> classes) throws ClassNotFoundException {
        File tmpDirectory;

        if (directory.exists() && directory.isDirectory()) {
            
            final String[] files = directory.list();

            for (final String file : files) {
                if (file.endsWith(".class")) {
                    try {
                        classes.add(Class.forName(pckgname + '.'
                                + file.substring(0, file.length() - 6)));
                    } catch (final NoClassDefFoundError e) {
                        // do nothing. this class hasn't been found by the
                        // loader, and we don't care.
                    }
                } else if ((tmpDirectory = new File(directory, file))
                        .isDirectory()) {
                    checkDirectory(tmpDirectory, pckgname + "." + file, classes);
                }
            }
        }
    }

    /**
     * Private helper method.
     * 
     * @param connection
     *            the connection to the jar
     * @param pckgname
     *            the package name to search for
     * @param classes
     *            the current ArrayList of all classes. This method will simply
     *            add new classes.
     * @throws ClassNotFoundException
     *             if a file isn't loaded but still is in the jar file
     * @throws IOException
     *             if it can't correctly read from the jar file.
     */
    private void checkJarFile(JarURLConnection connection, String pckgname, List<Class<?>> classes)
            throws ClassNotFoundException, IOException {
        final JarFile jarFile = connection.getJarFile();
        final Enumeration<JarEntry> entries = jarFile.entries();
        String name;

        for (JarEntry jarEntry = null; entries.hasMoreElements()
                && ((jarEntry = entries.nextElement()) != null);) {
            name = jarEntry.getName();

            if (name.contains(".class")) {
                name = name.substring(0, name.length() - 6).replace('/', '.');

                if (name.contains(pckgname)) {
                    classes.add(Class.forName(name));
                }
            }
        }
    }
}
