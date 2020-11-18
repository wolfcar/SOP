package com.gitee.sop.gatewaycommon.gateway.codec;

import com.gitee.sop.gatewaycommon.manager.EnvironmentKeys;
import org.springframework.core.codec.AbstractDataBufferDecoder;
import org.springframework.core.codec.Decoder;
import org.springframework.http.codec.DecoderHttpMessageReader;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.server.HandlerStrategies;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author tanghc
 */
public class MessageReaderFactory {

    public static final String METHOD_SET_MAX_IN_MEMORY_SIZE = "setMaxInMemorySize";
    public static final String METHOD_GET_DECODER = "getDecoder";
    public static final int DEFAULT_SIZE = 256 * 1024;

    public static List<HttpMessageReader<?>> build() {
        String maxInMemorySizeValueStr = EnvironmentKeys.MAX_IN_MEMORY_SIZE.getValue();
        int maxInMemorySizeValue = Integer.parseInt(maxInMemorySizeValueStr);
        List<HttpMessageReader<?>> messageReaders = HandlerStrategies.withDefaults().messageReaders();
        if (DEFAULT_SIZE == maxInMemorySizeValue) {
            return messageReaders;
        }
        // 设置POST缓存大小
        for (HttpMessageReader<?> httpMessageReader : messageReaders) {
            Method[] methods = ReflectionUtils.getDeclaredMethods(httpMessageReader.getClass());
            for (Method method : methods) {
                String methodName = method.getName();
                if (METHOD_SET_MAX_IN_MEMORY_SIZE.equals(methodName)) {
                    ReflectionUtils.invokeMethod(method, httpMessageReader, maxInMemorySizeValue);
                } else if (METHOD_GET_DECODER.equals(methodName)) {
                    Object decoder = ReflectionUtils.invokeMethod(method, httpMessageReader);
                    if (decoder instanceof AbstractDataBufferDecoder) {
                        AbstractDataBufferDecoder<?> bufferDecoder = (AbstractDataBufferDecoder<?>) decoder;
                        bufferDecoder.setMaxInMemorySize(maxInMemorySizeValue);
                    }
                }
            }
        }
        return messageReaders;
    }

    public static void initMaxInMemorySize(ExchangeStrategies exchangeStrategies) {
        // 修复返回大文本数据报org.springframework.core.io.buffer.DataBufferLimitException: Exceeded limit on max bytes to buffer : 262144
        String maxInMemorySizeValueStr = EnvironmentKeys.MAX_IN_MEMORY_SIZE.getValue();
        int maxInMemorySizeValue = Integer.parseInt(maxInMemorySizeValueStr);
        if (DEFAULT_SIZE == maxInMemorySizeValue) {
            return;
        }
        for (HttpMessageReader<?> messageReader : exchangeStrategies.messageReaders()) {
            if (messageReader instanceof DecoderHttpMessageReader) {
                DecoderHttpMessageReader reader = (DecoderHttpMessageReader) messageReader;
                Decoder decoder = reader.getDecoder();
                if (decoder instanceof AbstractDataBufferDecoder) {
                    AbstractDataBufferDecoder dataBufferDecoder = (AbstractDataBufferDecoder)decoder;
                    dataBufferDecoder.setMaxInMemorySize(maxInMemorySizeValue);
                }
            }
        }
    }
}
