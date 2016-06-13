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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;

/**
 * CloudFoundry specific implementation of {@link AppDeploymentCustomizer}
 *
 * @author Soby Chacko
 */
public class CloudFoundryAppDeploymentCustomizer implements AppDeploymentCustomizer, InitializingBean {

	private static final Log logger = LogFactory.getLog(CloudFoundryAppDeploymentCustomizer.class);

//	private final String DEFAULT_SPRING_APPLICATION_NAME = "spring-cloud-dataflow-server-cloudfoundry";
//
//	private final String DEFAULT_DATAFLOW_NAME_TO_USE = "dataflow";

	private String prefixToUse = "";

	private final CloudFoundryDeployerProperties properties;
	private final WordListRandomWords wordListRandomWords;

//	@Value("${spring.application.name:}")
//	private String springApplicationName;

	public CloudFoundryAppDeploymentCustomizer(CloudFoundryDeployerProperties cloudFoundryDeployerProperties,
											   WordListRandomWords wordListRandomWords) {
		this.properties = cloudFoundryDeployerProperties;
		this.wordListRandomWords = wordListRandomWords;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (properties.isEnableRandomAppNamePrefix()) {
			prefixToUse = createUniquePrefix();
			if (!StringUtils.isEmpty(properties.getAppNamePrefix())) {
				prefixToUse = String.format("%s-%s", properties.getAppNamePrefix(), prefixToUse);
			}
		} else {
			if (!StringUtils.isEmpty(properties.getAppNamePrefix())) {
				prefixToUse = properties.getAppNamePrefix();
			}
		}
//		if (!StringUtils.isEmpty(properties.getAppNamePrefix())) {
//			prefixToUse = String.format("%s-%s", properties.getAppNamePrefix(), prefixToUse);
//		}
		logger.info(String.format("Prefix to be used for deploying apps: %s", prefixToUse));
	}


	@Override
	public String deploymentIdWithUniquePrefix(String appName) {
		if (StringUtils.isEmpty(prefixToUse)) {
			return appName;
		} else {
			return String.format("%s-%s", prefixToUse, appName);
		}
	}

	private String createUniquePrefix() {
		return String.format("%s-%s", wordListRandomWords.getAdjective(), wordListRandomWords.getNoun());
	}


//	private boolean shallFallBackToDefault() {
//		return StringUtils.isEmpty(springApplicationName) ||
//				springApplicationName.equals(DEFAULT_SPRING_APPLICATION_NAME);
//	}

}
