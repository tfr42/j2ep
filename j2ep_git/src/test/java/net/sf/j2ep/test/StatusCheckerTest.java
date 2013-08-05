/*
 * Copyright 1999-2005 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.sf.j2ep.test;

import junit.framework.TestCase;
import net.sf.j2ep.model.Server;
import net.sf.j2ep.servers.BaseServer;
import net.sf.j2ep.servers.ServerStatusChecker;
import net.sf.j2ep.servers.ServerStatusListener;

public class StatusCheckerTest extends TestCase {
    
    private ServerStatusChecker statusChecker;
    private TestStatusListener listener;
    private BaseServer server;

    protected void setUp() throws Exception {
        listener = new TestStatusListener();
        statusChecker = new ServerStatusChecker(listener, 1);
        statusChecker.start();
        server = new BaseServer();
    }
    
    public void testAddServer() {
        server.setDomainName("localhost:8080");
        server.setPath("/test-response");
        listener.makeReady();
        statusChecker.addServer(server);
        while (!listener.gotResponse()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                return;
            }
        }
        assertEquals("Should be getting the expected server", server, listener.getNextOnline());
        
        //Taking server offline
        listener.makeReady();
        server.setDomainName("locallkjlkjlkjhost:8080");
        statusChecker.interrupt();
        while (!listener.gotResponse()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                return;
            }
        }
        assertEquals("Should be getting the expected server", server, listener.getNextOffline());
        
        // Taking server online
        listener.makeReady();
        server.setDomainName("localhost:8080");
        statusChecker.interrupt();
        while (!listener.gotResponse()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                return;
            }
        }
        assertEquals("Should be getting the expected server", server, listener.getNextOnline());
    }
    
    protected void tearDown() {
        statusChecker = null;
    }

    private class TestStatusListener implements ServerStatusListener {
        
        private Server offline;
        private Server online;
        private volatile boolean gotResponse;
        
        public synchronized void serverOnline(Server theServer) {
            online = theServer;
            gotResponse = true;
        }

        public synchronized void serverOffline(Server theServer) {
            offline = theServer;
            gotResponse = true;
        }

        public Server getNextOffline() {
            return offline;
        }

        public Server getNextOnline() {
            return online;
        }
        
        public boolean gotResponse() {
            return gotResponse;
        }
        
        public void makeReady() {
            gotResponse = false;
            online = null;
            offline = null;
        }

    }
}
