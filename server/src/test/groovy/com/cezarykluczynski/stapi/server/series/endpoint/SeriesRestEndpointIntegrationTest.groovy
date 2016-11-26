package com.cezarykluczynski.stapi.server.series.endpoint

import com.cezarykluczynski.stapi.client.v1.rest.model.SeriesResponse
import com.cezarykluczynski.stapi.etl.common.service.JobCompletenessDecider
import com.cezarykluczynski.stapi.server.StaticJobCompletenessDecider
import spock.lang.Requires

@Requires({
	StaticJobCompletenessDecider.isStepCompleted(JobCompletenessDecider.STEP_001_CREATE_SERIES)
})
class SeriesRestEndpointIntegrationTest extends AbstractSeriesEndpointIntegrationTest {

	def setup() {
		createRestClient()
	}

	def "gets all series"() {
		given:
		Integer pageNumber = 0
		Integer pageSize = 10

		when:
		SeriesResponse seriesResponse = stapiRestClient.seriesApi.seriesGet(pageNumber, pageSize)

		then:
		seriesResponse.page.pageNumber == pageNumber
		seriesResponse.page.pageSize == pageSize
		seriesResponse.series.size() == 6
	}

	def "gets series by title"() {
		given:
		Integer pageNumber = 0
		Integer pageSize = 2

		when:
		SeriesResponse seriesResponse = stapiRestClient.seriesApi.seriesPost(pageNumber, pageSize, null, VOYAGER, null,
				null, null, null, null, null, null, null, null)

		then:
		seriesResponse.page.pageNumber == pageNumber
		seriesResponse.page.pageSize == pageSize
		seriesResponse.series.size() == 1
		seriesResponse.series[0].title.contains VOYAGER
	}

	def "gets series by guid"() {
		given:
		Integer pageNumber = 0
		Integer pageSize = 2

		when:
		SeriesResponse seriesResponse = stapiRestClient.seriesApi.seriesPost(pageNumber, pageSize, GUID, null, null,
				null, null, null, null, null, null, null, null)

		then:
		seriesResponse.series.size() == 1
		seriesResponse.series[0].abbreviation == TAS
		seriesResponse.page.pageNumber == pageNumber
		seriesResponse.page.pageSize == pageSize
	}

}
