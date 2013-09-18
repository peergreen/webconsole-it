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

import com.peergreen.webconsole.Constants;

import org.apache.felix.ipojo.Factory;
import org.apache.felix.ipojo.architecture.Architecture;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;
import org.osgi.framework.BundleContext;
import org.ow2.chameleon.testing.helpers.OSGiHelper;

import javax.inject.Inject;

/**
 * @author Mohammed Boukada
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CheckProfessionalConsoleFactory {
    @Inject
    private BundleContext bundleContext;

    private OSGiHelper osgiHelper;

    @Before
    public void init() {
        osgiHelper = new OSGiHelper(bundleContext);
    }

    @Test
    public void checkInject() {
        Assert.assertNotNull(bundleContext);
    }

    @Test
    public void TestSecuredConsoleFactory() throws Exception {
        osgiHelper.waitForService(Factory.class, "(service.pid=" + Constants.PRODUCTION_MODE_CONSOLE_PID + ")", 3000);
        Assert.assertTrue("Unsecured console factory is not avaible in Professional Edition",
                osgiHelper.getServiceReferences(Factory.class, "(service.pid=" + Constants.DEVELOPMENT_MODE_CONSOLE_PID + ")").length == 0);
    }

    @Test
    public void testProfessionalConsoleFactoryInstance() throws Exception {
        osgiHelper.waitForService(Architecture.class, "(architecture.instance=com.peergreen.webconsole.professional.ProfessionalConsoleFactory-0)", 3000);
    }
}
