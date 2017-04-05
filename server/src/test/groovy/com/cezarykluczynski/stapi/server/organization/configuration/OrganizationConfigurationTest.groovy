package com.cezarykluczynski.stapi.server.organization.configuration

import com.cezarykluczynski.stapi.server.common.endpoint.EndpointFactory
import com.cezarykluczynski.stapi.server.organization.endpoint.OrganizationSoapEndpoint
import com.cezarykluczynski.stapi.server.organization.mapper.OrganizationBaseRestMapper
import com.cezarykluczynski.stapi.server.organization.mapper.OrganizationBaseSoapMapper
import com.cezarykluczynski.stapi.server.organization.mapper.OrganizationFullRestMapper
import com.cezarykluczynski.stapi.server.organization.mapper.OrganizationFullSoapMapper
import spock.lang.Specification

import javax.xml.ws.Endpoint

class OrganizationConfigurationTest extends Specification {

	private EndpointFactory endpointFactoryMock

	private OrganizationConfiguration organizationConfiguration

	void setup() {
		endpointFactoryMock = Mock(EndpointFactory)
		organizationConfiguration = new OrganizationConfiguration(endpointFactory: endpointFactoryMock)
	}

	void "Organization SOAP endpoint is created"() {
		given:
		Endpoint endpoint = Mock(Endpoint)

		when:
		Endpoint endpointOutput = organizationConfiguration.organizationEndpoint()

		then:
		1 * endpointFactoryMock.createSoapEndpoint(OrganizationSoapEndpoint, OrganizationSoapEndpoint.ADDRESS) >> endpoint
		0 * _
		endpointOutput == endpoint
	}

	void "OrganizationBaseSoapMapper is created"() {
		when:
		OrganizationBaseSoapMapper organizationBaseSoapMapper = organizationConfiguration.organizationBaseSoapMapper()

		then:
		organizationBaseSoapMapper != null
	}

	void "OrganizationFullSoapMapper is created"() {
		when:
		OrganizationFullSoapMapper organizationFullSoapMapper = organizationConfiguration.organizationFullSoapMapper()

		then:
		organizationFullSoapMapper != null
	}

	void "OrganizationBaseRestMapper is created"() {
		when:
		OrganizationBaseRestMapper organizationBaseRestMapper = organizationConfiguration.organizationBaseRestMapper()

		then:
		organizationBaseRestMapper != null
	}

	void "OrganizationFullRestMapper is created"() {
		when:
		OrganizationFullRestMapper organizationFullRestMapper = organizationConfiguration.organizationFullRestMapper()

		then:
		organizationFullRestMapper != null
	}

}