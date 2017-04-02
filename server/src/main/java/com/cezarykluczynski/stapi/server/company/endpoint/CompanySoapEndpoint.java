package com.cezarykluczynski.stapi.server.company.endpoint;

import com.cezarykluczynski.stapi.client.v1.soap.CompanyBaseRequest;
import com.cezarykluczynski.stapi.client.v1.soap.CompanyBaseResponse;
import com.cezarykluczynski.stapi.client.v1.soap.CompanyFullRequest;
import com.cezarykluczynski.stapi.client.v1.soap.CompanyFullResponse;
import com.cezarykluczynski.stapi.client.v1.soap.CompanyPortType;
import com.cezarykluczynski.stapi.server.company.reader.CompanySoapReader;

import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public class CompanySoapEndpoint implements CompanyPortType {

	private CompanySoapReader seriesSoapReader;

	public CompanySoapEndpoint(CompanySoapReader seriesSoapReader) {
		this.seriesSoapReader = seriesSoapReader;
	}

	@Override
	public CompanyBaseResponse getCompanyBase(@WebParam(partName = "request", name = "CompanyBaseRequest",
			targetNamespace = "http://stapi.co/api/v1/soap/company") CompanyBaseRequest request) {
		return seriesSoapReader.readBase(request);
	}

	@Override
	public CompanyFullResponse getCompanyFull(@WebParam(partName = "request", name = "CompanyFullRequest",
			targetNamespace = "http://stapi.co/api/v1/soap/company") CompanyFullRequest request) {
		return seriesSoapReader.readFull(request);
	}

}
