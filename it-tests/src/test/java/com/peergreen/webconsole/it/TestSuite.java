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
package com.peergreen.webconsole.it;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.peergreen.webconsole.it.common.CheckConsoleBase;
import com.peergreen.webconsole.it.community.CheckCommunityConsoleFactory;
import com.peergreen.webconsole.it.community.CheckCreateCommunityConsole;
import com.peergreen.webconsole.it.community.CommunityConfiguration;

@RunWith(Suite.class)
@SuiteClasses({SuiteConfiguration.class, CommunityConfiguration.class, CheckConsoleBase.class,
        CheckCommunityConsoleFactory.class, CheckCreateCommunityConsole.class})
public class TestSuite {

}
