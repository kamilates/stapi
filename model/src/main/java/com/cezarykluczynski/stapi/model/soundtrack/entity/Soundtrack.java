package com.cezarykluczynski.stapi.model.soundtrack.entity;

import com.cezarykluczynski.stapi.model.common.annotation.TrackedEntity;
import com.cezarykluczynski.stapi.model.common.annotation.enums.TrackedEntityType;
import com.cezarykluczynski.stapi.model.common.entity.PageAwareEntity;
import com.cezarykluczynski.stapi.model.company.entity.Company;
import com.cezarykluczynski.stapi.model.page.entity.PageAware;
import com.cezarykluczynski.stapi.model.reference.entity.Reference;
import com.cezarykluczynski.stapi.model.staff.entity.Staff;
import com.cezarykluczynski.stapi.model.video_release.repository.VideoReleaseRepository;
import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import java.time.LocalDate;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true, exclude = {"label", "composers", "contributors", "references"})
@EqualsAndHashCode(callSuper = true, exclude = {"label", "composers", "contributors", "references"})
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@TrackedEntity(type = TrackedEntityType.REAL_WORLD_PRIMARY, repository = VideoReleaseRepository.class, singularName = "video game",
		pluralName = "video games")
public class Soundtrack extends PageAwareEntity implements PageAware {

	@Id
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "soundtrack_sequence_generator")
	@SequenceGenerator(name = "soundtrack_sequence_generator", sequenceName = "soundtrack_sequence", allocationSize = 1)
	private Long id;

	private String title;

	private LocalDate releaseDate;

	private Integer numberOfDataCarriers;

	private Integer length;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "label_id")
	private Company label;

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "soundtracks_composers",
			joinColumns = @JoinColumn(name = "soundtrack_id", nullable = false, updatable = false),
			inverseJoinColumns = @JoinColumn(name = "staff_id", nullable = false, updatable = false))
	@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
	private Set<Staff> composers = Sets.newHashSet();

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "soundtracks_contributors",
			joinColumns = @JoinColumn(name = "soundtrack_id", nullable = false, updatable = false),
			inverseJoinColumns = @JoinColumn(name = "staff_id", nullable = false, updatable = false))
	@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
	private Set<Staff> contributors = Sets.newHashSet();

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "soundtracks_references",
			joinColumns = @JoinColumn(name = "soundtrack_id", nullable = false, updatable = false),
			inverseJoinColumns = @JoinColumn(name = "reference_id", nullable = false, updatable = false))
	@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
	private Set<Reference> references = Sets.newHashSet();

	// TODO orchestrators
	// TODO orchestras
	// TODO conductors


}
