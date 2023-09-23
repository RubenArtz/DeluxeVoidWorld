package developer.voidw;

public class activate {

    public static void setPolymart() {

        /*
        Si es true, la licencia no se utiliza
         */
        boolean isSpigot = true;

        if (!isSpigot) {
            polymart.setLicense();
        }
    }
}