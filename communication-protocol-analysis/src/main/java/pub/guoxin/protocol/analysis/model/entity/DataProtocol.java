package pub.guoxin.protocol.analysis.model.entity;

import lombok.*;
import pub.guoxin.protocol.analysis.model.DataProtocolCallbackService;
import pub.guoxin.protocol.analysis.model.anno.Callback;
import pub.guoxin.protocol.analysis.model.anno.Protocol;
import pub.guoxin.protocol.analysis.model.exception.ProtocolConfigException;
import pub.guoxin.protocol.analysis.model.exception.ProtocolException;
import pub.guoxin.protocol.analysis.utils.ArrayUtils;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.Objects;

/**
 * 中间层
 * 协议对象转换处理中间适配层
 * <p>
 * Created by guoxin on 18-2-25.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataProtocol<T extends ProtocolEntity> implements Serializable, ProtocolSerialization {
    /**
     * 协议头
     */
    private DataProtocolHeader                           header;
    /**
     * 数据段
     */
    private DataProtocolPacketList                       packets;
    /**
     * 协议对象类型
     */
    private Class<? extends ProtocolEntity>              protocolEntity;
    /**
     * 协议回调
     */
    private Class<? extends DataProtocolCallbackService> callback;

    /**
     * 解析数据
     *
     * @param bytes 字节流
     */
    public DataProtocol(byte[] bytes) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        this.header = new DataProtocolHeader(byteBuffer);
        this.packets = new DataProtocolPacketList(byteBuffer, this.header.getTotalPacket());
    }

    public DataProtocol(ByteBuffer byteBuffer) {
        this.header = new DataProtocolHeader(byteBuffer);
        this.packets = new DataProtocolPacketList(byteBuffer, header.getTotalPacket());
    }

    /**
     * 协议对象转换
     *
     * @param protocolEntity 协议对象
     */
    public DataProtocol(ProtocolEntity protocolEntity) {
        this(protocolEntity.getClass(), protocolEntity);
    }

    public DataProtocol(Class<? extends ProtocolEntity> clazz, ProtocolEntity protocolEntity) {
        Protocol protocolAnnotation = clazz.getAnnotation(Protocol.class);
        Callback callbackAnnotation = clazz.getAnnotation(Callback.class);
        if (Objects.isNull(protocolAnnotation)) {
            throw new ProtocolConfigException("请使用`@Protocol`对您的协议对象进行标识。");
        }
        this.protocolEntity = clazz;
        this.callback = callbackAnnotation.callback();
        this.header = new DataProtocolHeader(protocolAnnotation);
        this.packets = new DataProtocolPacketList(clazz, protocolEntity);
    }

    public ProtocolEntity protocolEntity() {
        Object instance;
        try {
//            Exception in thread "main" java.lang.NullPointerException
//            TODO 在解析字节流时，没有找到对应的协议对象，这里涉及到协议对象初始化的问题
            instance = this.protocolEntity.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            throw new ProtocolException("实例化协议对象失败");
        }
        packets.protocolEntity(instance, this.protocolEntity.getDeclaredFields());
        return (ProtocolEntity) instance;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataProtocol<?> that = (DataProtocol<?>) o;
        return Objects.equals(this.header, that.header) && Objects.equals(this.packets, that.packets);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.header, this.packets);
    }

    /**
     * 协议对象
     *
     * @return 字节流
     */
    @Override
    public byte[] serialization() {
        byte[] headerBytes  = this.header.serialization();
        byte[] packetsBytes = this.packets.serialization();
        return ArrayUtils.merge(headerBytes, packetsBytes);
    }

}
