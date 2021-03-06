package com.cezarykluczynski.stapi.etl.template.individual.processor;

import com.cezarykluczynski.stapi.etl.common.dto.EnrichablePair;
import com.cezarykluczynski.stapi.etl.common.processor.ItemEnrichingProcessor;
import com.cezarykluczynski.stapi.etl.template.common.processor.MaritalStatusProcessor;
import com.cezarykluczynski.stapi.etl.template.common.processor.gender.PartToGenderProcessor;
import com.cezarykluczynski.stapi.etl.template.individual.dto.IndividualLifeBoundaryDTO;
import com.cezarykluczynski.stapi.etl.template.individual.dto.IndividualTemplate;
import com.cezarykluczynski.stapi.etl.template.individual.dto.IndividualTemplateParameter;
import com.cezarykluczynski.stapi.etl.template.individual.processor.species.CharacterSpeciesWikitextProcessor;
import com.cezarykluczynski.stapi.sources.mediawiki.dto.Template;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service
public class IndividualTemplatePartsEnrichingProcessor implements ItemEnrichingProcessor<EnrichablePair<List<Template.Part>, IndividualTemplate>> {

	private final PartToGenderProcessor partToGenderProcessor;

	private final IndividualLifeBoundaryProcessor individualLifeBoundaryProcessor;

	private final IndividualTemplateActorLinkingProcessor individualTemplateActorLinkingProcessor;

	private final IndividualHeightProcessor individualHeightProcessor;

	private final IndividualWeightProcessor individualWeightProcessor;

	private final IndividualBloodTypeProcessor individualBloodTypeProcessor;

	private final MaritalStatusProcessor maritalStatusProcessor;

	private final CharacterSpeciesWikitextProcessor characterSpeciesWikitextProcessor;

	@Inject
	@SuppressWarnings("ParameterNumber")
	public IndividualTemplatePartsEnrichingProcessor(PartToGenderProcessor partToGenderProcessor,
			IndividualLifeBoundaryProcessor individualLifeBoundaryProcessor,
			IndividualTemplateActorLinkingProcessor individualTemplateActorLinkingProcessor, IndividualHeightProcessor individualHeightProcessor,
			IndividualWeightProcessor individualWeightProcessor, IndividualBloodTypeProcessor individualBloodTypeProcessor,
			MaritalStatusProcessor maritalStatusProcessor, CharacterSpeciesWikitextProcessor characterSpeciesWikitextProcessor) {
		this.partToGenderProcessor = partToGenderProcessor;
		this.individualLifeBoundaryProcessor = individualLifeBoundaryProcessor;
		this.individualTemplateActorLinkingProcessor = individualTemplateActorLinkingProcessor;
		this.individualHeightProcessor = individualHeightProcessor;
		this.individualWeightProcessor = individualWeightProcessor;
		this.individualBloodTypeProcessor = individualBloodTypeProcessor;
		this.maritalStatusProcessor = maritalStatusProcessor;
		this.characterSpeciesWikitextProcessor = characterSpeciesWikitextProcessor;
	}

	@Override
	public void enrich(EnrichablePair<List<Template.Part>, IndividualTemplate> enrichablePair) throws Exception {
		IndividualTemplate individualTemplate = enrichablePair.getOutput();

		for (Template.Part part : enrichablePair.getInput()) {
			String key = part.getKey();
			String value = part.getValue();

			switch (key) {
				case IndividualTemplateParameter.GENDER:
					individualTemplate.setGender(partToGenderProcessor.process(part));
					break;
				case IndividualTemplateParameter.ACTOR:
					individualTemplateActorLinkingProcessor.enrich(EnrichablePair.of(part, individualTemplate));
					break;
				case IndividualTemplateParameter.HEIGHT:
					individualTemplate.setHeight(individualHeightProcessor.process(value));
					break;
				case IndividualTemplateParameter.WEIGHT:
					individualTemplate.setWeight(individualWeightProcessor.process(value));
					break;
				case IndividualTemplateParameter.SERIAL_NUMBER:
					if (StringUtils.isNotBlank(value)) {
						individualTemplate.setSerialNumber(value);
					}
					break;
				case IndividualTemplateParameter.BORN:
					IndividualLifeBoundaryDTO birthBoundaryDTO = individualLifeBoundaryProcessor
							.process(value);
					individualTemplate.setYearOfBirth(birthBoundaryDTO.getYear());
					individualTemplate.setMonthOfBirth(birthBoundaryDTO.getMonth());
					individualTemplate.setDayOfBirth(birthBoundaryDTO.getDay());
					break;
				case IndividualTemplateParameter.DIED:
					IndividualLifeBoundaryDTO deathBoundaryDTO = individualLifeBoundaryProcessor
							.process(value);
					individualTemplate.setYearOfDeath(deathBoundaryDTO.getYear());
					individualTemplate.setMonthOfDeath(deathBoundaryDTO.getMonth());
					individualTemplate.setDayOfDeath(deathBoundaryDTO.getDay());
					break;
				case IndividualTemplateParameter.MARITAL_STATUS:
					individualTemplate.setMaritalStatus(maritalStatusProcessor.process(value));
					break;
				case IndividualTemplateParameter.BLOOD_TYPE:
					individualTemplate.setBloodType(individualBloodTypeProcessor.process(value));
					break;
				case IndividualTemplateParameter.SPECIES:
					individualTemplate.getCharacterSpecies()
							.addAll(characterSpeciesWikitextProcessor.process(Pair.of(value, individualTemplate)));
					break;
				default:
					break;
			}
		}
	}

}
