package com.cezarykluczynski.stapi.etl.template.video.processor

import com.cezarykluczynski.stapi.etl.common.dto.EnrichablePair
import com.cezarykluczynski.stapi.etl.template.video.dto.VideoTemplate
import com.cezarykluczynski.stapi.model.video_release.entity.enums.VideoReleaseFormat
import com.cezarykluczynski.stapi.sources.mediawiki.dto.CategoryHeader
import com.cezarykluczynski.stapi.sources.mediawiki.dto.Page
import com.google.common.collect.Lists
import spock.lang.Specification

class VideoTemplateFormatEnrichingProcessorTest extends Specification {

	private static final VideoReleaseFormat FORMAT_VHS = VideoReleaseFormat.VHS
	private static final VideoReleaseFormat FORMAT_DVD = VideoReleaseFormat.DVD
	private static final VideoReleaseFormat FORMAT_UMD = VideoReleaseFormat.UMD

	private VideoReleaseFormatFromCategoryLinkProcessor videoReleaseFormatFromCategoryLinkProcessorMock

	private VideoTemplateFormatEnrichingProcessor videoTemplateFormatEnrichingProcessor

	void setup() {
		videoReleaseFormatFromCategoryLinkProcessorMock = Mock()
		videoTemplateFormatEnrichingProcessor = new VideoTemplateFormatEnrichingProcessor(videoReleaseFormatFromCategoryLinkProcessorMock)
	}

	void "when VideoReleaseFormatFromCategoryLinkProcessor returns null, nothing happens"() {
		given:
		Page page = new Page()
		List<CategoryHeader> categoryHeaderList = Lists.newArrayList()
		VideoTemplate videoTemplate = new VideoTemplate()

		when:
		videoTemplateFormatEnrichingProcessor.enrich(EnrichablePair.of(page, videoTemplate))

		then:
		1 * videoReleaseFormatFromCategoryLinkProcessorMock.process(categoryHeaderList) >> null
		0 * _
	}

	void "when format is found from categories, it is used"() {
		given:
		Page page = new Page(categories: Lists.newArrayList())
		VideoTemplate videoTemplate = new VideoTemplate()

		when:
		videoTemplateFormatEnrichingProcessor.enrich(EnrichablePair.of(page, videoTemplate))

		then:
		1 * videoReleaseFormatFromCategoryLinkProcessorMock.process(Lists.newArrayList()) >> FORMAT_VHS
		0 * _
		videoTemplate.format == FORMAT_VHS
	}

	void "when format is found from categories, but different format is already present, the original format is kept"() {
		given:
		Page page = new Page(categories: Lists.newArrayList())
		VideoTemplate videoTemplate = new VideoTemplate(format: FORMAT_DVD)

		when:
		videoTemplateFormatEnrichingProcessor.enrich(EnrichablePair.of(page, videoTemplate))

		then:
		1 * videoReleaseFormatFromCategoryLinkProcessorMock.process(Lists.newArrayList()) >> FORMAT_VHS
		0 * _
		videoTemplate.format == FORMAT_DVD
	}

	void "when title contains ' (UMD)', UMD format is setrr, regardless of current format"() {
		given:
		Page page = new Page(
				title: 'Star Trek Nemesis (UMD)',
				categories: Lists.newArrayList())
		VideoTemplate videoTemplate = new VideoTemplate(format: FORMAT_DVD)

		when:
		videoTemplateFormatEnrichingProcessor.enrich(EnrichablePair.of(page, videoTemplate))

		then:
		1 * videoReleaseFormatFromCategoryLinkProcessorMock.process(Lists.newArrayList()) >> null
		0 * _
		videoTemplate.format == FORMAT_UMD
	}

}
