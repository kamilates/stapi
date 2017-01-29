package com.cezarykluczynski.stapi.model.company.entity;

import com.cezarykluczynski.stapi.model.common.entity.PageAwareEntity;
import com.cezarykluczynski.stapi.model.page.entity.PageAware;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Company extends PageAwareEntity implements PageAware {

	@Id
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "company_sequence_generator")
	@SequenceGenerator(name = "company_sequence_generator", sequenceName = "company_sequence", allocationSize = 1)
	private Long id;

	@Column(nullable = false)
	private String name;

	private Boolean broadcaster;

	private Boolean collectibleCompany;

	private Boolean conglomerate;

	private Boolean digitalVisualEffectsCompany;

	private Boolean distributor;

	private Boolean gameCompany;

	private Boolean filmEquipmentCompany;

	private Boolean makeUpEffectsStudio;

	private Boolean mattePaintingCompany;

	@Column(name = "model_and_miniature_company")
	private Boolean modelAndMiniatureEffectsCompany;

	private Boolean postProductionCompany;

	private Boolean productionCompany;

	private Boolean propCompany;

	private Boolean recordLabel;

	private Boolean specialEffectsCompany;

	private Boolean tvAndFilmProductionCompany;

	private Boolean videoGameCompany;

}
