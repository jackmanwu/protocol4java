package pub.guoxin.protocol.analysis.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pub.guoxin.protocol.analysis.model.anno.Protocol;
import pub.guoxin.protocol.analysis.utils.ArrayUtils;
import pub.guoxin.protocol.analysis.utils.ByteUtil;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.Objects;

/**
 * 协议：协议头
 * <p>
 * Created by guoxin on 18-2-25.
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DataProtocolHeader implements Serializable, ProtocolSerialization {

    private DataProtocolIndexCommand command;
    private short                    version;
    private short                    totalPacket;

    /**
     * 将字节流转换为协议对象
     *
     * @param byteBuffer 字节流
     */
    public DataProtocolHeader(ByteBuffer byteBuffer) {
        {
            short commandIndex = byteBuffer.getShort();
            this.command = DataProtocolIndexCommand.create(commandIndex, null);
//            this.command = DataProtocolIndexCommand.create(commandCode, null); byte[] bytes       = BytesUtils.createByteArray(data, DataProtocolConstants.Header.COMMAND_START, DataProtocolConstants.Header.COMMAND_END);
//            short  commandCode = ByteUtil.getShort(bytes);
//            this.command = DataProtocolIndexCommand.create(commandCode, null);
        }
        {
            short version = byteBuffer.getShort();
            this.version = version;
//            byte[] bytes   = BytesUtils.createByteArray(data, DataProtocolConstants.Header.VERSION_START, DataProtocolConstants.Header.VERSION_END);
//            short  version = ByteUtil.getShort(bytes);
//            this.version = version;
        }
        {
            short totalPacket = byteBuffer.getShort();
            this.totalPacket = totalPacket;
//            byte[] bytes       = BytesUtils.createByteArray(data, DataProtocolConstants.Header.TOTAL_PACKET_START, DataProtocolConstants.Header.TOTAL_PACKET_END);
//            short  totalPacket = ByteUtil.getShort(bytes);
//            this.totalPacket = totalPacket;
        }

    }

    public DataProtocolHeader(Protocol protocol) {
        short  commandIndex = protocol.commandIndex();
        String description  = protocol.description();
        this.command = DataProtocolIndexCommand.create(commandIndex, description);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataProtocolHeader that = (DataProtocolHeader) o;
        return version == that.version && totalPacket == that.totalPacket && Objects.equals(command, that.command);
    }

    @Override
    public int hashCode() {
        return Objects.hash(command, version, totalPacket);
    }

    /**
     * 将协议对象序列化为字节流
     *
     * @return 字节流
     */
    @Override
    public byte[] serialization() {

        byte[] result = null;
        {
            // 协议 命令
            result = ByteUtil.getBytes(this.command.getIndex());
        }
        {
            // 协议 版本
            byte[] bytes = ByteUtil.getBytes(this.version);
            result = ArrayUtils.merge(result, bytes);
        }
        {
            // 协议 数据段总包数
            byte[] bytes = ByteUtil.getBytes(this.totalPacket);
            result = ArrayUtils.merge(result, bytes);
        }
        return result;
    }

}
