package co.edu.uniquindio.poo;

public class Main {

    public static void main(String[] args) {

        int[][] matrizEjemplo = {
            {2, 3, 45, 6},
            {4, 6, 7,  3},
            {4, 6, 7,  3},
            {1, 8, 9,  2}
        };

        System.out.println("--- 1. Imprimir Matriz ---");
        imprimirMatriz(matrizEjemplo);

        System.out.println("\n--- 2. Sumar todos los números ---");
        int sumaTotal = sumarMatriz(matrizEjemplo);
        System.out.println("La suma de la matriz es: " + sumaTotal);

        System.out.println("\n--- 3. Sumar la diagonal principal ---");
        int sumaDiagonal = sumarDiagonalPrincipal(matrizEjemplo);
        System.out.println("La suma de la diagonal principal es: " + sumaDiagonal);

        System.out.println("\n--- 4. Dibujar una 'X' en una matriz (5x5) ---");
        dibujarX(5);

        System.out.println("\n--- 5. Cuadro / Triángulo Superior ---");
        imprimirTrianguloSuperior(matrizEjemplo);

        System.out.println("\n--- 6. Espiral de números (4x4) ---");
        int[][] espiral = generarEspiral(4, 4);
        imprimirMatriz(espiral);
    }

    // 1. Imprimir una matriz
    public static void imprimirMatriz(int[][] matriz) {
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                System.out.print(matriz[i][j] + "\t");
            }
            System.out.println();
        }
    }

    // 2. Sumar todos los números de una matriz
    public static int sumarMatriz(int[][] matriz) {
        int suma = 0;
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                suma += matriz[i][j];
            }
        }
        return suma;
    }

    // 3. Sumar los números de la diagonal principal de una matriz
    public static int sumarDiagonalPrincipal(int[][] matriz) {
        int suma = 0;
        int limite = Math.min(matriz.length, matriz[0].length);
        for (int i = 0; i < limite; i++) {
            suma += matriz[i][i];
        }
        return suma;
    }

    // 4. Dibujar una 'X' en una matriz
    public static void dibujarX(int tamano) {
        char[][] matrizX = new char[tamano][tamano];
        for (int i = 0; i < tamano; i++) {
            for (int j = 0; j < tamano; j++) {
                if (i == j || i + j == tamano - 1) {
                    matrizX[i][j] = 'X';
                } else {
                    matrizX[i][j] = ' ';
                }
                System.out.print(matrizX[i][j] + " ");
            }
            System.out.println();
        }
    }

    // 5. Cuadro superior / Triángulo superior (elementos en o sobre la diagonal principal)
    public static void imprimirTrianguloSuperior(int[][] matriz) {
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                if (i <= j) {
                    System.out.print(matriz[i][j] + "\t");
                } else {
                    System.out.print("\t"); // Espacio vacío para mantener el formato
                }
            }
            System.out.println();
        }
    }

    // 6. Dibujar en una matriz un espiral de números
    public static int[][] generarEspiral(int filas, int columnas) {
        int[][] matriz = new int[filas][columnas];
        int valor = 1;
        int arriba = 0, abajo = filas - 1;
        int izquierda = 0, derecha = columnas - 1;

        while (arriba <= abajo && izquierda <= derecha) {
            // Llenar fila superior
            for (int j = izquierda; j <= derecha; j++) {
                matriz[arriba][j] = valor++;
            }
            arriba++;

            // Llenar columna derecha
            for (int i = arriba; i <= abajo; i++) {
                matriz[i][derecha] = valor++;
            }
            derecha--;

            // Llenar fila inferior
            if (arriba <= abajo) {
                for (int j = derecha; j >= izquierda; j--) {
                    matriz[abajo][j] = valor++;
                }
                abajo--;
            }

            // Llenar columna izquierda
            if (izquierda <= derecha) {
                for (int i = abajo; i >= arriba; i--) {
                    matriz[i][izquierda] = valor++;
                }
                izquierda++;
            }
        }
        return matriz;
    }
}