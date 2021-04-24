package top.keiskeiframework.common.base.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import top.keiskeiframework.common.base.service.BaseService;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/4/21 11:46
 */
public abstract class BaseServiceImpl<T> implements BaseService<T> {

    @Autowired
    protected JpaRepository<T, Long> jpaRepository;
    @Autowired
    protected JpaSpecificationExecutor<T> jpaSpecificationExecutor;
    @Autowired
    protected BaseService<T> baseService;

    @Override
    public Page<T> page(Specification<T> s, Pageable p) {
        return jpaSpecificationExecutor.findAll(s, p);
    }

    @Override
    public Page<T> page(Example<T> e, Pageable p) {
        return jpaRepository.findAll(e, p);
    }

    @Override
    public List<T> findAll(Specification<T> s) {
        return jpaSpecificationExecutor.findAll(s);
    }

    @Override
    public List<T> findAll(Example<T> e) {
        return jpaRepository.findAll(e);
    }

    @Override
    public List<T> findAll(Specification<T> s, Sort sort) {
        return jpaSpecificationExecutor.findAll(s, sort);
    }

    @Override
    public List<T> findAll(Example<T> e, Sort sort) {
        return jpaRepository.findAll(e, sort);
    }
}
