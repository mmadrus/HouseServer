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

    private int hardwareOnline = 0;

    public int isHardwareOnline() {
        return hardwareOnline;
    }

    public void setHardwareOnline(int hardwareOnline) {
        this.hardwareOnline = hardwareOnline;
    }

    private boolean adminOnline = false;

    public boolean isAdminOnline() {
        return adminOnline;
    }

    public void setAdminOnline(boolean adminOnline) {
        this.adminOnline = adminOnline;
    }

    public void sendToAdmin (String json) {

        try {

            sendToAdmin(json);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
