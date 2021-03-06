package com.cezarykluczynski.stapi.model.comic_collection.query;

import com.cezarykluczynski.stapi.model.comic_collection.entity.ComicCollection;
import com.cezarykluczynski.stapi.model.common.cache.CachingStrategy;
import com.cezarykluczynski.stapi.model.common.query.AbstractQueryBuilderFactory;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class ComicCollectionQueryBuilderFactory extends AbstractQueryBuilderFactory<ComicCollection> {

	@Inject
	public ComicCollectionQueryBuilderFactory(JpaContext jpaContext, CachingStrategy cachingStrategy) {
		super(jpaContext, cachingStrategy, ComicCollection.class);
	}

}
