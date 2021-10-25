package spring;

public class VersionPrinter {
    private int majorVersion;
    private int minorVersion;

    public void print() {
        System.out.printf("포로그램 버전은 %d, %d입니다", majorVersion, minorVersion);
    }

    public void setMajorVersion(int majorVersion) {
        this.majorVersion = majorVersion;
    }

    public void setMinorVersion(int minorVersion) {
        this.minorVersion = minorVersion;
    }
}
