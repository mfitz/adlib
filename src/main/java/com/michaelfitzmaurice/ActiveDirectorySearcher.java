package com.michaelfitzmaurice;

import java.util.Hashtable;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActiveDirectorySearcher {
    
    private static final transient Logger LOG = 
        LoggerFactory.getLogger(ActiveDirectorySearcher.class);

    private String ldapUrl;
    private String ldapSearchingUser;
    private String ldapSearchingUserPassword;
    private String ldapSearchBase;
    private String ldapUsernameAttribute;
    private String ldapSecurityAuthentication;
    private String ldapSecurityProtocol;
    
    private Hashtable<String, String> searchContext;
    private SearchControls searchControls;
    
    public ActiveDirectorySearcher(Properties ldapProperties) {
        
        LOG.info("Reading LDAP configuration from properties object...");
        ldapUrl = ldapProperties.getProperty("ldap.url");
        ldapSearchingUser = ldapProperties.getProperty("ldap.searching.user");
        ldapSearchingUserPassword = 
            ldapProperties.getProperty("ldap.searching.user.password");
        ldapSearchBase = ldapProperties.getProperty("ldap.search.base");
        ldapUsernameAttribute = 
            ldapProperties.getProperty("ldap.username.attribute");
        ldapSecurityAuthentication = 
            ldapProperties.getProperty("ldap.security.authentication");
        ldapSecurityProtocol = 
            ldapProperties.getProperty("ldap.security.protocol");
        LOG.info("Finished reading LDAP configuration");
        
        LOG.info("Setting up JNDI search objects...");
        searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        
        searchContext = new Hashtable<String, String>();
        searchContext.put(  Context.INITIAL_CONTEXT_FACTORY, 
                            "com.sun.jndi.ldap.LdapCtxFactory");
        searchContext.put(Context.PROVIDER_URL, ldapUrl);
        if (ldapSecurityAuthentication == null){
            LOG.info("Defaulting to simple security authentication");
            ldapSecurityAuthentication = "simple";
        }
        searchContext.put(Context.SECURITY_AUTHENTICATION, 
                            ldapSecurityAuthentication);
        if (ldapSecurityProtocol != null){
            searchContext.put(Context.SECURITY_PROTOCOL, ldapSecurityProtocol);
        }
        searchContext.put(Context.SECURITY_PRINCIPAL, ldapSearchingUser);
        searchContext.put(Context.SECURITY_CREDENTIALS, 
                            ldapSearchingUserPassword);
        searchContext.put(Context.REFERRAL, "follow" );
        LOG.info("Finished setting up JNDI search objects");
    }
    
    public Attributes searchingUserAttributes() throws NamingException {
        
        InitialDirContext dir = new InitialDirContext(searchContext);
        
        return dir.getAttributes(ldapSearchingUser);
    }
}
