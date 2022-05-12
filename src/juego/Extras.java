package juego;

public class Extras {
    // Transforma grados a radianes.
    public static double radianes(double grados) {
        return grados * (Math.PI / 180);
    }

    // Transforma radianes a grados.
    public static double grados(double radianes) {
        return radianes * (180 / Math.PI);
    }
}