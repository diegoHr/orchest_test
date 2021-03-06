package com.diego.hernando.orchestTest.business.worksign.service.transformWorkSignService;

import java.util.List;

/**
 *
 * @param <D> DTO selected
 * @param <E> Entity selected
 */
public interface ITransformWorkSignService<D,E> {
    D getDto (E entity);

    /**
     * @param dto
     * @return E entity not saved in persistence
     */
    E getEntity (D dto);

    /**
     *
     * @param dto
     * @return E entity saved in persistence
     */
    @SuppressWarnings("UnusedReturnValue")
    E getEntitySaved(D dto);

    List<D> getListDto(List<E> entities);

    List<E> getListEntities (List<D> dtos);

    List<E> getListEntitiesSaved (List<D> dtos);
}
