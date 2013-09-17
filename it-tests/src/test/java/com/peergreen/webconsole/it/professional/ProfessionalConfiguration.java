/**
 * Peergreen S.A.S. All rights reserved.
 * Proprietary and confidential.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.peergreen.webconsole.it.professional;

import com.peergreen.deployment.Artifact;
import com.peergreen.deployment.ArtifactBuilder;
import com.peergreen.deployment.ArtifactProcessRequest;
import com.peergreen.deployment.DeploymentMode;
import com.peergreen.deployment.DeploymentService;
import com.peergreen.webconsole.it.common.StabilityHelper;

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

import javax.inject.Inject;
import java.net.URI;
import java.util.Collections;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProfessionalConfiguration {

    @Inject
    @Filter("(ipojo.queue.mode=async)")
    private QueueService queueService;

    private StabilityHelper helper;

    @Inject
    private DeploymentService deploymentService;

    @Inject
    private ArtifactBuilder artifactBuilder;

    @Inject
    private BundleContext bundleContext;

    @Test
    public void checkInject() {
        Assert.assertNotNull(bundleContext);
        Assert.assertNotNull(deploymentService);
        Assert.assertNotNull(artifactBuilder);
    }

    @Before
    public void configure() throws Exception {
        helper = new StabilityHelper(queueService);

        URI fileURI = new URI(System.getProperty("unsecured.admin.console.test.configuration"));
        Artifact artifact = artifactBuilder.build("unsecured-console-admin", fileURI);
        ArtifactProcessRequest artifactProcessRequest = new ArtifactProcessRequest(artifact);
        artifactProcessRequest.setDeploymentMode(DeploymentMode.UNDEPLOY);
        deploymentService.process(Collections.singleton(artifactProcessRequest));
        helper.waitForStability();

        fileURI = new URI(System.getProperty("secured.admin.console.test.configuration"));
        artifact = artifactBuilder.build("secured-console-admin", fileURI);
        artifactProcessRequest = new ArtifactProcessRequest(artifact);
        artifactProcessRequest.setDeploymentMode(DeploymentMode.UNDEPLOY);
        deploymentService.process(Collections.singleton(artifactProcessRequest));
        helper.waitForStability();

        fileURI = new URI(System.getProperty("community.deployment.plan"));
        artifact = artifactBuilder.build("web-console-community", fileURI);
        artifactProcessRequest = new ArtifactProcessRequest(artifact);
        artifactProcessRequest.setDeploymentMode(DeploymentMode.UNDEPLOY);
        deploymentService.process(Collections.singleton(artifactProcessRequest));
        helper.waitForStability();

        fileURI = new URI(System.getProperty("professional.deployment.plan"));
        artifact = artifactBuilder.build("web-console-professional", fileURI);
        artifactProcessRequest = new ArtifactProcessRequest(artifact);
        artifactProcessRequest.setDeploymentMode(DeploymentMode.DEPLOY);
        deploymentService.process(Collections.singleton(artifactProcessRequest));
        helper.waitForStability();
    }
}
