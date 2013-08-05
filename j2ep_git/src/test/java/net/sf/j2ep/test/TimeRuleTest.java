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

import java.util.Calendar;

import junit.framework.TestCase;
import net.sf.j2ep.rules.TimeRule;

public class TimeRuleTest extends TestCase {
    
    TimeRule rule;
    int now;

    protected void setUp() throws Exception {
        rule = new TimeRule();
        now = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    }

    public void testMatch() {
        rule.setStartTime(""+(now-1));
        rule.setEndTime(""+(now+1));
        assertTrue("Current should be between the start and end", rule.matches(new MockHttpServletRequest() {}));
        
        rule.setStartTime(""+now);
        rule.setEndTime(""+now);
        assertTrue("Current should be between the start and end", rule.matches(new MockHttpServletRequest() {}));
        
        rule.setStartTime(""+(now+24));
        rule.setEndTime(""+(now-23));
        assertTrue("Current should be between the start and end", rule.matches(new MockHttpServletRequest() {}));
    }
    
    public void testNoMatches() {
        rule.setStartTime(""+(now+1));
        rule.setEndTime(""+(now+1));
        assertFalse("Current shoudn't be covered", rule.matches(new MockHttpServletRequest() {}));
        
        rule.setStartTime(""+(now-1));
        rule.setEndTime(""+(now-1));
        assertFalse("Current shoudn't be covered", rule.matches(new MockHttpServletRequest() {}));
    }
    
    public void testDateCrossed() {
        rule.setStartTime("02");
        rule.setEndTime("01");
        assertTrue("Current should be between the start and end", rule.matches(new MockHttpServletRequest() {}));
    }

}
