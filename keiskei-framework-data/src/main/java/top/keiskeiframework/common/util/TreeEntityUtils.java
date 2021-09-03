package top.keiskeiframework.common.util;

import org.bson.types.ObjectId;
import org.springframework.util.CollectionUtils;
import top.keiskeiframework.common.base.entity.TreeEntity;

import java.util.*;

/**
 * <p>
 * 树形实体工具
 * </p>
 *
 * @author 陈加敏
 */
public class TreeEntityUtils<T extends TreeEntity> {


    private final Map<Object, List<T>> map;

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

    /**
     * 获取当前节点的节点树
     *
     * @param id 当前节点id
     * @return .
     */
    public List<T> getTree(ObjectId id) {
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

    /**
     * 获取所有子节点id, 包括当前节点id
     *
     * @param id 当前节点id
     * @return .
     */
    public Set<ObjectId> getChildIds(ObjectId id) {
        List<T> tempList = map.get(id);
        Set<ObjectId> tempIds = new HashSet<>();
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

    /**
     * 获取所有子节点集合, 包括当前节点
     *
     * @param id 当前节点id
     * @return .
     */
    public Set<T> getChildren(ObjectId id) {
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
