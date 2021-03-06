package com.cezarykluczynski.stapi.server.season.endpoint;

import com.cezarykluczynski.stapi.client.v1.rest.model.SeasonBaseResponse;
import com.cezarykluczynski.stapi.client.v1.rest.model.SeasonFullResponse;
import com.cezarykluczynski.stapi.server.common.dto.PageSortBeanParams;
import com.cezarykluczynski.stapi.server.season.dto.SeasonRestBeanParams;
import com.cezarykluczynski.stapi.server.season.reader.SeasonRestReader;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Service
@Produces(MediaType.APPLICATION_JSON)
public class SeasonRestEndpoint {

	public static final String ADDRESS = "/v1/rest/season";

	private final SeasonRestReader seasonRestReader;

	@Inject
	public SeasonRestEndpoint(SeasonRestReader seasonRestReader) {
		this.seasonRestReader = seasonRestReader;
	}

	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	public SeasonFullResponse getSeason(@QueryParam("uid") String uid) {
		return seasonRestReader.readFull(uid);
	}

	@GET
	@Path("search")
	@Consumes(MediaType.APPLICATION_JSON)
	public SeasonBaseResponse searchCompanies(@BeanParam PageSortBeanParams pageSortBeanParams) {
		return seasonRestReader.readBase(SeasonRestBeanParams.fromPageSortBeanParams(pageSortBeanParams));
	}

	@POST
	@Path("search")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public SeasonBaseResponse searchCompanies(@BeanParam SeasonRestBeanParams seriesRestBeanParams) {
		return seasonRestReader.readBase(seriesRestBeanParams);
	}

}
