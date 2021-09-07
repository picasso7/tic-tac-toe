package tictactoe;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        GameState gameState;
        //Random random = new Random();
        // true - player
        // false - pc
        Player player1;
        Player player2;
        boolean currentPlayerIs1;
        boolean gameLoop = true;
        String[] values;

        while(gameLoop) {
            System.out.println("Input command:");
            values = scanner.nextLine().split("\\s+");

            //exit check
            if (values.length == 1 && values[0].equals("exit")) {
                gameLoop = false;
                continue;
            }

            //if no 3 inputs then start again
            if (values.length != 3) {
                System.out.println("Bad parameters!");
                continue;
            }

            //if first input different from start then start again
            if (!values[0].equals("start")) {
                System.out.println("Bad parameters!");
                continue;
            }

            switch(values[1]) {
                case "user":
                    player1 = new User();
                    break;
                case "easy":
                    player1 = new AI_Easy();
                    break;
                case "medium":
                    player1 = new AI_Medium();
                    break;
                case "hard":
                    player1 = new AI_Hard();
                    break;
                default:
                    System.out.println("Bad parameters!");
                    continue;
            }


            switch(values[2]) {
                case "user":
                    player2 = new User();
                    break;
                case "easy":
                    player2 = new AI_Easy();
                    break;
                case "medium":
                    player2 = new AI_Medium();
                    break;
                case "hard":
                    player2 = new AI_Hard();
                    break;
                default:
                    System.out.println("Bad parameters!");
                    continue;
            }

            currentPlayerIs1 = true;
            gameState = new GameState("_________");
            gameState.printGameState();

            while(gameState.findWinner() == 0) {
                if (currentPlayerIs1) {
                    player1.makeMove(gameState);
                } else {
                    player2.makeMove(gameState);
                }
                gameState.printGameState();
                currentPlayerIs1 = !currentPlayerIs1;
            }
            switch(gameState.findWinner()) {
                case 1:
                    System.out.println("X wins");
                    break;
                case -1:
                    System.out.println("O wins");
                    break;
                case 2:
                    System.out.println("Draw");
                    break;
            }
        }



    }
}

class GameState {
    char[][] gameState;
    int x = 0; // 1
    int o = 0; // -1
    int turn = -1; //last player move, not current
    GameState(String lineGameState) {
        gameState = new char[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (lineGameState.charAt(3 * i + j) == '_') {
                    gameState[i][j] = ' ';
                } else {
                    gameState[i][j] = lineGameState.charAt(3 * i + j);
                }

                if (gameState[i][j] == 'X') {
                    x++;
                } else if (gameState[i][j] == 'O') {
                    o++;
                }
            }
        }
    }

    GameState(GameState gameState) {
        this.gameState = new char[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                this.gameState[i][j] = gameState.gameState[i][j];
            }
        }
        this.x = gameState.x;
        this.o = gameState.o;
        this.turn = gameState.turn;
    }

    void printGameState() {
        System.out.println("---------");
        for (int i = 0; i < 3; i++) {
            System.out.println("| " + gameState[i][0] + " " + gameState[i][1] + " " + gameState[i][2] + " |");
        }
        System.out.println("---------");
    }

    // x == 1
    // o == -1
    // draw == 2
    // not finished == 0
    int findWinner() {

        if (x + o == 9) {
            //System.out.println("Draw");
            return 2;
        }

        int[][] magicSquare = {
                {2, 7, 6},
                {9, 5, 1},
                {4, 3, 8}
        };

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (gameState[i][j] == ' ') {
                    magicSquare[i][j] *= 0;
                } else if (gameState[i][j] == 'X'){
                    magicSquare[i][j] *= 1;
                } else {
                    magicSquare[i][j] *= -1;
                }
            }
        }
        
        int[] sumsOfRows = new int[8];
        for (int i = 0; i < 3; i++) {
            sumsOfRows[i] = magicSquare[i][0] + magicSquare[i][1] + magicSquare[i][2];
            sumsOfRows[i + 3] = magicSquare[0][i] + magicSquare[1][i] + magicSquare[2][i];
        }
        sumsOfRows[6] = magicSquare[0][0] + magicSquare[1][1] + magicSquare[2][2];
        sumsOfRows[7] = magicSquare[0][2] + magicSquare[1][1] + magicSquare[2][0];

        for ( int i : sumsOfRows) {
            if (i == 15) {
                //System.out.println("X wins");
                return 1;
            } else if (i == -15) {
                //System.out.println("0 wins");
                return -1;
            }
        }
        return 0;

    }

    void shiftTurns() {
        turn *= -1;
    }


    boolean nextMove(int a, int b) {
        if (gameState[a - 1][b - 1] == ' ') {
            gameState[a - 1][b - 1] = turn == 1 ? 'O' : 'X';
            if (turn == 1) {
                o++;
            } else {
                x++;
            }
            shiftTurns();
            return true;
        } else {
            return false;
        }
    }



}

