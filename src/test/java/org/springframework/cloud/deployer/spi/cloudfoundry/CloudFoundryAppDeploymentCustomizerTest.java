/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.cloud.deployer.spi.cloudfoundry;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import org.junit.Test;

import org.springframework.test.util.ReflectionTestUtils;

/**
 * @author Soby Chacko
 * @author Mark Pollack
 */
public class CloudFoundryAppDeploymentCustomizerTest {

	@Test
	public void testDeploymentIdWithAppNamePrefixAndRandomAppNamePrefixFalse() throws Exception {
		CloudFoundryDeployerProperties properties = new CloudFoundryDeployerProperties();
		properties.setEnableRandomAppNamePrefix(false);
		properties.setAppNamePrefix("dataflow");
		CloudFoundryAppDeploymentCustomizer deploymentCustomizer =
				new CloudFoundryAppDeploymentCustomizer(properties, new WordListRandomWords());
		deploymentCustomizer.afterPropertiesSet();

		assertEquals("dataflow-foo", deploymentCustomizer.deploymentIdWithUniquePrefix("foo") );
	}

	@Test
	public void testDeploymentIdWithAppNamePrefixAndRandomAppNamePrefixTrue() throws Exception {
		CloudFoundryDeployerProperties properties = new CloudFoundryDeployerProperties();
		properties.setEnableRandomAppNamePrefix(true);
		properties.setAppNamePrefix("dataflow-longername");
		CloudFoundryAppDeploymentCustomizer deploymentCustomizer =
				new CloudFoundryAppDeploymentCustomizer(properties, new WordListRandomWords());
		deploymentCustomizer.afterPropertiesSet();

		String deploymentIdWithUniquePrefix = deploymentCustomizer.deploymentIdWithUniquePrefix("foo");
		assertTrue(deploymentIdWithUniquePrefix.startsWith("dataflow-"));
		assertTrue(deploymentIdWithUniquePrefix.endsWith("-foo"));
		assertTrue(deploymentIdWithUniquePrefix.matches("dataflow-longername-\\w+-\\w+-foo"));

		String deploymentIdWithUniquePrefixAgain = deploymentCustomizer.deploymentIdWithUniquePrefix("foo");

		assertEquals(deploymentIdWithUniquePrefix, deploymentIdWithUniquePrefixAgain);
	}

	@Test
	public void testDeploymentIdWithoutAppNamePrefixAndRandomAppNamePrefixTrue() throws Exception {
		CloudFoundryDeployerProperties properties = new CloudFoundryDeployerProperties();
		properties.setEnableRandomAppNamePrefix(true);
		properties.setAppNamePrefix("");
		CloudFoundryAppDeploymentCustomizer deploymentCustomizer =
				new CloudFoundryAppDeploymentCustomizer(properties, new WordListRandomWords());
		deploymentCustomizer.afterPropertiesSet();

		String deploymentIdWithUniquePrefix = deploymentCustomizer.deploymentIdWithUniquePrefix("foo");
		assertTrue(deploymentIdWithUniquePrefix.endsWith("-foo"));

		assertTrue(deploymentIdWithUniquePrefix.matches("\\w+-\\w+-foo"));
	}

	@Test
	public void testDeploymentIdWithoutAppNamePrefixAndRandomAppNamePrefixFalse() throws Exception {
		CloudFoundryDeployerProperties properties = new CloudFoundryDeployerProperties();
		properties.setEnableRandomAppNamePrefix(false);
		properties.setAppNamePrefix("");
		CloudFoundryAppDeploymentCustomizer deploymentCustomizer =
				new CloudFoundryAppDeploymentCustomizer(properties, new WordListRandomWords());
		deploymentCustomizer.afterPropertiesSet();

		assertEquals("foo", deploymentCustomizer.deploymentIdWithUniquePrefix("foo"));
	}

}
