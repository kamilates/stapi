package com.cezarykluczynski.stapi.server

import com.cezarykluczynski.stapi.etl.configuration.job.service.StepCompletenessDecider
import com.cezarykluczynski.stapi.model.step.SimpleStep
import com.cezarykluczynski.stapi.util.constant.SpringProfile
import com.google.common.collect.Lists
import liquibase.integration.spring.SpringLiquibase
import org.springframework.batch.core.BatchStatus
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.context.ApplicationContext
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

import javax.inject.Inject
import javax.persistence.EntityManager
import javax.sql.DataSource
import javax.transaction.Transactional

class StaticJobCompletenessDecider {

	private static ConfigurableApplicationContext applicationContext

	private static List<SimpleStep> simpleStepList

	@Service
	static class MockJobCompletenessDecider {

		@Inject
		private EntityManager entityManager

		MockJobCompletenessDecider(EntityManager entityManager) {
			this.entityManager = entityManager
		}

		@Transactional
		List<SimpleStep> getSteps() {
			return simpleStepList = entityManager
					.createQuery("from SimpleStep")
					.resultList
		}

	}

	static class AlwaysCompletedStepCompletenessDecider extends StepCompletenessDecider {

		AlwaysCompletedStepCompletenessDecider() {
			super(null, null)
		}

		@Override
		boolean isStepComplete(String jobName, String stepName) {
			return true
		}

	}

	@Configuration
	@EnableBatchProcessing
	static class EtlMockConfiguration {

		@Inject
		private ApplicationContext applicationContext

		@Primary
		@Bean
		StepCompletenessDecider jobCompletenessDecider() {
			return new AlwaysCompletedStepCompletenessDecider()
		}

		@Bean
		@Profile(SpringProfile.ETL_NOT)
		SpringLiquibase liquibase() {
			return new SpringLiquibase(
					changeLog: "classpath:liquibase/changelog.xml",
					dataSource: applicationContext.getBean(DataSource)
			)
		}

	}

	static boolean isStepCompleted(String stepName) {
		initializeMockJobCompletenessDecider()

		return simpleStepList.stream().anyMatch({
			step -> step.stepName == stepName && BatchStatus.COMPLETED == step.status
		})
	}

	private static void initializeMockJobCompletenessDecider() {
		if (simpleStepList == null) {
			ConfigurableApplicationContext applicationContext = getApplicationContext()
			try {
				simpleStepList = applicationContext.getBean(MockJobCompletenessDecider).getSteps()
			} catch (Throwable e) {
				simpleStepList = Lists.newArrayList()
		}
			applicationContext.close()
		}
	}

	private static synchronized ConfigurableApplicationContext getApplicationContext() {
		return applicationContext = Application
				.produceSpringApplicationBuilder()
						.sources(EtlMockConfiguration)
						.run(
								"--server.port=8543",
								"--spring.profiles.active=default,db,source"
						)
	}

}
