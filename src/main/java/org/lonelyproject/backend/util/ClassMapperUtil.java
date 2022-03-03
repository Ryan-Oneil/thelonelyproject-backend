package org.lonelyproject.backend.util;

import java.util.List;
import org.hibernate.collection.spi.PersistentCollection;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

public class ClassMapperUtil {

    private static final ModelMapper mapper;

    private ClassMapperUtil() {
    }

    static {
        mapper = new ModelMapper();
        mapper.getConfiguration()
            .setMatchingStrategy(MatchingStrategies.LOOSE);
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

    public static <S, T> List<T> mapListIgnoreLazyCollection(List<S> source, Class<T> targetClass) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setPropertyCondition(context -> !(context.getSource() instanceof PersistentCollection));

        return source
            .stream()
            .map(element -> modelMapper.map(element, targetClass))
            .toList();
    }
}
