package com.cezarykluczynski.stapi.etl.organization.creation.processor;

import com.cezarykluczynski.stapi.etl.common.dto.FixedValueHolder;
import com.cezarykluczynski.stapi.etl.common.processor.CategoryTitlesExtractingProcessor;
import com.cezarykluczynski.stapi.etl.common.service.PageBindingService;
import com.cezarykluczynski.stapi.etl.organization.creation.service.OrganizationPageFilter;
import com.cezarykluczynski.stapi.etl.util.constant.CategoryTitle;
import com.cezarykluczynski.stapi.model.common.service.GuidGenerator;
import com.cezarykluczynski.stapi.model.organization.entity.Organization;
import com.cezarykluczynski.stapi.sources.mediawiki.dto.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service
public class OrganizationPageProcessor implements ItemProcessor<Page, Organization> {

	private OrganizationPageFilter organizationPageFilter;

	private PageBindingService pageBindingService;

	private GuidGenerator guidGenerator;

	private CategoryTitlesExtractingProcessor categoryTitlesExtractingProcessor;

	private OrganizationNameFixedValueProvider organizationNameFixedValueProvider;

	@Inject
	public OrganizationPageProcessor(OrganizationPageFilter organizationPageFilter,
			PageBindingService pageBindingService, GuidGenerator guidGenerator, CategoryTitlesExtractingProcessor categoryTitlesExtractingProcessor,
			OrganizationNameFixedValueProvider organizationNameFixedValueProvider) {
		this.organizationPageFilter = organizationPageFilter;
		this.pageBindingService = pageBindingService;
		this.guidGenerator = guidGenerator;
		this.categoryTitlesExtractingProcessor = categoryTitlesExtractingProcessor;
		this.organizationNameFixedValueProvider = organizationNameFixedValueProvider;
	}

	@Override
	public Organization process(Page item) throws Exception {
		if (organizationPageFilter.shouldBeFilteredOut(item)) {
			return null;
		}

		Organization organization = new Organization();
		FixedValueHolder<String> titleFixedValueHolder = organizationNameFixedValueProvider.getSearchedValue(item.getTitle());
		organization.setName(titleFixedValueHolder.isFound() ? titleFixedValueHolder.getValue() : item.getTitle());

		organization.setPage(pageBindingService.fromPageToPageEntity(item));
		organization.setGuid(guidGenerator.generateFromPage(organization.getPage(), Organization.class));

		List<String> categoryTitleList = categoryTitlesExtractingProcessor.process(item.getCategories());

		organization.setGovernment(categoryTitleList.contains(CategoryTitle.GOVERNMENTS));
		organization.setIntergovernmentalOrganization(categoryTitleList.contains(CategoryTitle.INTERGOVERNMENTAL_ORGANIZATIONS)
				|| categoryTitleList.contains(CategoryTitle.EARTH_INTERGOVERNMENTAL_ORGANIZATIONS));
		organization.setResearchOrganization(categoryTitleList.contains(CategoryTitle.RESEARCH_ORGANIZATIONS));
		organization.setSportOrganization(categoryTitleList.contains(CategoryTitle.SPORTS_ORGANIZATIONS));
		organization.setMedicalOrganization(categoryTitleList.contains(CategoryTitle.MEDICAL_ORGANIZATIONS)
				|| categoryTitleList.contains(CategoryTitle.MEDICAL_ESTABLISHMENTS) || categoryTitleList.contains(CategoryTitle.WARDS)
				|| categoryTitleList.contains(CategoryTitle.MEDICAL_ESTABLISHMENTS_RETCONNED));
		organization.setMilitaryUnit(categoryTitleList.contains(CategoryTitle.MILITARY_UNITS));
		organization.setMilitaryOrganization(organization.getMilitaryUnit() || categoryTitleList.contains(CategoryTitle.MILITARY_ORGANIZATIONS)
				|| categoryTitleList.contains(CategoryTitle.EARTH_MILITARY_ORGANIZATIONS));
		organization.setGovernmentAgency(anyEndsWithIgnoreCase(categoryTitleList, CategoryTitle.AGENCIES));
		organization.setLawEnforcementAgency(categoryTitleList.contains(CategoryTitle.LAW_ENFORCEMENT_AGENCIES));
		organization.setPrisonOrPenalColony(categoryTitleList.contains(CategoryTitle.PRISONS_AND_PENAL_COLONIES));
		organization.setSchool(anyEndsWithIgnoreCase(categoryTitleList, CategoryTitle.SCHOOLS));
		organization.setEstablishment(organization.getSchool() || anyEndsWithIgnoreCase(categoryTitleList, CategoryTitle.ESTABLISHMENTS)
				|| categoryTitleList.contains(CategoryTitle.ESTABLISHMENTS_RETCONNED) || categoryTitleList.contains(CategoryTitle.WARDS)
				|| categoryTitleList.contains(CategoryTitle.MEDICAL_ESTABLISHMENTS_RETCONNED));
		organization.setDs9Establishment(categoryTitleList.contains(CategoryTitle.DS9_ESTABLISHMENTS));
		organization.setMirror(categoryTitleList.contains(CategoryTitle.MIRROR_UNIVERSE));
		organization.setAlternateReality(categoryTitleList.contains(CategoryTitle.ALTERNATE_REALITY));

		return organization;
	}

	private boolean anyEndsWithIgnoreCase(List<String> categoryTitleList, String suffix) {
		return categoryTitleList.stream().anyMatch(categoryTitle -> StringUtils.endsWithIgnoreCase(categoryTitle, suffix));
	}

}