class Player {
    void makeMove(GameState gameState) {

    }
}

class User extends Player {
    void makeMove(GameState gameState) {
        Scanner scanner = new Scanner(System.in);
        boolean goodValues = false;
        int a = 0;
        int b = 0;
        while (!goodValues) {
            System.out.println("Enter the coordinates:");
            if (scanner.hasNextInt()) {
                a = scanner.nextInt();
                b = scanner.nextInt();
                if (a > 0 && a < 4 && b > 0 && b < 4) {
                    goodValues = true;
                } else {
                    System.out.println("Coordinates should be from 1 to 3!");
                }

            } else {
                scanner.nextLine();
                System.out.println("You should enter numbers!");
            }
            if (goodValues) {
                goodValues = gameState.nextMove(a, b);
                if (!goodValues) {
                    System.out.println("This cell is occupied! Choose another one!");
                    //goodValues = false;
                }
            }
        }
    }
}

class AI_Easy extends Player {
    void makeMove(GameState gameState) {
        Random random = new Random();
        System.out.println("Making move level \"easy\"");
        while(!gameState.nextMove(random.nextInt(3) + 1,random.nextInt(3) + 1 )) {
        }
    }
}

class AI_Medium extends Player {
    GameState gameState;
    void makeMove(GameState gameState) {
        System.out.println("Making move level \"medium\"");
        Random random = new Random();
        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 3; j++) {
                this.gameState = new GameState(gameState);
                if (this.gameState.nextMove(i, j)) {
                    if(this.gameState.findWinner() == this.gameState.turn) {
                        gameState.nextMove(i, j);
                        return;
                    }
                }
            }
        }
        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 3; j++) {
                this.gameState = new GameState(gameState);
                this.gameState.turn *= -1;
                if (this.gameState.nextMove(i, j)) {
                    if(this.gameState.findWinner() == this.gameState.turn) {
                        gameState.nextMove(i, j);
                        return;
                    }
                }
            }
        }
        while(!gameState.nextMove(random.nextInt(3) + 1,random.nextInt(3) + 1 )) {
        }
    }
}

class AI_Hard extends Player {

    void makeMove(GameState gameState) {
        Random random = new Random();
        int maxValue = Integer.MIN_VALUE;
        int minValue = Integer.MAX_VALUE;
        int aCoord = 1;
        int bCoord = 1;
        int[][]  bestMove= new int[3][3];
        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 3; j++) {
                bestMove[i-1][j-1] = minmax(i, j, gameState);
            }
        }
//        System.out.println("bestMoves:");
//
//        System.out.println("---------");
//        for (int i = 0; i < 3; i++) {
//            System.out.println("| " + bestMove[i][0] + " " + bestMove[i][1] + " " + bestMove[i][2] + " |");
//        }
//        System.out.println("---------");

        if ( gameState.turn == -1) {
            for (int i = 1; i <= 3; i++) {
                for (int j = 1; j <= 3; j++) {
                    if (bestMove[i-1][j-1] > maxValue && gameState.gameState[i - 1][j - 1] == ' ') {
                        maxValue = bestMove[i-1][j-1];
                        aCoord = i;
                        bCoord = j;
                    }
                }
            }
            System.out.println("Making move: " + aCoord + " " + bCoord);
            gameState.nextMove(aCoord, bCoord);
        } else {
            for (int i = 1; i <= 3; i++) {
                for (int j = 1; j <= 3; j++) {
                    if (bestMove[i-1][j-1] < minValue && gameState.gameState[i - 1][j - 1] == ' ') {
                        minValue = bestMove[i-1][j-1];
                        aCoord = i;
                        bCoord = j;
                    }
                }
            }
            System.out.println("Making move: " + aCoord + " " + bCoord);
            gameState.nextMove(aCoord, bCoord);
        }
    }

    int minmax(int a, int b, GameState gameState) {
        GameState newGameState = new GameState(gameState);
        //this.gameState.nextMove(a, b);
        if (newGameState.nextMove(a, b)) {
            int winner = gameState.findWinner();
            int sum = 0;
            if (winner == 1 || winner == -1) {
                return winner;
            } else if (winner == 2) {
                return 0;
            } else {
                for (int i = 1; i <= 3; i++) {
                    for (int j = 1; j <= 3; j++) {


                        sum += minmax(i, j, newGameState);

                    }
                }
                return sum;
            }
        }
        return 0;


    }


}