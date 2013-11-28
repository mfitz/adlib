package com.michaelfitzmaurice;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 
 * @author Michael Fitzmaurice, November 2013
 */
public class App {
    
    private static final transient Logger LOG = 
        LoggerFactory.getLogger(App.class);
    
    public static void main(String[] args) throws Exception {
        
//        System.out.println("Hello World!");
        
        LOG.info("Looking for ldap properties file...");
        URL fileUrl = new Object().getClass().getResource("/ldap.properties");
        if (fileUrl == null) {
            throw new IOException("No ldap.properties file found on the classpath");
        }
        LOG.info("Found ldap.properties file at {}", fileUrl);
        
        File propsFile = new File( fileUrl.getPath() );
        Properties ldapProperties = new Properties();
        ldapProperties.load( new FileReader(propsFile) );
        
        ActiveDirectorySearcher searcher = 
            new ActiveDirectorySearcher(ldapProperties);
        LOG.info( "Searching user attributes: " 
                    + searcher.searchingUserAttributes() );
    }
}
