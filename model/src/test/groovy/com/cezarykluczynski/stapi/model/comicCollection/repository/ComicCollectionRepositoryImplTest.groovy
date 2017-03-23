package com.cezarykluczynski.stapi.model.comicCollection.repository

import com.cezarykluczynski.stapi.model.character.entity.Character
import com.cezarykluczynski.stapi.model.comicCollection.dto.ComicCollectionRequestDTO
import com.cezarykluczynski.stapi.model.comicCollection.entity.ComicCollection
import com.cezarykluczynski.stapi.model.comicCollection.entity.ComicCollection_
import com.cezarykluczynski.stapi.model.comicCollection.query.ComicCollectionInitialQueryBuilderFactory
import com.cezarykluczynski.stapi.model.comicSeries.entity.ComicSeries
import com.cezarykluczynski.stapi.model.comics.entity.Comics
import com.cezarykluczynski.stapi.model.common.query.QueryBuilder
import com.cezarykluczynski.stapi.model.company.entity.Company
import com.cezarykluczynski.stapi.model.reference.entity.Reference
import com.cezarykluczynski.stapi.model.staff.entity.Staff
import com.google.common.collect.Lists
import com.google.common.collect.Sets
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import spock.lang.Specification

class ComicCollectionRepositoryImplTest extends Specification {

	private static final String GUID = 'ABCD0123456789'

	private ComicCollectionInitialQueryBuilderFactory comicCollectionInitialQueryBuilderFactory

	private ComicCollectionRepositoryImpl comicCollectionRepositoryImpl

	private QueryBuilder<ComicCollection> comicCollectionQueryBuilder

	private QueryBuilder<ComicCollection> comicCollectionComicSeriesPublishersComicsQueryBuilder

	private QueryBuilder<ComicCollection> comicCollectionCharactersReferencesQueryBuilder

	private QueryBuilder<ComicCollection> comicCollectionStaffQueryBuilder

	private Pageable pageable

	private ComicCollectionRequestDTO comicCollectionRequestDTO

	private ComicCollection comicCollection

	private ComicCollection comicSeriesPerformersComicCollection

	private ComicCollection charactersReferencesComicCollection

	private ComicCollection staffComicCollection

	private Page page

	private Page performersPage

	private Page charactersPage

	private Set<ComicSeries> comicSeriesSet

	private Set<Company> publishersSet

	private Set<Comics> comicsSet

	private Set<Character> charactersSet

	private Set<Reference> referencesSet

	private Set<Staff> writersSet

	private Set<Staff> artistsSet

	private Set<Staff> editorsSet

	private Set<Staff> staffSet

	void setup() {
		comicCollectionInitialQueryBuilderFactory = Mock(ComicCollectionInitialQueryBuilderFactory)
		comicCollectionRepositoryImpl = new ComicCollectionRepositoryImpl(comicCollectionInitialQueryBuilderFactory)
		comicCollectionQueryBuilder = Mock(QueryBuilder)
		comicCollectionComicSeriesPublishersComicsQueryBuilder = Mock(QueryBuilder)
		comicCollectionCharactersReferencesQueryBuilder = Mock(QueryBuilder)
		comicCollectionStaffQueryBuilder = Mock(QueryBuilder)
		pageable = Mock(Pageable)
		comicCollectionRequestDTO = Mock(ComicCollectionRequestDTO)
		page = Mock(Page)
		performersPage = Mock(Page)
		charactersPage = Mock(Page)
		comicCollection = Mock(ComicCollection)
		comicSeriesPerformersComicCollection = Mock(ComicCollection)
		charactersReferencesComicCollection = Mock(ComicCollection)
		staffComicCollection = Mock(ComicCollection)
		comicSeriesSet = Mock(Set)
		publishersSet = Mock(Set)
		comicsSet = Mock(Set)
		charactersSet = Mock(Set)
		referencesSet = Mock(Set)
		writersSet = Mock(Set)
		artistsSet = Mock(Set)
		editorsSet = Mock(Set)
		staffSet = Mock(Set)
	}

