package com.cezarykluczynski.stapi.server.species.endpoint;

import com.cezarykluczynski.stapi.client.v1.soap.SpeciesBaseRequest;
import com.cezarykluczynski.stapi.client.v1.soap.SpeciesBaseResponse;
import com.cezarykluczynski.stapi.client.v1.soap.SpeciesFullRequest;
import com.cezarykluczynski.stapi.client.v1.soap.SpeciesFullResponse;
import com.cezarykluczynski.stapi.client.v1.soap.SpeciesPortType;
import com.cezarykluczynski.stapi.server.species.reader.SpeciesSoapReader;

import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public class SpeciesSoapEndpoint implements SpeciesPortType {

	private SpeciesSoapReader speciesSoapReader;

	public SpeciesSoapEndpoint(SpeciesSoapReader speciesSoapReader) {
		this.speciesSoapReader = speciesSoapReader;
	}

	@Override
	public SpeciesBaseResponse getSpeciesBase(@WebParam(partName = "request", name = "SpeciesBaseRequest",
			targetNamespace = "http://stapi.co/api/v1/soap/species") SpeciesBaseRequest request) {
		return speciesSoapReader.readBase(request);
	}

	@Override
	public SpeciesFullResponse getSpeciesFull(@WebParam(partName = "request", name = "SpeciesFullRequest",
			targetNamespace = "http://stapi.co/api/v1/soap/species") SpeciesFullRequest request) {
		return speciesSoapReader.readFull(request);
	}

}
