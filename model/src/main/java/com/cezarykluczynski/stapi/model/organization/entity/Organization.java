package com.cezarykluczynski.stapi.model.organization.entity;

import com.cezarykluczynski.stapi.model.common.annotation.TrackedEntity;
import com.cezarykluczynski.stapi.model.common.annotation.enums.TrackedEntityType;
import com.cezarykluczynski.stapi.model.common.entity.PageAwareEntity;
import com.cezarykluczynski.stapi.model.organization.repository.OrganizationRepository;
import com.cezarykluczynski.stapi.model.page.entity.PageAware;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

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
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@TrackedEntity(type = TrackedEntityType.FICTIONAL_PRIMARY, repository = OrganizationRepository.class, singularName = "organization",
		pluralName = "organizations")
public class Organization extends PageAwareEntity implements PageAware {

	@Id
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "organization_sequence_generator")
	@SequenceGenerator(name = "organization_sequence_generator", sequenceName = "organization_sequence", allocationSize = 1)
	private Long id;

	@Column(nullable = false)
	private String name;

	private Boolean government;

	private Boolean intergovernmentalOrganization;

	private Boolean researchOrganization;

	private Boolean sportOrganization;

	private Boolean medicalOrganization;

	private Boolean militaryOrganization;

	private Boolean militaryUnit;

	private Boolean governmentAgency;

	private Boolean lawEnforcementAgency;

	private Boolean prisonOrPenalColony;

	private Boolean mirror;

	private Boolean alternateReality;

}
