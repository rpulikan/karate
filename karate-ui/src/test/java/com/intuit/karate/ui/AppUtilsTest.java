/*
 * The MIT License
 *
 * Copyright 2017 Intuit Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.intuit.karate.ui;

import com.intuit.karate.ScriptEnv;
import com.intuit.karate.cucumber.FeatureSection;
import com.intuit.karate.cucumber.KarateBackend;
import com.intuit.karate.cucumber.ScenarioOutlineWrapper;
import com.intuit.karate.cucumber.ScenarioWrapper;
import com.intuit.karate.cucumber.StepResult;
import com.intuit.karate.cucumber.StepWrapper;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author pthomas3
 */
public class AppUtilsTest {
    
    private static final Logger logger = LoggerFactory.getLogger(AppUtilsTest.class);
    
    @Test
    public void testRunning() {
        FeatureBackend fb = AppUtils.getFeatureBackend("src/test/java", "feature/test.feature");
        for (FeatureSection section : fb.feature.getSections()) {
            if (section.isOutline()) {
                ScenarioOutlineWrapper outline = section.getScenarioOutline();
                for (ScenarioWrapper scenario : outline.getScenarios()) {
                    call(scenario, fb.backend);
                }
            } else {
                call(section.getScenario(), fb.backend);
            }
        }        
    }
    
    private static void call(ScenarioWrapper scenario, KarateBackend backend) {
        for (StepWrapper step : scenario.getSteps()) {
            StepResult result = step.run(backend);            
            if (!result.isPass()) {
                ScriptEnv env = scenario.getFeature().getEnv();
                throw new RuntimeException("failed: " + env, result.getError());
            }
            logger.debug("passed: {} - {}", result.isPass(), result.getStep().getText());
        }
    }    
    
}
