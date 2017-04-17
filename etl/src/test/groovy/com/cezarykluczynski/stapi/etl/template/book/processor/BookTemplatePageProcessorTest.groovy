package com.cezarykluczynski.stapi.etl.template.book.processor

import com.cezarykluczynski.stapi.etl.book.creation.service.BookPageFilter
import com.cezarykluczynski.stapi.etl.common.dto.EnrichablePair
import com.cezarykluczynski.stapi.etl.common.service.PageBindingService
import com.cezarykluczynski.stapi.etl.template.book.dto.BookTemplate
import com.cezarykluczynski.stapi.etl.template.service.TemplateFinder
import com.cezarykluczynski.stapi.model.page.entity.Page as ModelPage
import com.cezarykluczynski.stapi.sources.mediawiki.dto.CategoryHeader
import com.cezarykluczynski.stapi.sources.mediawiki.dto.Page
import com.cezarykluczynski.stapi.sources.mediawiki.dto.Template
import com.cezarykluczynski.stapi.util.constant.TemplateTitle
import org.assertj.core.util.Lists
import spock.lang.Specification

class BookTemplatePageProcessorTest extends Specification {

	private static final String TITLE = 'TITLE'

	private BookPageFilter bookPageFilterMock

	private PageBindingService pageBindingServiceMock

	private TemplateFinder templateFinderMock

	private CategoriesBookTemplateEnrichingProcessor categoriesBookTemplateEnrichingProcessorMock

	private BookTemplateCompositeEnrichingProcessor bookTemplateCompositeEnrichingProcessorMock

	private BookTemplatePageProcessor bookTemplatePageProcessor

	void setup() {
		bookPageFilterMock = Mock()
		pageBindingServiceMock = Mock()
		templateFinderMock = Mock()
		categoriesBookTemplateEnrichingProcessorMock = Mock()
		bookTemplateCompositeEnrichingProcessorMock = Mock()
		bookTemplatePageProcessor = new BookTemplatePageProcessor(bookPageFilterMock, pageBindingServiceMock, templateFinderMock,
				categoriesBookTemplateEnrichingProcessorMock, bookTemplateCompositeEnrichingProcessorMock)
	}

	void "returns null when BookPageFilter returns true"() {
		given:
		Page page = Mock()

		when:
		BookTemplate bookTemplate = bookTemplatePageProcessor.process(page)

		then:
		1 * bookPageFilterMock.shouldBeFilteredOut(page) >> true
		0 * _
		bookTemplate == null
	}

	void "parses page that does not have any book sidebar template"() {
		given:
		CategoryHeader categoryHeader = Mock()
		List<CategoryHeader> categoryHeaderList = Lists.newArrayList(categoryHeader)
		Page page = new Page(
				title: TITLE,
				categories: categoryHeaderList)
		ModelPage modelPage = new ModelPage()

		when:
		BookTemplate bookTemplate = bookTemplatePageProcessor.process(page)

		then:
		1 * bookPageFilterMock.shouldBeFilteredOut(page) >> false
		1 * pageBindingServiceMock.fromPageToPageEntity(page) >> modelPage
		1 * categoriesBookTemplateEnrichingProcessorMock.enrich(_ as EnrichablePair) >> {
				EnrichablePair<List<CategoryHeader>, BookTemplate> enrichablePair ->
			assert enrichablePair.input == categoryHeaderList
			assert enrichablePair.output != null
		}
		1 * templateFinderMock.findTemplate(page, TemplateTitle.SIDEBAR_NOVEL, TemplateTitle.SIDEBAR_REFERENCE_BOOK, TemplateTitle.SIDEBAR_RPG_BOOK,
				TemplateTitle.SIDEBAR_BIOGRAPHY_BOOK) >> Optional.empty()
		0 * _
		bookTemplate.title == TITLE
		bookTemplate.page == modelPage
	}

	void "parses page that do have a book sidebar template"() {
		given:
		CategoryHeader categoryHeader = Mock()
		List<CategoryHeader> categoryHeaderList = Lists.newArrayList(categoryHeader)
		Page page = new Page(
				title: TITLE,
				categories: categoryHeaderList)
		ModelPage modelPage = new ModelPage()
		Template sidebarBookTemplate = Mock()

		when:
		BookTemplate bookTemplate = bookTemplatePageProcessor.process(page)

		then:
		1 * bookPageFilterMock.shouldBeFilteredOut(page) >> false
		1 * pageBindingServiceMock.fromPageToPageEntity(page) >> modelPage
		1 * categoriesBookTemplateEnrichingProcessorMock.enrich(_ as EnrichablePair) >> {
				EnrichablePair<List<CategoryHeader>, BookTemplate> enrichablePair ->
			assert enrichablePair.input == categoryHeaderList
			assert enrichablePair.output != null
		}
		1 * templateFinderMock.findTemplate(page, TemplateTitle.SIDEBAR_NOVEL, TemplateTitle.SIDEBAR_REFERENCE_BOOK, TemplateTitle.SIDEBAR_RPG_BOOK,
				TemplateTitle.SIDEBAR_BIOGRAPHY_BOOK) >> Optional.of(sidebarBookTemplate)
		1 * bookTemplateCompositeEnrichingProcessorMock.enrich(_ as EnrichablePair) >> { EnrichablePair<Template, BookTemplate> enrichablePair ->
			assert enrichablePair.input == sidebarBookTemplate
			assert enrichablePair.output != null
		}
		0 * _
		bookTemplate.title == TITLE
		bookTemplate.page == modelPage
	}

}