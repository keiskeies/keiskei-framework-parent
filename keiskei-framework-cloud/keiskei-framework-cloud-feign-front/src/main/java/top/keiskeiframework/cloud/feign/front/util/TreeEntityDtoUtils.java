package top.keiskeiframework.cloud.feign.front.util;

import org.springframework.util.CollectionUtils;
import top.keiskeiframework.cloud.feign.dto.TreeEntityDTO;

import java.io.Serializable;
import java.util.*;

/**
 * <p>
 * 树形实体工具
 * </p>
 *
 * @param <T>  .
 * @author 陈加敏
 */
public class TreeEntityDtoUtils<T extends TreeEntityDTO<T, ID>, ID extends Serializable> {


    private final LinkedHashMap<ID, List<T>> map;

    public TreeEntityDtoUtils(List<T> list) {
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
    public List<T> getTree(ID id) {
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
    public List<T> getTreeAll(ID id) {
        List<T> tempList = map.get(id);
        if (CollectionUtils.isEmpty(tempList)) {
            return null;
        } else {
            List<T> responses = new ArrayList<>();
            for (T t : tempList) {
                t.setChildren(getTreeAll(t.getId()));
                responses.add(t);
            }
            map.remove(id);
            return responses;
        }
    }

    public List<T> getTreeAll() {
        List<T> responses = getTreeAll(null);
        if (CollectionUtils.isEmpty(responses)) {
            responses = new ArrayList<>();
            Set<ID> keySet = new HashSet<>(map.keySet());
            for (ID key : keySet) {
                List<T> tempList = getTreeAll(key);
                if (!CollectionUtils.isEmpty(tempList)) {
                    responses.addAll(tempList);
                }
            }
        }
        if (!map.isEmpty()) {
            Set<ID> keySet = new HashSet<>(map.keySet());
            for (ID key : keySet) {
                List<T> tempList = getTreeAll(key);
                if (!CollectionUtils.isEmpty(tempList)) {
                    responses.addAll(tempList);
                }
            }
        }
        return responses;
    }

    /**
     * 获取所有子节点id, 包括当前节点id
     *
     * @param id 当前节点id
     * @return .
     */
    public Set<ID> getChildIds(ID id) {
        List<T> tempList = map.get(id);
        Set<ID> tempIds = new HashSet<>();
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
    public Set<T> getChildren(ID id) {
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
