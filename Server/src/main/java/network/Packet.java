package network;


import org.apache.commons.lang3.SerializationUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class Packet {
    // header
    private byte type;
    private byte code;
    private int bodyLength;
    // body
    private byte[] objects;

    private ArrayList<Object> arrayList;

    public Packet(){
        this(Protocol.UNDEFINED, Protocol.UNDEFINED);
    }

    public Packet(int type) {
        this(type, Protocol.UNDEFINED);
    }

    public Packet(int type, int code) {
        setType(type);
        setCode(code);
    }

    public byte getType() {
        return type;
    }

    public void setType(int type) {
        this.type = (byte) type;
    }

    public byte getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = (byte) code;
    }

    public int getBodyLength() {
        return bodyLength;
    }

    public void setBodyLength(int bodyLength) {
        this.bodyLength = bodyLength;
    }

    public ArrayList<Object> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<Object> arrayList) {
        this.arrayList = arrayList;
    }


    public Packet read(InputStream in) throws IOException {
        // header
        byte[] bytes = new byte[Protocol.LEN_HEADER - Protocol.LEN_BODY_LENGTH];
        in.read(bytes, 0, Protocol.LEN_HEADER - Protocol.LEN_BODY_LENGTH);

        type = bytes[0];
        code = bytes[Protocol.LEN_TYPE];

        byte[] bodyLengthBytes = new byte[Protocol.LEN_BODY_LENGTH];
        in.read(bodyLengthBytes, 0, Protocol.LEN_BODY_LENGTH);
        bodyLength = byteToInt(bodyLengthBytes);

        // body
        if (bodyLength > 0) {
            objects = new byte[bodyLength];
            in.read(objects, 0, bodyLength);
            arrayList = (ArrayList<Object>) SerializationUtils.deserialize(objects);
        }

        return this;
    }

    public void write(OutputStream out) throws IOException {
        this.writeObjects(out, null);
    }
    public void writeObject(OutputStream out, Object object) throws IOException {
        ArrayList<Object> list = new ArrayList<>();
        list.add(object);
        writeObjects(out, list);
    }

    public void writeObjects(OutputStream out, ArrayList<Object> objectArrayList) throws IOException {
        byte[] bytes = new byte[Protocol.LEN_HEADER - Protocol.LEN_BODY_LENGTH];
        bytes[0] = type;
        bytes[Protocol.LEN_TYPE] = code;


        objects = SerializationUtils.serialize(objectArrayList);
        bodyLength = objects.length;
        byte[] bodyLengthBytes = intToByte(bodyLength);

        byte[] sendPt = new byte[Protocol.LEN_HEADER + bodyLength];
        System.arraycopy(bytes, 0, sendPt, 0, Protocol.LEN_HEADER - Protocol.LEN_BODY_LENGTH);
        System.arraycopy(bodyLengthBytes, 0, sendPt, Protocol.LEN_HEADER - Protocol.LEN_BODY_LENGTH, Protocol.LEN_BODY_LENGTH);
        System.arraycopy(objects, 0, sendPt, Protocol.LEN_HEADER, bodyLength);

        // 패킷 전송
        out.write(sendPt);
        out.flush();
    }

    private byte[] intToByte(int i) {
        return ByteBuffer.allocate(Integer.SIZE / 8).putInt(i).array();
    }

    private int byteToInt(byte[] b) {
        return ByteBuffer.wrap(b).getInt();
    }
}
