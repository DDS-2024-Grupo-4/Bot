package ar.edu.utn.dds.k3003.Utils;

public class prueba {
    public  static void main(String[] args) {
        String mensaje=" que tal";
        String[] partes = mensaje.split("\\s+");
        String formas=partes[1];
        try {
                formas=formas.concat(",").concat(partes[2]);
                formas=formas.concat(",").concat(partes[3]);
        }
        catch (ArrayIndexOutOfBoundsException e) {
        }
        System.out.println(formas);
        };
    }

