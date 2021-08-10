package com.bix.bixApi.utils;

import org.hibernate.id.uuid.CustomVersionOneStrategy;
import org.hibernate.id.uuid.Helper;
import org.hibernate.internal.util.BytesHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * <p> traceId生成器 </p>
 *
 * <pre> Created: 2019/6/18 10:35 </pre>
 *
 * @author  lx
 * @version 1.0
 */
public class TraceIdGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(TraceIdGenerator.class);
    private static CustomVersionOneStrategy strategy = new CustomVersionOneStrategy();

    private TraceIdGenerator() {
        throw new IllegalStateException("TraceIdGenerator is Utility class");
    }

    /**
     * 获取traceId
     * @return
     */
    public static String getTraceId() {
        byte[] loBits = new byte[8];
        long sysTime = System.currentTimeMillis();
        short hiTime = (short)((int)(System.currentTimeMillis() >>> 32));
        int loTime = (int)sysTime;
        System.arraycopy(BytesHelper.fromShort(hiTime), 0, loBits, 0, 2);
        System.arraycopy(BytesHelper.fromInt(loTime), 0, loBits, 2, 4);
        System.arraycopy(Helper.getCountBytes(), 0, loBits, 6, 2);
        loBits[0] = (byte)(loBits[0] & 63);
        loBits[0] = (byte)(loBits[0] | 128);
        long leastSignificantBits = BytesHelper.asLong(loBits);
        UUID uuid = new UUID(strategy.getMostSignificantBits(), leastSignificantBits);
        if (uuid.variant() != 2) {
            LOGGER.warn("traceId生成有问题：bad variant");
        }

        return uuid.toString();
    }
}
