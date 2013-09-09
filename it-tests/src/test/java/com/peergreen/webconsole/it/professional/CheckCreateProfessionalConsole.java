package com.peergreen.webconsole.it.professional;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.peergreen.deployment.Artifact;
import com.peergreen.deployment.ArtifactBuilder;
import com.peergreen.deployment.ArtifactProcessRequest;
import com.peergreen.deployment.DeploymentService;
import com.peergreen.webconsole.Constants;
import com.peergreen.webconsole.it.community.CheckCreateCommunityConsole;

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
public class CheckCreateProfessionalConsole {

    private final static int NUM_SESSION = CheckCreateCommunityConsole.NB_SESSIONS * 2;
    private final static String UNSECURED_CONSOLE_URL = "http://localhost:9000/pgadmin/";
    private final static String SECURED_CONSOLE_URL = "http://localhost:9000/securedadmin/";

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
        URI fileURI = new URI(System.getProperty("secured.admin.console.test.configuration"));
        Artifact webConsoleAdmin = artifactBuilder.build("secured-console-admin", fileURI);
        ArtifactProcessRequest artifactProcessRequest = new ArtifactProcessRequest(webConsoleAdmin);
        deploymentService.process(Collections.singleton(artifactProcessRequest));
        Thread.sleep(1000);

        osgiHelper.waitForService(Architecture.class, "(architecture.instance=" + Constants.SECURED_CONSOLE_PID + ".*)", 0);
        osgiHelper.waitForService(Architecture.class, "(architecture.instance=com.peergreen.webconsole.core.vaadin7.BaseUIProvider-0)", 0);
        WebClient webClient = new WebClient();
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getPage(SECURED_CONSOLE_URL);
        osgiHelper.waitForService(Architecture.class, "(architecture.instance=com.peergreen.webconsole.core.vaadin7.BaseUI-0)", 0);
        webClient.closeAllWindows();

        fileURI = new URI(System.getProperty("unsecured.admin.console.test.configuration"));
        webConsoleAdmin = artifactBuilder.build("unsecured-console-admin", fileURI);
        artifactProcessRequest = new ArtifactProcessRequest(webConsoleAdmin);
        deploymentService.process(Collections.singleton(artifactProcessRequest));
        Thread.sleep(5000);

        Assert.assertTrue("Unsecured console factory is not avaible in Professional Edition",
                osgiHelper.getServiceReferences(Architecture.class, "(architecture.instance=" + Constants.UNSECURED_CONSOLE_PID + ".*)").length == 0);
        Assert.assertTrue("Unsecured console factory is not avaible in Professional Edition",
                osgiHelper.getServiceReferences(Architecture.class, "(architecture.instance=com.peergreen.webconsole.core.vaadin7.BaseUIProvider-1)").length == 0);
        boolean error404 = false;
        try {
            webClient.getPage(UNSECURED_CONSOLE_URL);
        } catch (FailingHttpStatusCodeException e) {
            error404 = true;
        }
        Assert.assertTrue("Unsecured console factory is not avaible in Professional Edition", error404);
        Assert.assertTrue("Unsecured console factory is not avaible in Professional Edition",
                osgiHelper.getServiceReferences(Architecture.class, "(architecture.instance=com.peergreen.webconsole.core.vaadin7.BaseUI-1)").length == 0);
        webClient.closeAllWindows();
    }
}