	void "query is built and performed"() {
		when:
		Page pageOutput = comicCollectionRepositoryImpl.findMatching(comicCollectionRequestDTO, pageable)

		then: 'criteria builder is retrieved'
		1 * comicCollectionInitialQueryBuilderFactory.createInitialQueryBuilder(comicCollectionRequestDTO, pageable) >>
				comicCollectionQueryBuilder

		then: 'guid is retrieved, and it is not null'
		1 * comicCollectionRequestDTO.guid >> GUID

		then: 'staff fetch is performed'
		1 * comicCollectionQueryBuilder.fetch(ComicCollection_.comicSeries)
		1 * comicCollectionQueryBuilder.fetch(ComicCollection_.writers)
		1 * comicCollectionQueryBuilder.fetch(ComicCollection_.artists)
		1 * comicCollectionQueryBuilder.fetch(ComicCollection_.editors)
		1 * comicCollectionQueryBuilder.fetch(ComicCollection_.staff)
		1 * comicCollectionQueryBuilder.fetch(ComicCollection_.publishers)
		1 * comicCollectionQueryBuilder.fetch(ComicCollection_.characters)
		1 * comicCollectionQueryBuilder.fetch(ComicCollection_.references)
		1 * comicCollectionQueryBuilder.fetch(ComicCollection_.comics)

		then: 'page is retrieved'
		1 * comicCollectionQueryBuilder.findPage() >> page
		1 * page.content >> Lists.newArrayList(comicCollection)

		then: 'another criteria builder is retrieved for comic series, publishers, and comics'
		1 * comicCollectionInitialQueryBuilderFactory.createInitialQueryBuilder(comicCollectionRequestDTO, pageable) >>
				comicCollectionComicSeriesPublishersComicsQueryBuilder

		then: 'comic series and publishers fetch is performed'
		1 * comicCollectionComicSeriesPublishersComicsQueryBuilder.fetch(ComicCollection_.comicSeries)
		1 * comicCollectionComicSeriesPublishersComicsQueryBuilder.fetch(ComicCollection_.publishers)
		1 * comicCollectionComicSeriesPublishersComicsQueryBuilder.fetch(ComicCollection_.comics)

		then: 'comic series and publishers list is retrieved'
		1 * comicCollectionComicSeriesPublishersComicsQueryBuilder.findAll() >> Lists.newArrayList(comicSeriesPerformersComicCollection)

		then: 'comic series and publishers are set to comicCollection'
		1 * comicSeriesPerformersComicCollection.comicSeries >> comicSeriesSet
		1 * comicCollection.setComicSeries(comicSeriesSet)
		1 * comicSeriesPerformersComicCollection.publishers >> publishersSet
		1 * comicCollection.setPublishers(publishersSet)
		1 * comicSeriesPerformersComicCollection.comics >> comicsSet
		1 * comicCollection.setComics(comicsSet)

		then: 'another criteria builder is retrieved for characters and references'
		1 * comicCollectionInitialQueryBuilderFactory.createInitialQueryBuilder(comicCollectionRequestDTO, pageable) >>
				comicCollectionCharactersReferencesQueryBuilder

		then: 'characters and references fetch is performed'
		1 * comicCollectionCharactersReferencesQueryBuilder.fetch(ComicCollection_.characters)
		1 * comicCollectionCharactersReferencesQueryBuilder.fetch(ComicCollection_.references)

		then: 'characters and references list is retrieved'
		1 * comicCollectionCharactersReferencesQueryBuilder.findAll() >> Lists.newArrayList(charactersReferencesComicCollection)

		then: 'characters and references are set to comicCollection'
		1 * charactersReferencesComicCollection.characters >> charactersSet
		1 * comicCollection.setCharacters(charactersSet)
		1 * charactersReferencesComicCollection.references >> referencesSet
		1 * comicCollection.setReferences(referencesSet)

		then: 'another criteria builder is retrieved for staff'
		1 * comicCollectionInitialQueryBuilderFactory.createInitialQueryBuilder(comicCollectionRequestDTO, pageable) >>
				comicCollectionStaffQueryBuilder

		then: 'staff fetch is performed'
		1 * comicCollectionStaffQueryBuilder.fetch(ComicCollection_.writers)
		1 * comicCollectionStaffQueryBuilder.fetch(ComicCollection_.artists)
		1 * comicCollectionStaffQueryBuilder.fetch(ComicCollection_.editors)
		1 * comicCollectionStaffQueryBuilder.fetch(ComicCollection_.staff)

		then: 'staff list is retrieved'
		1 * comicCollectionStaffQueryBuilder.findAll() >> Lists.newArrayList(staffComicCollection)

		then: 'staff is set to comicCollection'
		1 * staffComicCollection.writers >> writersSet
		1 * comicCollection.setWriters(writersSet)
		1 * staffComicCollection.artists >> artistsSet
		1 * comicCollection.setArtists(artistsSet)
		1 * staffComicCollection.editors >> editorsSet
		1 * comicCollection.setEditors(editorsSet)
		1 * staffComicCollection.staff >> staffSet
		1 * comicCollection.setStaff(staffSet)

		then: 'page is returned'
		pageOutput == page

		then: 'no other interactions are expected'
		0 * _
	}

