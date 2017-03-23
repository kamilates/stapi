package com.cezarykluczynski.stapi.server.comicCollection.query

import com.cezarykluczynski.stapi.client.v1.soap.ComicCollectionBaseRequest
import com.cezarykluczynski.stapi.client.v1.soap.ComicCollectionFullRequest
import com.cezarykluczynski.stapi.client.v1.soap.RequestPage
import com.cezarykluczynski.stapi.model.comicCollection.dto.ComicCollectionRequestDTO
import com.cezarykluczynski.stapi.model.comicCollection.repository.ComicCollectionRepository
import com.cezarykluczynski.stapi.server.comicCollection.mapper.ComicCollectionBaseSoapMapper
import com.cezarykluczynski.stapi.server.comicCollection.mapper.ComicCollectionFullSoapMapper
import com.cezarykluczynski.stapi.server.common.mapper.PageMapper
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import spock.lang.Specification

class ComicCollectionSoapQueryTest extends Specification {

	private ComicCollectionBaseSoapMapper comicCollectionBaseSoapMapperMock

	private ComicCollectionFullSoapMapper comicCollectionFullSoapMapperMock

	private PageMapper pageMapperMock

	private ComicCollectionRepository comicCollectionRepositoryMock

	private ComicCollectionSoapQuery comicCollectionSoapQuery

	void setup() {
		comicCollectionBaseSoapMapperMock = Mock(ComicCollectionBaseSoapMapper)
		comicCollectionFullSoapMapperMock = Mock(ComicCollectionFullSoapMapper)
		pageMapperMock = Mock(PageMapper)
		comicCollectionRepositoryMock = Mock(ComicCollectionRepository)
		comicCollectionSoapQuery = new ComicCollectionSoapQuery(comicCollectionBaseSoapMapperMock, comicCollectionFullSoapMapperMock, pageMapperMock,
				comicCollectionRepositoryMock)
	}

	void "maps ComicCollectionBaseRequest to ComicCollectionRequestDTO and to PageRequest, then calls repository, then returns result"() {
		given:
		RequestPage requestPage = Mock(RequestPage)
		PageRequest pageRequest = Mock(PageRequest)
		ComicCollectionBaseRequest comicCollectionRequest = Mock(ComicCollectionBaseRequest)
		comicCollectionRequest.page >> requestPage
		ComicCollectionRequestDTO comicCollectionRequestDTO = Mock(ComicCollectionRequestDTO)
		Page page = Mock(Page)

		when:
		Page pageOutput = comicCollectionSoapQuery.query(comicCollectionRequest)

		then:
		1 * comicCollectionBaseSoapMapperMock.mapBase(comicCollectionRequest) >> comicCollectionRequestDTO
		1 * pageMapperMock.fromRequestPageToPageRequest(requestPage) >> pageRequest
		1 * comicCollectionRepositoryMock.findMatching(comicCollectionRequestDTO, pageRequest) >> page
		pageOutput == page
	}

	void "maps ComicCollectionFullRequest to ComicCollectionRequestDTO, then calls repository, then returns result"() {
		given:
		PageRequest pageRequest = Mock(PageRequest)
		ComicCollectionFullRequest comicCollectionRequest = Mock(ComicCollectionFullRequest)
		ComicCollectionRequestDTO comicCollectionRequestDTO = Mock(ComicCollectionRequestDTO)
		Page page = Mock(Page)

		when:
		Page pageOutput = comicCollectionSoapQuery.query(comicCollectionRequest)

		then:
		1 * comicCollectionFullSoapMapperMock.mapFull(comicCollectionRequest) >> comicCollectionRequestDTO
		1 * pageMapperMock.defaultPageRequest >> pageRequest
		1 * comicCollectionRepositoryMock.findMatching(comicCollectionRequestDTO, pageRequest) >> page
		pageOutput == page
	}

}