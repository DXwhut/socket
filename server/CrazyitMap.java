package socket.server;

import java.util.*;

/**
 * Created by duxin on 2017/8/20.
 */
public class CrazyitMap<K, V> {
    public Map<K, V> map = Collections.synchronizedMap(new HashMap<K, V>());

    public synchronized void removeByValue(Object value){
        for(Object key : map.keySet()){
            if(map.get(key) == value){
                map.remove(key);
                break;
            }
        }
    }

    public synchronized Set<V> valueSet(){
        Set<V> result = new HashSet<>();
        for(V value : map.values()){
            result.add(value);
        }
        return result;
    }

    public synchronized K getKeyByValue(V value){
        for(K key : map.keySet()){
            if(map.get(key) == value || map.get(key).equals(value)){
                return key;
            }
        }
        return null;
    }

    public synchronized V put(K key, V value){
        for(V val : valueSet()){
            if(val.equals(value) && val.hashCode() == value.hashCode()){
                throw new RuntimeException("MyMap实例中不允许有重复value！");
            }
        }
        return map.put(key, value);
    }

    public synchronized boolean containsKey(K key){
        return map.containsKey(key);
    }

    public synchronized V get(K key){
        return map.get(key);
    }

    public synchronized int size(){
        return map.size();
    }
}
