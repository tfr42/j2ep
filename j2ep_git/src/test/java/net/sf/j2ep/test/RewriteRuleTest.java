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
import net.sf.j2ep.rules.RewriteRule;

public class RewriteRuleTest extends TestCase {
    
    private RewriteRule rule;

    protected void setUp() throws Exception {
        rule = new RewriteRule();
    }
    
    public void testMatch() {       
        rule.setFrom("/blogs/([0-9]+)/([0-9]+)[/\\w]*");
        assertTrue("Checking for match", rule.matches(new MockHttpServletRequest() {
            public String getRequestURI() {
                return "/blogs/050505/12/";
            }
        }));
        
        assertTrue("Should get match here too", rule.matches(new MockHttpServletRequest() {
            public String getRequestURI() {
                return "/blogs/050505/1231";
            }
        }));       
        
        
        rule.setFrom("^/~(([a-z])[a-z0-9]+)(.*)");
        assertTrue("Should get match here", rule.matches(new MockHttpServletRequest() {
            public String getRequestURI() {
                return "/~j2ep/docs/api/";
            }
        })); 
        
        assertFalse("No match here", rule.matches(new MockHttpServletRequest() {
            public String getRequestURI() {
                return "/~22ep/docs/api/";
            }
        })); 
        
    }
    
    public void testRewrite() {        
        rule.setFrom("/blogs/([0-9]+)/([0-9]+)[/\\w]*");
        rule.setTo("/blog?date=$1&id=$2");
        assertEquals("Testing the rewrite", "/blog?date=010101&id=10", rule.process("/blogs/010101/10afd"));
        
        rule.setFrom("^/~(([a-z])[a-z0-9]+)(.*)");
        rule.setTo("/home/$2/$1/.www$3");
        assertEquals("Testing the rewrite", "/home/j/j2ep/.www/docs/api/", rule.process("/~j2ep/docs/api/"));
    }
    
    public void testRevert() {
        rule.setRevertFrom("/blog\\?date=([0-9]+)&id=([0-9]+)");
        rule.setRevertTo("/blogs/$1/$2/");
        assertEquals("Testing the rewrite", "/blogs/051212/1/", rule.revert("/blog?date=051212&id=1"));
        
        rule.setRevertFrom("^/home/[a-z]/([a-z0-9]+)/.www(.*)$");
        rule.setRevertTo("/~$1$2");
        assertEquals("Testing the rewrite", "/~j2ep/docs/api/", rule.revert("/home/j/j2ep/.www/docs/api/"));
    }

}
