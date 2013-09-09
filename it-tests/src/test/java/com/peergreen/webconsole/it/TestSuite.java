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

import com.peergreen.webconsole.it.common.CheckConsoleBase;
import com.peergreen.webconsole.it.community.CheckCommunityConsoleFactory;
import com.peergreen.webconsole.it.community.CheckCreateCommunityConsole;
import com.peergreen.webconsole.it.community.CommunityConfiguration;
import com.peergreen.webconsole.it.professional.CheckCreateProfessionalConsole;
import com.peergreen.webconsole.it.professional.CheckProfessionalConsoleFactory;
import com.peergreen.webconsole.it.professional.ProfessionalConfiguration;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({SuiteConfiguration.class, CommunityConfiguration.class, CheckConsoleBase.class,
        CheckCommunityConsoleFactory.class, CheckCreateCommunityConsole.class,
        ProfessionalConfiguration.class, CheckProfessionalConsoleFactory.class,
        CheckCreateProfessionalConsole.class})
public class TestSuite {

}
