public class Entrance {
    public static void main(String[] args) {

        try {

        } catch (Exception e) {
            e.printStackTrace();
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
        } finally {
        }
    }
}