	void "query is built and performed without results from additional queries"() {
		when:
		Page pageOutput = comicCollectionRepositoryImpl.findMatching(comicCollectionRequestDTO, pageable)

		then: 'criteria builder is retrieved'
		1 * comicCollectionInitialQueryBuilderFactory.createInitialQueryBuilder(comicCollectionRequestDTO, pageable) >>
				comicCollectionQueryBuilder

		then: 'guid is retrieved, and it is not null'
		1 * comicCollectionRequestDTO.guid >> GUID

		then: 'staff fetch is performed'
		1 * comicCollectionQueryBuilder.fetch(ComicCollection_.comicSeries)
		1 * comicCollectionQueryBuilder.fetch(ComicCollection_.writers)
		1 * comicCollectionQueryBuilder.fetch(ComicCollection_.artists)
		1 * comicCollectionQueryBuilder.fetch(ComicCollection_.editors)
		1 * comicCollectionQueryBuilder.fetch(ComicCollection_.staff)
		1 * comicCollectionQueryBuilder.fetch(ComicCollection_.publishers)
		1 * comicCollectionQueryBuilder.fetch(ComicCollection_.characters)
		1 * comicCollectionQueryBuilder.fetch(ComicCollection_.references)
		1 * comicCollectionQueryBuilder.fetch(ComicCollection_.comics)

		then: 'page is retrieved'
		1 * comicCollectionQueryBuilder.findPage() >> page
		1 * page.content >> Lists.newArrayList(comicCollection)

		then: 'another criteria builder is retrieved for comic series and publishers'
		1 * comicCollectionInitialQueryBuilderFactory.createInitialQueryBuilder(comicCollectionRequestDTO, pageable) >>
				comicCollectionComicSeriesPublishersComicsQueryBuilder

		then: 'comic series and publishers fetch is performed'
		1 * comicCollectionComicSeriesPublishersComicsQueryBuilder.fetch(ComicCollection_.comicSeries)
		1 * comicCollectionComicSeriesPublishersComicsQueryBuilder.fetch(ComicCollection_.publishers)
		1 * comicCollectionComicSeriesPublishersComicsQueryBuilder.fetch(ComicCollection_.comics)

		then: 'empty comic series and publishers list is retrieved'
		1 * comicCollectionComicSeriesPublishersComicsQueryBuilder.findAll() >> Lists.newArrayList()

		then: 'another criteria builder is retrieved for characters and references'
		1 * comicCollectionInitialQueryBuilderFactory.createInitialQueryBuilder(comicCollectionRequestDTO, pageable) >>
				comicCollectionCharactersReferencesQueryBuilder

		then: 'characters and references fetch is performed'
		1 * comicCollectionCharactersReferencesQueryBuilder.fetch(ComicCollection_.characters)
		1 * comicCollectionCharactersReferencesQueryBuilder.fetch(ComicCollection_.references)

		then: 'empty characters and references list is retrieved'
		1 * comicCollectionCharactersReferencesQueryBuilder.findAll() >> Lists.newArrayList()

		then: 'another criteria builder is retrieved for staff'
		1 * comicCollectionInitialQueryBuilderFactory.createInitialQueryBuilder(comicCollectionRequestDTO, pageable) >>
				comicCollectionStaffQueryBuilder

		then: 'staff fetch is performed'
		1 * comicCollectionStaffQueryBuilder.fetch(ComicCollection_.writers)
		1 * comicCollectionStaffQueryBuilder.fetch(ComicCollection_.artists)
		1 * comicCollectionStaffQueryBuilder.fetch(ComicCollection_.editors)
		1 * comicCollectionStaffQueryBuilder.fetch(ComicCollection_.staff)

		then: 'empty staff list is retrieved'
		1 * comicCollectionStaffQueryBuilder.findAll() >> Lists.newArrayList()

		then: 'page is returned'
		pageOutput == page

		then: 'no other interactions are expected'
		0 * _
	}

