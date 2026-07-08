package com.lowdragmc.lowdraglib2.utils.codec;

import java.util.function.BiConsumer;
import java.util.function.Function;

public interface StreamCodec<B, V> {
    void encode(B buffer, V value);

    V decode(B buffer);

    static <B, V> StreamCodec<B, V> of(BiConsumer<B, V> encoder, Function<B, V> decoder) {
        return new StreamCodec<>() {
            @Override
            public void encode(B buffer, V value) {
                encoder.accept(buffer, value);
            }

            @Override
            public V decode(B buffer) {
                return decoder.apply(buffer);
            }
        };
    }

    static <B, V> StreamCodec<B, V> ofMember(BiConsumer<V, B> encoder, Function<B, V> decoder) {
        return of((buffer, value) -> encoder.accept(value, buffer), decoder);
    }

    default <T> StreamCodec<B, T> map(Function<V, T> mapper, Function<T, V> reverseMapper) {
        return of(
                (buffer, value) -> encode(buffer, reverseMapper.apply(value)),
                buffer -> mapper.apply(decode(buffer))
        );
    }
}
