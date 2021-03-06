package com.cezarykluczynski.stapi.etl.template.magazine_series.processor;

import com.cezarykluczynski.stapi.etl.common.dto.EnrichablePair;
import com.cezarykluczynski.stapi.etl.common.dto.FixedValueHolder;
import com.cezarykluczynski.stapi.etl.common.processor.ItemEnrichingProcessor;
import com.cezarykluczynski.stapi.etl.common.processor.WikitextStaffProcessor;
import com.cezarykluczynski.stapi.etl.template.common.dto.datetime.MonthYear;
import com.cezarykluczynski.stapi.etl.template.common.dto.datetime.MonthYearRange;
import com.cezarykluczynski.stapi.etl.template.common.processor.NumberOfPartsProcessor;
import com.cezarykluczynski.stapi.etl.template.magazine_series.dto.MagazineSeriesTemplate;
import com.cezarykluczynski.stapi.etl.template.magazine_series.dto.MagazineSeriesTemplateParameter;
import com.cezarykluczynski.stapi.etl.template.publishable_series.processor.PublishableSeriesTemplatePartsEnrichingProcessor;
import com.cezarykluczynski.stapi.sources.mediawiki.dto.Template;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service
public class MagazineSeriesTemplatePartsEnrichingProcessor
		implements ItemEnrichingProcessor<EnrichablePair<List<Template.Part>, MagazineSeriesTemplate>> {

	private final PublishableSeriesTemplatePartsEnrichingProcessor publishableSeriesTemplatePartsEnrichingProcessor;

	private final NumberOfPartsProcessor numberOfPartsProcessor;

	private final WikitextStaffProcessor wikitextStaffProcessor;

	private final MagazineSeriesPublicationDatesFixedValueProvider magazineSeriesPublicationDatesFixedValueProvider;

	private final MagazineSeriesNumberOfIssuesFixedValueProvider magazineSeriesNumberOfIssuesFixedValueProvider;

	@Inject
	public MagazineSeriesTemplatePartsEnrichingProcessor(PublishableSeriesTemplatePartsEnrichingProcessor
			publishableSeriesTemplatePartsEnrichingProcessor, NumberOfPartsProcessor numberOfPartsProcessor,
			WikitextStaffProcessor wikitextStaffProcessor,
			MagazineSeriesPublicationDatesFixedValueProvider magazineSeriesPublicationDatesFixedValueProvider,
			MagazineSeriesNumberOfIssuesFixedValueProvider magazineSeriesNumberOfIssuesFixedValueProvider) {
		this.publishableSeriesTemplatePartsEnrichingProcessor = publishableSeriesTemplatePartsEnrichingProcessor;
		this.numberOfPartsProcessor = numberOfPartsProcessor;
		this.wikitextStaffProcessor = wikitextStaffProcessor;
		this.magazineSeriesPublicationDatesFixedValueProvider = magazineSeriesPublicationDatesFixedValueProvider;
		this.magazineSeriesNumberOfIssuesFixedValueProvider = magazineSeriesNumberOfIssuesFixedValueProvider;
	}

	@Override
	public void enrich(EnrichablePair<List<Template.Part>, MagazineSeriesTemplate> enrichablePair) throws Exception {
		publishableSeriesTemplatePartsEnrichingProcessor.enrich(EnrichablePair.of(enrichablePair.getInput(), enrichablePair.getOutput()));

		MagazineSeriesTemplate magazineSeriesTemplate = enrichablePair.getOutput();

		for (Template.Part part : enrichablePair.getInput()) {
			String key = part.getKey();
			String value = part.getValue();

			switch (key) {
				case MagazineSeriesTemplateParameter.ISSUES:
					if (magazineSeriesTemplate.getNumberOfIssues() == null) {
						magazineSeriesTemplate.setNumberOfIssues(numberOfPartsProcessor.process(value));
					}
					break;
				case MagazineSeriesTemplateParameter.EDITOR:
					magazineSeriesTemplate.getEditors().addAll(wikitextStaffProcessor.process(value));
					break;
				default:
					break;
			}
		}

		maybeAddPublicationDate(magazineSeriesTemplate);
		maybeAddNumberOfIssues(magazineSeriesTemplate);
	}

	private void maybeAddPublicationDate(MagazineSeriesTemplate magazineSeriesTemplate) {
		FixedValueHolder<MonthYearRange> publicationDatesFixedValueHolder = magazineSeriesPublicationDatesFixedValueProvider
				.getSearchedValue(magazineSeriesTemplate.getTitle());
		if (publicationDatesFixedValueHolder.isFound()) {
			MonthYearRange publicationDates = publicationDatesFixedValueHolder.getValue();
			MonthYear publicationDateFrom = publicationDates.getFrom();
			MonthYear publicationDateTo = publicationDates.getTo();
			magazineSeriesTemplate.setPublishedMonthFrom(publicationDateFrom.getMonth());
			magazineSeriesTemplate.setPublishedYearFrom(publicationDateFrom.getYear());
			magazineSeriesTemplate.setPublishedMonthTo(publicationDateTo.getMonth());
			magazineSeriesTemplate.setPublishedYearTo(publicationDateTo.getYear());
		}
	}

	private void maybeAddNumberOfIssues(MagazineSeriesTemplate magazineSeriesTemplate) {
		FixedValueHolder<Integer> numberOfIssuesFixedValueHolder = magazineSeriesNumberOfIssuesFixedValueProvider
				.getSearchedValue(magazineSeriesTemplate.getTitle());

		if (numberOfIssuesFixedValueHolder.isFound()) {
			magazineSeriesTemplate.setNumberOfIssues(numberOfIssuesFixedValueHolder.getValue());
		}
	}

}
