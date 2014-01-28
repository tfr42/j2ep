package net.sf.j2ep.servers;

import junit.framework.TestCase;
import net.sf.j2ep.model.Server;
import net.sf.j2ep.rules.DirectoryRule;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

/**
 * Tests for BaseServerFromUrlCreator.
 *
 * @author <a href="mailto:stenger@lat-lon.de">Dirk Stenger</a>
 * @author last edited by: $Author: stenger $
 * @version $Revision: $, $Date: $
 */
public class BaseServerFromUrlCreatorTest extends TestCase {

    private static final String URL = "http://www.test.de/url/j2ep?";

    private static final String URL_WITHOUT_PROTOCOL = "www.test.de/url/j2ep?";

    private static final String URL_WITHOUT_QUESTION_MARK = "http://www.test.de/url/j2ep";

    private static final String REQUEST_SERVLET_PATH = "/path";

    private static final String EXPECTED_DOMAIN_NAME = "www.test.de";

    private static final String EXPECTED_PATH = "/url/j2ep";

    private static final String EXPECTED_DIRECTORY = "/path";

    private final BaseServerFromUrlCreator serverCreator = new BaseServerFromUrlCreator();

    public void testCreateServer() {
        ServletRequest request = mockServletRequest();
        Server server = serverCreator.createServer( URL, request );

        assertEquals( EXPECTED_DOMAIN_NAME, server.getDomainName() );
        assertEquals( EXPECTED_PATH, server.getPath() );
        DirectoryRule rule = (DirectoryRule) server.getRule();
        assertEquals( EXPECTED_DIRECTORY, rule.getDirectory() );
    }

    public void testCreateServerWithoutProtocol() {
        ServletRequest request = mockServletRequest();
        Server server = serverCreator.createServer( URL_WITHOUT_PROTOCOL, request );

        assertEquals( EXPECTED_DOMAIN_NAME, server.getDomainName() );
        assertEquals( EXPECTED_PATH, server.getPath() );
        DirectoryRule rule = (DirectoryRule) server.getRule();
        assertEquals( EXPECTED_DIRECTORY, rule.getDirectory() );
    }

    public void testCreateServerWithoutQuestionMark() {
        ServletRequest request = mockServletRequest();
        Server server = serverCreator.createServer( URL_WITHOUT_QUESTION_MARK, request );

        assertEquals( EXPECTED_DOMAIN_NAME, server.getDomainName() );
        assertEquals( EXPECTED_PATH, server.getPath() );
        DirectoryRule rule = (DirectoryRule) server.getRule();
        assertEquals( EXPECTED_DIRECTORY, rule.getDirectory() );
    }

    public void testCreateServerWithNullValues() {
        try {
            serverCreator.createServer( null, null );
            fail( "Expected an IllegalArugmentException to be thrown" );
        } catch ( IllegalArgumentException exception ) {
        }
    }

    public void testCreateServerWithUrlNullValue() {
        ServletRequest request = mockServletRequest();
        try {
            serverCreator.createServer( null, request );
            fail( "Expected an IllegalArugmentException to be thrown" );
        } catch ( IllegalArgumentException exception ) {
        }
    }

    public void testCreateServerWithRequestNullValue() {
        try {
            serverCreator.createServer( URL, null );
            fail( "Expected an IllegalArugmentException to be thrown" );
        } catch ( IllegalArgumentException exception ) {
        }
    }

    private ServletRequest mockServletRequest() {
        HttpServletRequest request = mock( HttpServletRequest.class );
        doReturn( REQUEST_SERVLET_PATH ).when( request ).getServletPath();
        return request;
    }
}
