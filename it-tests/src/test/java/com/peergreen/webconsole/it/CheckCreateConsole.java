package com.peergreen.webconsole.it;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.peergreen.deployment.Artifact;
import com.peergreen.deployment.ArtifactBuilder;
import com.peergreen.deployment.ArtifactProcessRequest;
import com.peergreen.deployment.DeploymentService;
import org.apache.felix.ipojo.architecture.Architecture;
import org.apache.felix.ipojo.extender.queue.QueueService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;
import org.ops4j.pax.exam.util.Filter;
import org.osgi.framework.BundleContext;
import org.ow2.chameleon.testing.helpers.OSGiHelper;

import javax.inject.Inject;

import java.net.URI;
import java.util.Collections;

/**
 * @author Mohammed Boukada
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@FixMethodOrder(MethodSorters.DEFAULT)
public class CheckCreateConsole {

    private final static int NB_SESSIONS = 2;
    private final static String WEB_CONSOLE_URL = "http://localhost:9000/pgadmin/";

    @Inject
    @Filter("(ipojo.queue.mode=async)")
    private QueueService queueService;

    @Inject
    private BundleContext bundleContext;

    private OSGiHelper osgiHelper;

    @Inject
    private DeploymentService deploymentService;

    @Inject
    private ArtifactBuilder artifactBuilder;

    @Before
    public void init() {
        osgiHelper = new OSGiHelper(bundleContext);
    }

    @Test
    public void checkInject() {
        Assert.assertNotNull(bundleContext);
        Assert.assertNotNull(deploymentService);
        Assert.assertNotNull(artifactBuilder);
    }

    @Test
    public void testCreateConsole() throws Exception {
        URI fileURI = new URI(System.getProperty("admin.console.test.configuration"));
        Artifact webConsoleAdmin = artifactBuilder.build("web-console-admin", fileURI);
        ArtifactProcessRequest artifactProcessRequest = new ArtifactProcessRequest(webConsoleAdmin);
        deploymentService.process(Collections.singleton(artifactProcessRequest));

        osgiHelper.waitForService(Architecture.class, "(architecture.instance=com.peergreen.webconsole.BaseConsole.*)", 0);
        osgiHelper.waitForService(Architecture.class, "(architecture.instance=com.peergreen.webconsole.core.vaadin7.UIProviderBase-0)", 0);
        for (int i=0; i < NB_SESSIONS; i++) {
            WebClient webClient = new WebClient();
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setJavaScriptEnabled(true);
            HtmlPage page = webClient.getPage(WEB_CONSOLE_URL);
            osgiHelper.waitForService(Architecture.class, "(architecture.instance=com.peergreen.webconsole.core.vaadin7.BaseUI-" + i + ")", 0);
        }
    }
}
