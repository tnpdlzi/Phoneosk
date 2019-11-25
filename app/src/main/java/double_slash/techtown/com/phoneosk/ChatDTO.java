package double_slash.techtown.com.phoneosk;

public class ChatDTO {

    public String table;
    public String message;

    public ChatDTO() {}
    public ChatDTO(String table, String message) {
        this.table = table;
        this.message = message;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ChatDTO{" +
                "table='" + table + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}