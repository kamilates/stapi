package com.cezarykluczynski.stapi.model.magazine.query;

import com.cezarykluczynski.stapi.model.common.cache.CachingStrategy;
import com.cezarykluczynski.stapi.model.common.query.AbstractQueryBuilderFactory;
import com.cezarykluczynski.stapi.model.magazine.entity.Magazine;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class MagazineQueryBuilderFactory extends AbstractQueryBuilderFactory<Magazine> {

	@Inject
	public MagazineQueryBuilderFactory(JpaContext jpaContext, CachingStrategy cachingStrategy) {
		super(jpaContext, cachingStrategy, Magazine.class);
	}

}
