package com.michaelfitzmaurice;

import static java.lang.String.format;

import java.util.Hashtable;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;

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
    
    private String randomSearchContext;
    private String randomSearchFilter;
    private int randomSearchScope;
    
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
            
        randomSearchContext = 
            ldapProperties.getProperty("ldap.random.search.context");
        randomSearchFilter = 
            ldapProperties.getProperty("ldap.random.search.filter");
        String randomSearchScopeString =
            ldapProperties.getProperty("ldap.random.search.scope", 
                                        "" + SearchControls.SUBTREE_SCOPE);
        randomSearchScope = Integer.parseInt(randomSearchScopeString);
        
        
        LOG.info("Setting up JNDI search objects...");
        searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        
        searchContext = new Hashtable<String, String>();
        LOG.info("Default initial context factory: {}", 
                    System.getProperty(Context.INITIAL_CONTEXT_FACTORY) );
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
    
    public void randomSearch() throws NamingException {
        
        LOG.info("Performing random search from context '{}' with filter '{}'", 
                    randomSearchContext, 
                    randomSearchFilter);
        InitialDirContext dirContext = new InitialDirContext(searchContext);
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(randomSearchScope);
        NamingEnumeration<SearchResult> results = 
            dirContext.search(randomSearchContext, 
                                randomSearchFilter, 
                                searchControls);
        
        int resultCount = 0;
        while ( results.hasMoreElements() ) {
            resultCount++;
            LOG.info("\tSearch result {}:", resultCount);
            SearchResult result = results.nextElement();
            Attributes attributes = result.getAttributes();
            NamingEnumeration<String> allAttrs = attributes.getIDs();
            while( allAttrs.hasMoreElements() ) {
                String attrName = allAttrs.nextElement();
                LOG.info( "\t\t{} : {}", attrName, attributes.get(attrName) );
            }
        }
        LOG.info("Finished search; found {} results", resultCount);
    }
    
    public Attributes searchingUserAttributes() throws NamingException {
        
        return new InitialDirContext(searchContext)
                        .getAttributes(ldapUsernameAttribute + "=" 
                                        + ldapSearchingUser);
    }
    
    public String objectClasses() throws NamingException {
        
        DirContext ctx = new InitialLdapContext(searchContext, null);

        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope( SearchControls.OBJECT_SCOPE );
        searchControls.setReturningAttributes( new String[]
            { "objectClasses" } );
        NamingEnumeration<SearchResult> results = 
            ctx.search( "cn=schema", "(ObjectClass=*)", searchControls );

        SearchResult result = results.next();
        Attributes entry = result.getAttributes();

        Attribute objectClasses = entry.get( "objectClasses" );
        return objectClasses.toString();
    }
    
    public String boundOjects() throws NamingException {
        
        InitialDirContext ctx = new InitialDirContext(searchContext);
        SearchControls ctls = new SearchControls();
        ctls.setReturningObjFlag(true);

        String filter = "(objectclass=*)";
        NamingEnumeration<SearchResult> answer = ctx.search("", filter, ctls);
        StringBuffer buffer = new StringBuffer();
        while ( answer.hasMore() ) {
            buffer.append( answer.nextElement().toString() );
            buffer.append("\n");
        }
        
        return buffer.toString();
    }
    
    public String baseDn() throws NamingException {
        
        int oldSearchScope = searchControls.getSearchScope();
        try {
            searchControls.setSearchScope(SearchControls.OBJECT_SCOPE);
            InitialDirContext dirContext = new InitialDirContext(searchContext);
            LOG.info("baseDn() search context DN: '{}'", 
                        dirContext.getNameInNamespace() );
            NamingEnumeration<SearchResult> results = 
                    dirContext.search("", "(objectClass=top)", searchControls);
            
            SearchResult result = results.next();
            LOG.info( "Search result name in namespace: '{}'", 
                        result.getNameInNamespace());
            Attributes attributes = result.getAttributes();
            LOG.debug("Attributes from search at root: {}", attributes);
            Attribute namingContexts = attributes.get("namingContexts");
            
            if (namingContexts == null) {
                return "unknown";
            } else {
                return namingContexts.toString();
            }
        } finally {
            searchControls.setSearchScope(oldSearchScope); 
        }
    }
    
    public Person findPerson(String username) throws NamingException {
        
        LOG.debug("Searching for person with username '{}'...", username);
        
        Person person = null;
        String searchFilter = format("(%s=%s)", ldapUsernameAttribute, username);
        InitialDirContext dir = new InitialDirContext(searchContext);
        
        LOG.debug("Searching from context '{}' with filter '{}'", 
                    ldapSearchBase, searchFilter);
        NamingEnumeration<SearchResult> results = 
            dir.search(ldapSearchBase, searchFilter, searchControls);
        if ( results.hasMore() ) {
            LOG.debug("Found a matching person for '{}", username);
            person = new Person();
            person.setUsername(username);
            
            SearchResult result = results.next();
            Attributes personAttributes = result.getAttributes();
            if (personAttributes.get("cn") != null) {
                person.setFullName( 
                    personAttributes.get("cn").get().toString() );
            }
            if (personAttributes.get("title") != null) {
                person.setTitle( 
                    personAttributes.get("title").get().toString() );
            }
            if (personAttributes.get("physicalDeliveryOfficeName") != null) {
                person.setLocation( 
                    personAttributes.get(
                        "physicalDeliveryOfficeName").get().toString() );
            }
            if (personAttributes.get("houseIdentifier") != null) {
                person.setFloor( 
                    personAttributes.get("houseIdentifier").get().toString() );
            }
            if (personAttributes.get("roomNumber") != null) {
                person.setDesk( 
                    personAttributes.get("roomNumber").get().toString() );
            }
            if (personAttributes.get("telephoneNumber") != null) {
                person.setTelephoneNumber( 
                    personAttributes.get("telephoneNumber").get().toString() );
            }
            if (personAttributes.get("mail") != null) {
                person.setEmailAddress( 
                    personAttributes.get("mail").get().toString() );
            }
            if (personAttributes.get("department") != null) {
                person.setDepartment( 
                    personAttributes.get("department").get().toString() );
            }
            if (personAttributes.get("manager") != null) {
                person.setManager( 
                    personAttributes.get("manager").get().toString() );
            }
            if (personAttributes.get("businessCategory") != null) {
                person.setManagerLevel( 
                    personAttributes.get(
                        "businessCategory").get().toString() );
            }
        } else {
            LOG.debug("Found no matching person for '{}'", username);
        }
        
        return person;
    }
}
