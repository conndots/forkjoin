package io.conndots.forkjoin.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by xqlee on 16/1/30.
 */
public class MapUtil {
    public interface MapValueCatFunction<T> {
        T apply(T val0, T val1);
    }
    public static <T> ImmutableMap<String, T> mergeWith(final MapValueCatFunction<T> valCatFunc, ImmutableMap<String, T>
            map0, ImmutableMap<String, T> map1) {
        Preconditions.checkNotNull(valCatFunc);
        Preconditions.checkNotNull(map0);
        Preconditions.checkNotNull(map1);

        final Map<String, T> resultMap = Maps.newHashMap();
        FluentIterable.from(map0.entrySet()).filter(stringTEntry -> stringTEntry.getValue() != null).forEach
                (stringTEntry -> resultMap.put(stringTEntry.getKey(), stringTEntry.getValue()));

        FluentIterable.from(map1.entrySet()).filter(stringEntry -> stringEntry.getValue() != null).forEach(stringEntry
                -> {
            String key = stringEntry.getKey();
            T value = stringEntry.getValue();

            if (resultMap.containsKey(key)) {
                resultMap.put(key, valCatFunc.apply(value, resultMap.get(key)));
            }
            else {
                resultMap.put(key, value);
            }
        });

        return new ImmutableMap.Builder<String, T>().putAll(resultMap).build();
    }

    public static <T> ImmutableMap<String, T> mergeWith(final MapValueCatFunction<T> valueCatFunction,
                                                        ImmutableMap<String, T> map0, ImmutableMap<String, T>...
                                                                maps) {
        Preconditions.checkNotNull(valueCatFunction);
        Preconditions.checkNotNull(map0);
        Preconditions.checkArgument(maps != null && maps.length > 0);

        ImmutableMap<String, T> result = map0;
        for (ImmutableMap<String, T> map : maps) {
            result = mergeWith(valueCatFunction, result, map);
        }
        return result;
    }
}
