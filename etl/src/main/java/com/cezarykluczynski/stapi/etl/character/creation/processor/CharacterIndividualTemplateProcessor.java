package com.cezarykluczynski.stapi.etl.character.creation.processor;

import com.cezarykluczynski.stapi.etl.common.mapper.GenderMapper;
import com.cezarykluczynski.stapi.etl.template.individual.dto.IndividualTemplate;
import com.cezarykluczynski.stapi.model.character.entity.Character;
import com.cezarykluczynski.stapi.model.common.service.UidGenerator;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class CharacterIndividualTemplateProcessor implements ItemProcessor<IndividualTemplate, Character> {

	private final UidGenerator uidGenerator;

	private final GenderMapper genderMapper;

	@Inject
	public CharacterIndividualTemplateProcessor(UidGenerator uidGenerator, GenderMapper genderMapper) {
		this.uidGenerator = uidGenerator;
		this.genderMapper = genderMapper;
	}

	@Override
	public Character process(IndividualTemplate item) throws Exception {
		Character character = new Character();

		character.setName(item.getName());
		character.setPage(item.getPage());
		character.setUid(uidGenerator.generateFromPage(item.getPage(), Character.class));
		character.setGender(genderMapper.fromEtlToModel(item.getGender()));
		character.setYearOfBirth(item.getYearOfBirth());
		character.setMonthOfBirth(item.getMonthOfBirth());
		character.setDayOfBirth(item.getDayOfBirth());
		character.setPlaceOfBirth(item.getPlaceOfBirth());
		character.setYearOfDeath(item.getYearOfDeath());
		character.setMonthOfDeath(item.getMonthOfDeath());
		character.setDayOfDeath(item.getDayOfDeath());
		character.setPlaceOfDeath(item.getPlaceOfDeath());
		character.setHeight(item.getHeight());
		character.setWeight(item.getWeight());
		character.setDeceased(item.getDeceased());
		character.setBloodType(item.getBloodType());
		character.setMaritalStatus(item.getMaritalStatus());
		character.setSerialNumber(item.getSerialNumber());
		character.setMirror(Boolean.TRUE.equals(item.getMirror()));
		character.setAlternateReality(Boolean.TRUE.equals(item.getAlternateReality()));
		character.getPerformers().addAll(item.getPerformers());
		item.getPerformers().forEach(performer -> performer.getCharacters().add(character));
		character.getCharacterSpecies().addAll(item.getCharacterSpecies());

		return character;
	}

}
