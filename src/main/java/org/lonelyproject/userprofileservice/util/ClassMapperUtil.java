package org.lonelyproject.backend.util;

import java.util.List;
import org.hibernate.collection.spi.PersistentCollection;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

public class ClassMapperUtil {

    private static final ModelMapper mapper;
    private static final ModelMapper lazyMapper;

    private ClassMapperUtil() {
    }

    static {
        mapper = new ModelMapper();
        mapper.getConfiguration()
            .setMatchingStrategy(MatchingStrategies.LOOSE);

        lazyMapper = new ModelMapper();
        lazyMapper.getConfiguration()
            .setMatchingStrategy(MatchingStrategies.LOOSE)
            .setPropertyCondition(context -> !(context.getSource() instanceof PersistentCollection));
    }

    public static <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
        return source
            .stream()
            .map(element -> mapper.map(element, targetClass))
            .toList();
    }

    public static <S, T> T mapClass(S source, Class<T> targetClass) {
        return mapper.map(source, targetClass);
    }

    public static <S, T> T mapClassIgnoreLazy(S source, Class<T> targetClass) {
        return lazyMapper.map(source, targetClass);
    }

    public static <S, T> List<T> mapListIgnoreLazyCollection(List<S> source, Class<T> targetClass) {
        return source
            .stream()
            .map(element -> lazyMapper.map(element, targetClass))
            .toList();
    }
}
