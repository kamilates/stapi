package com.cezarykluczynski.stapi.server.character.endpoint

import com.cezarykluczynski.stapi.client.v1.rest.model.CharacterResponse
import com.cezarykluczynski.stapi.etl.common.service.JobCompletenessDecider
import com.cezarykluczynski.stapi.server.StaticJobCompletenessDecider
import spock.lang.Requires

@Requires({
	StaticJobCompletenessDecider.isStepCompleted(JobCompletenessDecider.STEP_004_CREATE_CHARACTERS)
})
class CharacterRestEndpointIntegrationTest extends AbstractCharacterEndpointIntegrationTest {

	def setup() {
		createRestClient()
	}

	def "gets character by guid"() {
		when:
		CharacterResponse characterResponse = stapiRestClient.characterApi.characterPost(null, null, GUID, null, null,
				null)

		then:
		characterResponse.page.totalElements == 1
		characterResponse.characters[0].guid == GUID
	}

}