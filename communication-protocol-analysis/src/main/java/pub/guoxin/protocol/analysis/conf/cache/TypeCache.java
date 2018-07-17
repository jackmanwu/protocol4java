package pub.guoxin.protocol.analysis.conf.cache;

import lombok.Builder;
import lombok.Getter;
import pub.guoxin.protocol.analysis.conf.convert.TypeConvert;

/**
 * Create by guoxin on 2018/7/13
 */
@Builder
@Getter
public class TypeCache {
    private short                        index;
    private Class<?>                     typeClass;
    private Class<? extends TypeConvert> typeConvert;
}
