package com.cezarykluczynski.stapi.etl.template.video_game.processor

import com.cezarykluczynski.stapi.etl.common.dto.EnrichablePair
import com.cezarykluczynski.stapi.etl.common.processor.company.WikitextToCompaniesProcessor
import com.cezarykluczynski.stapi.etl.reference.processor.ReferencesFromTemplatePartProcessor
import com.cezarykluczynski.stapi.etl.template.common.processor.ContentRatingsProcessor
import com.cezarykluczynski.stapi.etl.template.video_game.dto.VideoGameTemplate
import com.cezarykluczynski.stapi.etl.template.video_game.dto.VideoGameTemplateParameter
import com.cezarykluczynski.stapi.model.company.entity.Company
import com.cezarykluczynski.stapi.model.content_rating.entity.ContentRating
import com.cezarykluczynski.stapi.model.reference.entity.Reference
import com.cezarykluczynski.stapi.sources.mediawiki.dto.Template
import com.google.common.collect.Lists
import com.google.common.collect.Sets
import spock.lang.Specification

class VideoGameRelationsEnrichingProcessorTest extends Specification {

	private static final String PUBLISHER = 'PUBLISHER'
	private static final String DEVELOPER = 'DEVELOPER'

	private WikitextToCompaniesProcessor wikitextToCompaniesProcessorMock

	private ContentRatingsProcessor contentRatingsProcessorMock

	private ReferencesFromTemplatePartProcessor referencesFromTemplatePartProcessorMock

	private VideoGameTemplateRelationsEnrichingProcessor videoGameTemplateRelationsEnrichingProcessor

	void setup() {
		wikitextToCompaniesProcessorMock = Mock()
		contentRatingsProcessorMock = Mock()
		referencesFromTemplatePartProcessorMock = Mock()
		videoGameTemplateRelationsEnrichingProcessor = new VideoGameTemplateRelationsEnrichingProcessor(wikitextToCompaniesProcessorMock,
				contentRatingsProcessorMock, referencesFromTemplatePartProcessorMock)
	}

	void "when publisher part is found, WikitextToCompaniesProcessor is used to process it"() {
		given:
		Template.Part templatePart = new Template.Part(
				key: VideoGameTemplateParameter.PUBLISHER,
				value: PUBLISHER)
		Template sidebarVideoGameTemplate = new Template(parts: Lists.newArrayList(templatePart))
		Company company1 = Mock()
		Company company2 = Mock()
		VideoGameTemplate videoGameTemplate = new VideoGameTemplate()

		when:
		videoGameTemplateRelationsEnrichingProcessor.enrich(EnrichablePair.of(sidebarVideoGameTemplate, videoGameTemplate))

		then:
		1 * wikitextToCompaniesProcessorMock.process(PUBLISHER) >> Sets.newHashSet(company1, company2)
		0 * _
		videoGameTemplate.publishers.contains company1
		videoGameTemplate.publishers.contains company2
	}

	void "when developer part is found, WikitextToCompaniesProcessor is used to process it"() {
		given:
		Template.Part templatePart = new Template.Part(
				key: VideoGameTemplateParameter.DEVELOPER,
				value: DEVELOPER)
		Template sidebarVideoGameTemplate = new Template(parts: Lists.newArrayList(templatePart))
		Company company1 = Mock()
		Company company2 = Mock()
		VideoGameTemplate videoGameTemplate = new VideoGameTemplate()

		when:
		videoGameTemplateRelationsEnrichingProcessor.enrich(EnrichablePair.of(sidebarVideoGameTemplate, videoGameTemplate))

		then:
		1 * wikitextToCompaniesProcessorMock.process(DEVELOPER) >> Sets.newHashSet(company1, company2)
		0 * _
		videoGameTemplate.developers.contains company1
		videoGameTemplate.developers.contains company2
	}

	void "when rating part is found, ContentRatingProcessor is used to process it"() {
		given:
		Template.Part templatePart = new Template.Part(key: VideoGameTemplateParameter.RATING)
		Template sidebarVideoGameTemplate = new Template(parts: Lists.newArrayList(templatePart))
		ContentRating contentRating1 = Mock()
		ContentRating contentRating2 = Mock()
		VideoGameTemplate videoGameTemplate = new VideoGameTemplate()

		when:
		videoGameTemplateRelationsEnrichingProcessor.enrich(EnrichablePair.of(sidebarVideoGameTemplate, videoGameTemplate))

		then:
		1 * contentRatingsProcessorMock.process(templatePart) >> Sets.newHashSet(contentRating1, contentRating2)
		0 * _
		videoGameTemplate.ratings.contains contentRating1
		videoGameTemplate.ratings.contains contentRating2
	}

	void "when reference part is found, ReferencesFromTemplatePartProcessor is used to process it"() {
		given:
		Template.Part templatePart = new Template.Part(key: VideoGameTemplateParameter.REFERENCE)
		Template sidebarVideoGameTemplate = new Template(parts: Lists.newArrayList(templatePart))
		Reference reference1 = Mock()
		Reference reference2 = Mock()
		VideoGameTemplate videoGameTemplate = new VideoGameTemplate()

		when:
		videoGameTemplateRelationsEnrichingProcessor.enrich(EnrichablePair.of(sidebarVideoGameTemplate, videoGameTemplate))

		then:
		1 * referencesFromTemplatePartProcessorMock.process(templatePart) >> Sets.newHashSet(reference1, reference2)
		0 * _
		videoGameTemplate.references.contains reference1
		videoGameTemplate.references.contains reference2
	}

}
