package com.cezarykluczynski.stapi.etl.template.comicSeries.processor

import com.cezarykluczynski.stapi.etl.common.processor.CategoryTitlesExtractingProcessor
import com.cezarykluczynski.stapi.etl.util.constant.CategoryName
import com.cezarykluczynski.stapi.sources.mediawiki.dto.CategoryHeader
import com.cezarykluczynski.stapi.sources.mediawiki.dto.Page
import com.google.common.collect.Lists
import spock.lang.Specification

class ComicSeriesTemplatePhotonovelSeriesProcessorTest extends Specification {

	private CategoryTitlesExtractingProcessor categoryTitlesExtractingProcessorMock

	private ComicSeriesTemplatePhotonovelSeriesProcessor comicSeriesTemplatePhotonovelSeriesProcessor

	void setup() {
		categoryTitlesExtractingProcessorMock = Mock(CategoryTitlesExtractingProcessor)
		comicSeriesTemplatePhotonovelSeriesProcessor = new ComicSeriesTemplatePhotonovelSeriesProcessor(categoryTitlesExtractingProcessorMock)
	}

	void "returns true when Page has 'Photonovel series' category"() {
		given:
		CategoryHeader categoryHeader = Mock(CategoryHeader)
		List<CategoryHeader> categoryHeaderList = Lists.newArrayList(categoryHeader)
		Page page = new Page(categories: categoryHeaderList)

		when:
		Boolean photonovel = comicSeriesTemplatePhotonovelSeriesProcessor.process(page)

		then:
		1 * categoryTitlesExtractingProcessorMock.process(Lists.newArrayList(categoryHeader)) >> Lists.newArrayList(CategoryName.PHOTONOVEL_SERIES)
		0 * _
		photonovel
	}

	void "returns true when Page does not have 'Photonovel series' category"() {
		given:
		Page page = new Page()

		when:
		Boolean photonovel = comicSeriesTemplatePhotonovelSeriesProcessor.process(page)

		then:
		1 * categoryTitlesExtractingProcessorMock.process(_) >> Lists.newArrayList()
		0 * _
		!photonovel
	}
}