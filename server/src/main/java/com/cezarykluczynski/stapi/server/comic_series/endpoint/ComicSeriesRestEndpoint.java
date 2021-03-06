package com.cezarykluczynski.stapi.server.comic_series.endpoint;

import com.cezarykluczynski.stapi.client.v1.rest.model.ComicSeriesBaseResponse;
import com.cezarykluczynski.stapi.client.v1.rest.model.ComicSeriesFullResponse;
import com.cezarykluczynski.stapi.server.comic_series.dto.ComicSeriesRestBeanParams;
import com.cezarykluczynski.stapi.server.comic_series.reader.ComicSeriesRestReader;
import com.cezarykluczynski.stapi.server.common.dto.PageSortBeanParams;
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
public class ComicSeriesRestEndpoint {

	public static final String ADDRESS = "/v1/rest/comicSeries";

	private final ComicSeriesRestReader comicSeriesRestReader;

	@Inject
	public ComicSeriesRestEndpoint(ComicSeriesRestReader comicSeriesRestReader) {
		this.comicSeriesRestReader = comicSeriesRestReader;
	}

	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	public ComicSeriesFullResponse getComicSeries(@QueryParam("uid") String uid) {
		return comicSeriesRestReader.readFull(uid);
	}

	@GET
	@Path("search")
	@Consumes(MediaType.APPLICATION_JSON)
	public ComicSeriesBaseResponse searchComicSeries(@BeanParam PageSortBeanParams pageSortBeanParams) {
		return comicSeriesRestReader.readBase(ComicSeriesRestBeanParams.fromPageSortBeanParams(pageSortBeanParams));
	}

	@POST
	@Path("search")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public ComicSeriesBaseResponse searchComicSeries(@BeanParam ComicSeriesRestBeanParams comicSeriesRestBeanParams) {
		return comicSeriesRestReader.readBase(comicSeriesRestBeanParams);
	}

}
