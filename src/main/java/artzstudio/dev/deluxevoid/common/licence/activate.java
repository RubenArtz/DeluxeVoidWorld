package artzstudio.dev.deluxevoid.common.licence;

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