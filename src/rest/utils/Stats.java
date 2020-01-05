package rest.utils;

public class Stats {

    private static Stats stats = null;

    private Stats () {}

    public static Stats getInstance() {

        if (stats == null) {

            stats = new Stats();
        }

        return stats;
    }

    private boolean hardwareOnline = false;

    public boolean isHardwareOnline() {
        return hardwareOnline;
    }

    public void setHardwareOnline(boolean hardwareOnline) {
        this.hardwareOnline = hardwareOnline;
    }

    public void sendToAdmin (String json) {

        try {

            sendToAdmin(json);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
