package com.cezarykluczynski.stapi.etl.common.configuration

import com.cezarykluczynski.stapi.sources.mediawiki.dto.PageHeader
import com.cezarykluczynski.stapi.sources.wordpress.dto.Page
import com.google.common.collect.Lists
import org.springframework.batch.item.support.ListItemReader
import spock.lang.Specification

abstract class AbstractCreationConfigurationTest extends Specification {

	protected static List<PageHeader> createListWithPageHeaderTitle(String title) {
		Lists.newArrayList(PageHeader.builder().title(title).build())
	}

	protected static List<Page> createListWithPageRenderedTitle(String title) {
		Lists.newArrayList(new Page(renderedTitle: title))
	}

	protected static List<String> pageHeaderReaderToList(ListItemReader<PageHeader> listItemReader) {
		List<String> pageHeaderList = Lists.newArrayList()

		while (true) {
			PageHeader pageHeader = listItemReader.read()

			if (pageHeader == null) {
				break
			}

			pageHeaderList.add pageHeader.title
		}

		pageHeaderList
	}

	protected static List<String> pageReaderToList(ListItemReader<Page> listItemReader) {
		List<String> pageHeaderList = Lists.newArrayList()

		while (true) {
			Page page = listItemReader.read()

			if (page == null) {
				break
			}

			pageHeaderList.add page.renderedTitle
		}

		pageHeaderList
	}

}
