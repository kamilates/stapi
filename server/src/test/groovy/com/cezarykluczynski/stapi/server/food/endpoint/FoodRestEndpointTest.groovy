package com.cezarykluczynski.stapi.server.food.endpoint

import com.cezarykluczynski.stapi.client.v1.rest.model.FoodBaseResponse
import com.cezarykluczynski.stapi.client.v1.rest.model.FoodFullResponse
import com.cezarykluczynski.stapi.server.common.dto.PageSortBeanParams
import com.cezarykluczynski.stapi.server.common.endpoint.AbstractRestEndpointTest
import com.cezarykluczynski.stapi.server.food.dto.FoodRestBeanParams
import com.cezarykluczynski.stapi.server.food.reader.FoodRestReader

class FoodRestEndpointTest extends AbstractRestEndpointTest {

	private static final String GUID = 'GUID'
	private static final String NAME = 'NAME'

	private FoodRestReader foodRestReaderMock

	private FoodRestEndpoint foodRestEndpoint

	void setup() {
		foodRestReaderMock = Mock(FoodRestReader)
		foodRestEndpoint = new FoodRestEndpoint(foodRestReaderMock)
	}

	void "passes get call to FoodRestReader"() {
		given:
		FoodFullResponse foodFullResponse = Mock(FoodFullResponse)

		when:
		FoodFullResponse foodFullResponseOutput = foodRestEndpoint.getFood(GUID)

		then:
		1 * foodRestReaderMock.readFull(GUID) >> foodFullResponse
		foodFullResponseOutput == foodFullResponse
	}

	void "passes search get call to FoodRestReader"() {
		given:
		PageSortBeanParams pageAwareBeanParams = Mock(PageSortBeanParams)
		pageAwareBeanParams.pageNumber >> PAGE_NUMBER
		pageAwareBeanParams.pageSize >> PAGE_SIZE
		FoodBaseResponse foodResponse = Mock(FoodBaseResponse)

		when:
		FoodBaseResponse foodResponseOutput = foodRestEndpoint.searchCompanies(pageAwareBeanParams)

		then:
		1 * foodRestReaderMock.readBase(_ as FoodRestBeanParams) >> { FoodRestBeanParams foodRestBeanParams ->
			assert pageAwareBeanParams.pageNumber == PAGE_NUMBER
			assert pageAwareBeanParams.pageSize == PAGE_SIZE
			foodResponse
		}
		foodResponseOutput == foodResponse
	}

	void "passes search post call to FoodRestReader"() {
		given:
		FoodRestBeanParams foodRestBeanParams = new FoodRestBeanParams(name: NAME)
		FoodBaseResponse foodResponse = Mock(FoodBaseResponse)

		when:
		FoodBaseResponse foodResponseOutput = foodRestEndpoint.searchCompanies(foodRestBeanParams)

		then:
		1 * foodRestReaderMock.readBase(foodRestBeanParams as FoodRestBeanParams) >> { FoodRestBeanParams params ->
			assert params.name == NAME
			foodResponse
		}
		foodResponseOutput == foodResponse
	}

}
