package utils;

import core.source.constant.SourceCacheState;
import core.source.constant.SourceCheckType;

public class SourceCacheUtils {

    private static volatile SourceCacheState CACHE_STATE = SourceCacheState.UNKNOWN;

    private SourceCacheUtils() {
    }

    public static void sourceCacheAction(SourceCheckType type) {
        try {
            switch (type) {
                case CHECK_CACHE: {
                    if (CACHE_STATE != SourceCacheState.DONE) {
                        SourceUtils.getDefaultSource();
                        CACHE_STATE = SourceCacheState.IN_PROGRESS;
                    }
                    break;
                }
                case ASSUME_CACHE:
                    break;
                case RELOAD_CACHE:
                    SourceUtils.getDefaultSource();
                    CACHE_STATE = SourceCacheState.IN_PROGRESS;
                    break;
                default:
                    throw new IllegalArgumentException(String.format("Failed to Execute against SourceCheckType: %s", type));
            }
            CACHE_STATE = SourceCacheState.DONE;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean checkCacheState() {
        return CACHE_STATE == SourceCacheState.DONE;
    }
}
