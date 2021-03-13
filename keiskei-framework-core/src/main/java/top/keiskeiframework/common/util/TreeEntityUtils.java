package top.keiskeiframework.common.util;

import top.keiskeiframework.common.base.entity.TreeEntity;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @author 陈加敏
 */
public class TreeEntityUtils<T extends TreeEntity> {


    private final Map<Long, List<T>> map;

    public TreeEntityUtils(List<T> list) {
        map = new LinkedHashMap<>();

        for (T t : list) {
            if (map.containsKey(t.getParentId())) {
                map.get(t.getParentId()).add(t);
            } else {
                List<T> tList = new ArrayList<>();
                tList.add(t);
                map.put(t.getParentId(), tList);
            }
        }
    }

    public List<T> getTree(Long id) {
        List<T> tempList = map.get(id);
        if (CollectionUtils.isEmpty(tempList)) {
            return null;
        }
        List<T> responses = new ArrayList<>(tempList.size());
        for (T t : tempList) {
            t.setChildren(getTree(t.getId()));
            responses.add(t);
        }
        return responses;
    }

    public Set<Long> getChildIds(Long id) {
        List<T> tempList = map.get(id);
        Set<Long> tempIds = new HashSet<>();
        tempIds.add(id);
        if (CollectionUtils.isEmpty(tempList)) {
            return tempIds;
        }
        for (T t : tempList) {
            tempIds.add(t.getId());
            tempIds.addAll(getChildIds(t.getId()));
        }
        return tempIds;
    }

    public Set<T> getChildren(Long id) {
        List<T> tempList = map.get(id);
        Set<T> children = new HashSet<>();
        if (CollectionUtils.isEmpty(tempList)) {
            return children;
        }
        for (T t : tempList) {
            children.add(t);
            children.addAll(getChildren(t.getId()));
        }
        return children;
    }

}
