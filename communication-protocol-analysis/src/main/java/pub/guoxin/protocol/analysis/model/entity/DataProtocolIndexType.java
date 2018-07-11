package pub.guoxin.protocol.analysis.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 协议：类型索引
 * Create by guoxin on 2018/6/12
 */
@Getter
@NoArgsConstructor
public class DataProtocolIndexType extends BaseDataProtocolIndex<String> implements Serializable {

    private Class<?> type;

    public DataProtocolIndexType(short index, String value, Class<?> type) {
        super(index, value);
        this.type = type;
    }

    public static DataProtocolIndexType create(short index, String value, Class<?> type) {
        return new DataProtocolIndexType(index, value, type);
    }
}