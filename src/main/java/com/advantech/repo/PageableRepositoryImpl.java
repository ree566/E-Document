/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.repo;

import com.advantech.jqgrid.PageInfo;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.GenericTypeResolver;

/**
 *
 * @author MFG.ESOP
 * @param <T>
 * @param <PK>
 */
public class PageableRepositoryImpl<T, PK extends Serializable> implements PageableRepository {

    @Autowired
    @Qualifier("entityManagerFactory")
    private EntityManagerFactory factory;

    Class<T> typeParameterClass;

//    public PageableRepositoryImpl(Class<T> typeParameterClass) {
//        this.typeParameterClass = (Class<T>) GenericTypeResolver.resolveTypeArgument(getClass(), PageableRepositoryImpl.class);
//    }
    public enum SearchOperation {
        EQUALITY, NEGATION, GREATER_THAN, LESS_THAN, LIKE;
        public static final String[] SIMPLE_OPERATION_SET
                = {":", "!", ">", "<", "~"};

        public static SearchOperation getSimpleOperation(final char input) {
            switch (input) {
                case ':':
                    return EQUALITY;
                case '!':
                    return NEGATION;
                case '>':
                    return GREATER_THAN;
                case '<':
                    return LESS_THAN;
                case '~':
                    return LIKE;
                default:
                    return null;
            }
        }
    }

    public class SearchCriteria {

        private String key;
        private Object value;
        private SearchOperation operation;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        public SearchOperation getOperation() {
            return operation;
        }

        public void setOperation(SearchOperation operation) {
            this.operation = operation;
        }

    }

    @Override
    public List<T> findAll(PageInfo info) {
        CriteriaBuilder builder = factory.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(this.typeParameterClass);
        Root<T> root = query.from(this.typeParameterClass);

//        Predicate isComedy = builder.equal(root.get(Movie.genre), Genre.Comedy);
//        Predicate isReallyOld = builder.lessThan(root.get(Movie.createdAt), today.minusYears(25));
//        query.where(builder.and(isComedy, isReallyOld));
//        factory.createQuery(query.select(root)).getResultList();
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private List paginateResult(Criteria criteria, Class clz, PageInfo info) {
        if (info.getSearchField() != null) {
            String searchOper = info.getSearchOper();
            String searchField = info.getSearchField();
            Object searchString = this.autoCaseSearchParam(clz, searchField, info.getSearchString());
            addSearchCriteria(criteria, searchOper, searchField, searchString);
        }

        //Get total row count and reset criteria
        //https://forum.hibernate.org/viewtopic.php?t=951369
        //Set max rows info after "Where" cause(This also create an groupby statement)
        setMaxRowsToInfo(info, criteria);

        //Order after groupby
        String sortIdx = info.getSidx();
        if (sortIdx.contains("asc") || sortIdx.contains("desc")) {
            String[] sortCols = sortIdx.split(",");
            for (String sortCol : sortCols) {
                String col = this.trimLeftRight(sortCol);
                String[] colInfo = col.split(" ");
                String colName = colInfo[0];
                String colSord = colInfo.length == 1 ? "asc" : colInfo[1];
                if (!"".equals(colName)) {
                    criteria.addOrder("asc".equals(colSord) ? Order.asc(colName) : Order.desc(colName));
                }
            }
        } else if (sortIdx.length() > 0) {
            if ("asc".equalsIgnoreCase(info.getSord())) {
                criteria.addOrder(Order.asc(sortIdx));
            } else {
                criteria.addOrder(Order.desc(sortIdx));
            }
        }

        criteria.setFirstResult((info.getPage() - 1) * info.getRows());

        if (info.getRows() > 0) {
            criteria.setMaxResults(info.getRows());
        }

        return criteria.list();
    }

    private String trimLeftRight(String s) {
        return s.replaceAll("^\\s+", "").replaceAll("\\s+$", "");
    }

    private Object autoCaseSearchParam(Class clz, String searchField, String searchParam) {
        if (searchField.contains(".id")) {
            return Objects.equals("0", searchParam) ? null : Integer.parseInt(searchParam);
        }
        try {
            Field field = clz.getDeclaredField(searchField);
            field.setAccessible(true);
            Class type = field.getType();

            if (type.equals(int.class) || type.equals(Integer.class)) {
                return Integer.parseInt(searchParam);
            } else if (type.equals(BigDecimal.class)) {
                return new BigDecimal(searchParam);
            } else if (type.equals(java.util.Date.class)) {
                DateTimeFormatter fmt = DateTimeFormat.forPattern("YYYY-MM-dd");
                return fmt.parseDateTime(searchParam).toDate();
            } else {
                return searchParam;
            }
        } catch (NoSuchFieldException | NumberFormatException | SecurityException e) {
            return searchParam;
        }
    }

    private void setMaxRowsToInfo(PageInfo info, Criteria c) {
        c.setProjection(Projections.rowCount());
        info.setMaxNumOfRows(((Long) c.uniqueResult()).intValue());
        //Remove group by statement after get the maxium row count
        c.setProjection(null);
        c.setResultTransformer(Criteria.ROOT_ENTITY);
    }

    private Criteria addSearchCriteria(Criteria criteria, String searchOper, String searchField, Object searchParam) {
        switch (searchOper) {
            case "in":
                String[] param = searchParam.toString().split(",");
                criteria.add(Restrictions.in(searchField, param));
                break;
            case "eq":
                criteria.add(searchParam == null ? Restrictions.isNull(searchField) : Restrictions.eq(searchField, searchParam));
                break;
            case "ne":
                criteria.add(searchParam == null ? Restrictions.isNotNull(searchField) : Restrictions.ne(searchField, searchParam));
                break;
            case "bw":
                criteria.add(Restrictions.like(searchField, searchParam.toString(), MatchMode.START));
                break;
            case "ew":
                criteria.add(Restrictions.like(searchField, searchParam.toString(), MatchMode.END));
                break;
            case "cn":
                criteria.add(Restrictions.like(searchField, searchParam.toString(), MatchMode.ANYWHERE));
                break;
            case "lt":
                criteria.add(Restrictions.lt(searchField, searchParam));
                break;
            case "gt":
                criteria.add(Restrictions.gt(searchField, searchParam));
                break;
            default:
                break;
        }
        return criteria;
    }

}
