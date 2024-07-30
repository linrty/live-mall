package top.linrty.live.common.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 用户请求上下文环境
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/7/30 15:28
 * @Version: 1.0
 **/
public class RequestContext {
    private static final ThreadLocal<Map<Object, Object>> resources = new InheritableThreadLocalMap<>();

    public static void set(Object key, Object value) {
        if(key == null) {
            throw new IllegalArgumentException("key can not be null!");
        }
        if(value == null) {
            resources.get().remove(key);
        }
        resources.get().put(key, value);
    }

    public static void remove(Object key) {
        if (key == null) {
            throw new IllegalArgumentException("key can not be null!");
        }
        resources.get().remove(key);
    }

    public static Object get(Object key) {
        if (key == null) {
            throw new IllegalArgumentException("key can not be null!");
        }
        return resources.get().get(key);
    }

    //设计一个clear方法，防止内存泄漏，springboot-web容器处理请求，tomcat，工作线程会去处理我们的业务请求，工作线程是会长时间存在的，
    public static void clear() {
        resources.remove();
    }

    //实现父子线程之间的线程本地变量传递，方便我们后序的异步操作
    //A-->threadLocal ("userId",1001)
    //A-->new Thread(B)-->B线程属于A线程的子线程，threadLocal get("userId")
    private static final class InheritableThreadLocalMap<T extends Map<Object, Object>> extends InheritableThreadLocal<Map<Object, Object>> {

        @Override
        protected Map<Object, Object> initialValue() {
            return new HashMap();
        }

        @Override
        protected Map<Object, Object> childValue(Map<Object, Object> parentValue) {
            if (parentValue != null) {
                return (Map<Object, Object>) ((HashMap<Object, Object>) parentValue).clone();
            } else {
                return null;
            }
        }
    }

}