	void "empty page is returned"() {
		when:
		Page pageOutput = comicCollectionRepositoryImpl.findMatching(comicCollectionRequestDTO, pageable)

		then: 'criteria builder is retrieved'
		1 * comicCollectionInitialQueryBuilderFactory.createInitialQueryBuilder(comicCollectionRequestDTO, pageable) >>
				comicCollectionQueryBuilder

		then: 'guid is retrieved, and it is not null'
		1 * comicCollectionRequestDTO.guid >> GUID

		then: 'staff fetch is performed'
		1 * comicCollectionQueryBuilder.fetch(ComicCollection_.comicSeries)
		1 * comicCollectionQueryBuilder.fetch(ComicCollection_.writers)
		1 * comicCollectionQueryBuilder.fetch(ComicCollection_.artists)
		1 * comicCollectionQueryBuilder.fetch(ComicCollection_.editors)
		1 * comicCollectionQueryBuilder.fetch(ComicCollection_.staff)
		1 * comicCollectionQueryBuilder.fetch(ComicCollection_.publishers)
		1 * comicCollectionQueryBuilder.fetch(ComicCollection_.characters)
		1 * comicCollectionQueryBuilder.fetch(ComicCollection_.references)
		1 * comicCollectionQueryBuilder.fetch(ComicCollection_.comics)

		then: 'page is retrieved'
		1 * comicCollectionQueryBuilder.findPage() >> page
		1 * page.content >> Lists.newArrayList()

		then: 'page is returned'
		pageOutput == page

		then: 'no other interactions are expected'
		0 * _
	}

	void "proxies are cleared when no related entities should be fetched"() {
		when:
		Page pageOutput = comicCollectionRepositoryImpl.findMatching(comicCollectionRequestDTO, pageable)

		then: 'criteria builder is retrieved'
		1 * comicCollectionInitialQueryBuilderFactory.createInitialQueryBuilder(comicCollectionRequestDTO, pageable) >> comicCollectionQueryBuilder

		then: 'guid criteria is set to null'
		1 * comicCollectionRequestDTO.guid >> null

		then: 'page is searched for and returned'
		1 * comicCollectionQueryBuilder.findPage() >> page

		then: 'proxies are cleared'
		1 * page.content >> Lists.newArrayList(comicCollection)
		comicCollection.setComicSeries(Sets.newHashSet())
		comicCollection.setWriters(Sets.newHashSet())
		comicCollection.setArtists(Sets.newHashSet())
		comicCollection.setEditors(Sets.newHashSet())
		comicCollection.setStaff(Sets.newHashSet())
		comicCollection.setPublishers(Sets.newHashSet())
		comicCollection.setCharacters(Sets.newHashSet())
		comicCollection.setReferences(Sets.newHashSet())
		comicCollection.setComics(Sets.newHashSet())
		pageOutput == page
	}

}