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

package com.peergreen.webconsole.it.community;

import javax.inject.Inject;

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

import com.peergreen.webconsole.Constants;

/**
 * @author Mohammed Boukada
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CheckCommunityConsoleFactory {
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
    public void TestConsoleFactories() throws Exception {
        osgiHelper.waitForService(Factory.class, "(service.pid=" + Constants.PRODUCTION_MODE_CONSOLE_PID + ")", 3000);
        osgiHelper.waitForService(Factory.class, "(service.pid=" + Constants.DEVELOPMENT_MODE_CONSOLE_PID + ")", 3000);
    }

    @Test
    public void testCommunityConsoleFactoryInstance() throws Exception {
        osgiHelper.waitForService(Architecture.class, "(architecture.instance=com.peergreen.webconsole.development.internal.DevelopmentConsoleFactory-0)", 3000);
    }
}
