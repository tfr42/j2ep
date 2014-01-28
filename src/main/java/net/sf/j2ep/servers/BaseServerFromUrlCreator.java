package net.sf.j2ep.servers;

import net.sf.j2ep.model.Server;
import net.sf.j2ep.rules.DirectoryRule;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

/**
 * Creates a BaseServer-object from passed url.
 *
 * @author <a href="mailto:stenger@lat-lon.de">Dirk Stenger</a>
 * @author last edited by: $Author: stenger $
 * @version $Revision: $, $Date: $
 */
public class BaseServerFromUrlCreator {

    private static final String BASE_SERVER_IS_REWRITING = "true";

    private static final String DIRECTORY_RULE_IS_APPENDING_TRAILING_SLASH = "false";

    /**
     * Create a BaseServer-object from url and request.
     *
     * @param url,     never null
     * @param request, never null
     * @return Server
     */
    public Server createServer( String url, ServletRequest request ) {
        checkRequiredParameters( url, request );
        String urlWithoutProtocol = retrieveUrlWithoutProtocol( url );
        int divideDomainNameAndPathIndex = urlWithoutProtocol.indexOf( "/" );
        String domainName = urlWithoutProtocol.substring( 0, divideDomainNameAndPathIndex );
        String path = retrievePath( urlWithoutProtocol, divideDomainNameAndPathIndex );
        String directoryPath = retrieveDirectoryPath( request );
        return createServer( domainName, path, directoryPath );
    }

    private String retrieveUrlWithoutProtocol( String url ) {
        if ( url.contains( "//" ) ) {
            int protocolIndex = url.indexOf( "//" );
            return url.substring( protocolIndex + 2 );
        } else {
            return url;
        }
    }

    private String retrievePath( String urlWithoutProtocol, int divideDomainNameAndPathIndex ) {
        String path = urlWithoutProtocol.substring( divideDomainNameAndPathIndex );
        if ( path.endsWith( "?" ) )
            return path.substring( 0, path.length() - 1 );
        else
            return path;
    }

    private String retrieveDirectoryPath( ServletRequest request ) {
        String directoryPath = ( (HttpServletRequest) request ).getServletPath();
        if ( directoryPath.contains( "/" ) ) {
            int directoryPathIndex = directoryPath.lastIndexOf( "/" );
            return directoryPath.substring( directoryPathIndex );
        } else {
            return directoryPath;
        }
    }

    private Server createServer( String domainName, String path, String directoryPath ) {
        BaseServer baseServer = createBaseServer( domainName, path );
        DirectoryRule directoryRule = createDirectoryRule( directoryPath );
        baseServer.setRule( directoryRule );
        return baseServer;
    }

    private BaseServer createBaseServer( String domainName, String path ) {
        BaseServer baseServer = new BaseServer();
        baseServer.setDomainName( domainName );
        baseServer.setPath( path );
        baseServer.setIsRewriting( BASE_SERVER_IS_REWRITING );
        return baseServer;
    }

    private DirectoryRule createDirectoryRule( String directoryPath ) {
        DirectoryRule directoryRule = new DirectoryRule();
        directoryRule.setDirectory( directoryPath );
        directoryRule.setIsAppendTrailingSlash( DIRECTORY_RULE_IS_APPENDING_TRAILING_SLASH );
        return directoryRule;
    }

    private void checkRequiredParameters( String url, ServletRequest request ) {
        if ( url == null )
            throw new IllegalArgumentException( "Url must not be null!" );
        if ( request == null )
            throw new IllegalArgumentException( "Request must not be null!" );
    }
}
