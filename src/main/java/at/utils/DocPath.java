package at.utils;

/**
 * Перечень документов и их путь до файла
 */
public enum DocPath {
    BID_CHANGE_APPLICATION("/src/test/resources/attachments/BidChangeApplication.png");

    private final String path;

    <T extends String> DocPath(T url) {
        this.path = url;
    }

    public String getPath() {
        return path;
    }
}
