/**
 * Copyright 2013 Peergreen S.A.S.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.peergreen.webconsole.it;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.apache.felix.ipojo.extender.queue.QueueService;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;
import org.ops4j.pax.exam.util.Filter;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import java.net.URI;
import java.net.URISyntaxException;

import static org.ops4j.pax.exam.CoreOptions.junitBundles;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.options;
import static org.ops4j.pax.exam.CoreOptions.systemProperty;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WaitForStability {

    @Inject
    @Filter("(ipojo.queue.mode=async)")
    private QueueService queueService;

    private StabilityHelper helper;

    @Configuration
    public Option[] config() throws URISyntaxException {
        // Reduce log level.
        Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        root.setLevel(Level.INFO);
        URI file = getClass().getResource("/adminConsole.xml").toURI();

        return options(systemProperty("admin.console.test.configuration").value(file.toString()),
                systemProperty("org.ops4j.pax.logging.DefaultServiceLog.level").value("WARN"),
                mavenBundle("org.slf4j", "slf4j-api").version("1.7.2"),
                mavenBundle("org.slf4j", "slf4j-simple").version("1.7.2").noStart(),
                mavenBundle("org.jsoup", "jsoup").version("1.6.3"),
                mavenBundle("org.atmosphere", "atmosphere-runtime").version("1.0.12"),
                mavenBundle("com.vaadin", "vaadin-shared-deps").version("1.0.2"),
                mavenBundle("com.peergreen.webconsole", "vaadin-7.1.0.beta1").version("1.0.0-SNAPSHOT"),
                mavenBundle("com.peergreen.webconsole", "htmlunit-all").version("1.0.0-SNAPSHOT"),
                mavenBundle("com.peergreen.webconsole", "web-console-api").version("1.0.0-SNAPSHOT"),
                mavenBundle("com.peergreen.webconsole", "web-console-core").version("1.0.0-SNAPSHOT"),
                junitBundles()
        );
    }

    @Before
    public void init() throws Exception {
        helper = new StabilityHelper(queueService);
        helper.waitForStability();
    }

    /**
     * Dummy empty test used to launch all configuration/before checks
     */
    @Test
    public void testEmpty() {

    }
}
