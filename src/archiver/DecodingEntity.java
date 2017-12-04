package archiver;

public class DecodingEntity {
    private String s;
    private Byte[] nonDecodedBytes;

    public DecodingEntity() {
    }

    public DecodingEntity(String s, Byte[] nonDecodedBytes) {
        this.s = s;
        this.nonDecodedBytes = nonDecodedBytes;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public Byte[] getNonDecodedBytes() {
        return nonDecodedBytes;
    }

    public void setNonDecodedBytes(Byte[] nonDecodedBytes) {
        this.nonDecodedBytes = nonDecodedBytes;
    }
}
