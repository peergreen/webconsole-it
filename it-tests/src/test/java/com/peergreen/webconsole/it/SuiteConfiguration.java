package com.peergreen.webconsole.it;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.peergreen.webconsole.it.common.StabilityHelper;
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

import static org.ops4j.pax.exam.CoreOptions.junitBundles;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.options;
import static org.ops4j.pax.exam.CoreOptions.systemProperty;

/**
 * @author Mohammed Boukada
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SuiteConfiguration {
    @Inject
    @Filter("(ipojo.queue.mode=async)")
    private QueueService queueService;

    private StabilityHelper helper;

    @Configuration
    public Option[] config() throws Exception {
        // Reduce log level.
        Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        root.setLevel(Level.INFO);
        URI unsecuredConsoleFile = getClass().getResource("/unsecuredConsole.xml").toURI();
        URI securedConsoleFile = getClass().getResource("/securedConsole.xml").toURI();
        URI communityDeploymentPlan = getClass().getResource("/communityDeploymentPlan.xml").toURI();
        URI professionalDeploymentPlan = getClass().getResource("/professionalDeploymentPlan.xml").toURI();

        return options(systemProperty("unsecured.admin.console.test.configuration").value(unsecuredConsoleFile.toString()),
                systemProperty("secured.admin.console.test.configuration").value(securedConsoleFile.toString()),
                systemProperty("community.deployment.plan").value(communityDeploymentPlan.toString()),
                systemProperty("professional.deployment.plan").value(professionalDeploymentPlan.toString()),
                systemProperty("org.ops4j.pax.logging.DefaultServiceLog.level").value("WARN"),
                mavenBundle("org.ow2.chameleon.testing", "osgi-helpers").version("0.6.0"),
                mavenBundle("com.peergreen.webconsole", "htmlunit-all").version("1.0.0-SNAPSHOT"),
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