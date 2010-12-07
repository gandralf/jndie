package com.devx.naming;


import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;
import javax.naming.spi.ObjectFactory;

import org.apache.log4j.Logger;
import com.devx.naming.url.NullContextFactory;

/**
 * Initial context factory
 * @author Alexandre Gandra Lages
 */
public class MockContextFactory implements InitialContextFactory, ObjectFactory {
	private static final Logger log = Logger.getLogger(MockContextFactory.class);
	private static String additionalPkgs;

    static {
		init();
	}

    /**
     * This method has the following responsabilities:<ul>
     * <li>Prepares the auxiliary context to be used, defined by
     * "com.devx.naming.factory.initial" and "java.naming.factory.url.pkgs"
     * system properties</li>
     * <li>Configures the "java.naming.factory.url.pkgs" system property
     * so it will be equals to 
     * "com.devx.naming[:old java.naming.factory.url.pkgs value]>"</li>
     * </ul>
     */
	static void init() {
        // 1. It saves the additional ContextFactory, found on "com.devx.naming.factory.initial"
        String newPkgs = "com.devx.naming.url";
        String systemPkgs = System.getProperty(Context.URL_PKG_PREFIXES);
        if (systemPkgs != null && systemPkgs.indexOf(newPkgs) != -1 ) {
            additionalPkgs =  systemPkgs.replaceAll("com\\.devx\\.naming\\.url(:|$)", "").trim();
            additionalPkgs = additionalPkgs.length() != 0 ? additionalPkgs : null;
        } else {
            additionalPkgs = systemPkgs;
        }

        // 2. It sets the goodn'old pkgs (and show a log message), but only if necessary
        if (systemPkgs == null || !systemPkgs.startsWith("com.devx.naming.url(:|$)")) {
            if (additionalPkgs != null && additionalPkgs.length() > 0) {
                // Mantando os pkgs antigos no path faz o lookup funcionar
                // em outros namespaces que nao java:, sem a intervencao MockContext & cia
                newPkgs = newPkgs + ":" + additionalPkgs;
                log.debug("Setting " + Context.URL_PKG_PREFIXES + "=" + newPkgs +
                        " (was " + additionalPkgs + ")");
            } else {
                log.debug("Setting " + Context.URL_PKG_PREFIXES + "=" + newPkgs);
            }
            System.setProperty(Context.URL_PKG_PREFIXES, newPkgs);
        }
	}
	
	public Context getInitialContext(Hashtable<?,?> environment) throws NamingException {
    	if (additionalPkgs != null) {
			Hashtable<String, String> additionalContextEnvironment = new Hashtable(environment);
            additionalContextEnvironment.put(Context.INITIAL_CONTEXT_FACTORY,
                    NullContextFactory.class.getName());

            additionalContextEnvironment.put(Context.URL_PKG_PREFIXES,
                    additionalPkgs);

			Context additionalContext = new InitialContext(additionalContextEnvironment);
	        return new MockContext(additionalContext);
		} else {
			return new MockContext();
		}
    }

	public Object getObjectInstance(Object obj, Name name, Context nameCtx,
            Hashtable environment) throws Exception {
        return null;
    }
}
