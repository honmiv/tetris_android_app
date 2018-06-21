package honmiv.tetris;

import android.graphics.Point;
import java.util.Random;

public class Figure {
    int type;
    boolean rightRot;
    public Point[] fCoords = new Point[4];
    private Point[] possible_fCoords = new Point[4];
    public Point[] corners = new Point[2];


    Figure(int type, int[][] field) {
        for (int i = 0; i < 4; i++)
            fCoords[i] = new Point(0, 0);
        for (int i = 0; i < 2; i++)
            corners[i] = new Point(0, 0);
        for (int i = 0; i < 4; i++)
            possible_fCoords[i] = new Point(0, 0);
        this.type = type;
        Random random = new Random();
        int colNum = field[0].length;

        switch (type) {
            case 1: { // Квадрат
                this.type = 0; // не вращать
                rightRot = true;

                fCoords[0].set(colNum / 2 - 1, 2);
                fCoords[1].set(fCoords[0].x + 1, 2);
                fCoords[2].set(fCoords[0].x, 3);
                fCoords[3].set(fCoords[0].x + 1, 3);

                corners[0].set(colNum / 2 - 1, 2);
                corners[1].set(fCoords[0].x + 1, 3);
            }
            break;
            case 0: { // вертикальная прямая
                this.type = 1; // вращать вправо-лево
                rightRot = true;

                fCoords[0].set(colNum / 2, 0);
                fCoords[1].set(fCoords[0].x, 1);
                fCoords[2].set(fCoords[0].x, 2);
                fCoords[3].set(fCoords[0].x, 3);

                corners[0].set(colNum / 2 - 1, 0);
                corners[1].set(colNum / 2 + 2, 3);
            }
            break;
            case 2:
            case 3:
            case 4:
            case 5:
                switch (random.nextInt(5)) {
                    case 0: { // т - образная
                        this.type = 2; // вращать только вправо
                        rightRot = true;

                        corners[0].set(colNum / 2 - 1, 1);
                        corners[1].set(colNum / 2 + 1, 3);

                        fCoords[0].set(colNum / 2 - 1, 2);
                        fCoords[1].set(fCoords[0].x + 1, 2);
                        fCoords[2].set(fCoords[0].x + 2, 2);
                        fCoords[3].set(fCoords[0].x + 1, 3);
                    }
                    break;
                    case 1: { // s - образная левая
                        this.type = 1; // вращать вправо-лево
                        rightRot = true;

                        fCoords[0].set(colNum / 2 - 1, 2);
                        fCoords[1].set(fCoords[0].x + 1, 2);
                        fCoords[2].set(fCoords[0].x + 1, 3);
                        fCoords[3].set(fCoords[0].x + 2, 3);

                        corners[0].set(colNum / 2 - 1, 1);
                        corners[1].set(fCoords[0].x + 2, 3);
                    }
                    break;
                    case 2: { // s - образная правая
                        this.type = 1; // вращать вправо-лево
                        rightRot = true;

                        fCoords[0].set(colNum / 2 - 1, 3);
                        fCoords[1].set(fCoords[0].x + 1, 2);
                        fCoords[2].set(fCoords[0].x + 1, 3);
                        fCoords[3].set(fCoords[0].x + 2, 2);

                        corners[0].set(colNum / 2 - 1, 1);
                        corners[1].set(fCoords[0].x + 2, 3);
                    }
                    break;
                    case 3: { // г - образная левая
                        this.type = 2; // вращать только вправо
                        rightRot = true;

                        fCoords[0].set(colNum / 2 - 1, 2);
                        fCoords[1].set(fCoords[0].x + 1, 2);
                        fCoords[2].set(fCoords[0].x + 2, 2);
                        fCoords[3].set(fCoords[0].x + 2, 3);

                        corners[0].set(colNum / 2 - 1, 1);
                        corners[1].set(fCoords[0].x + 2, 3);
                    }
                    break;
                    case 4: { // г - образная правая
                        this.type = 2; // вращать только вправо
                        rightRot = true;

                        fCoords[0].set(colNum / 2 - 1, 2);
                        fCoords[1].set(fCoords[0].x + 1, 2);
                        fCoords[2].set(fCoords[0].x + 2, 2);
                        fCoords[3].set(fCoords[0].x, 3);

                        corners[0].set(colNum / 2 - 1, 1);
                        corners[1].set(fCoords[0].x + 2, 3);
                    }
                    break;
                }
                break;
        }
    }

    void MoveLeft(int[][] field) {
        boolean canMove = true;
        for (int i = 0; i < 4; i++) {
            if (fCoords[i].x == 0 || field[fCoords[i].y][fCoords[i].x - 1] == 1) {
                canMove = false;
                break;
            }
        }
        if (canMove) {
            for (int i = 0; i < 4; i++)
                fCoords[i].x--;
            for (int i = 0; i < 2; i++)
                corners[i].x--;
        }
    }

    void MoveRight(int[][] field) {
        boolean canMove = true;
        for (int i = 0; i < 4; i++) {
            if (fCoords[i].x == 9 || field[fCoords[i].y][fCoords[i].x + 1] == 1) {
                canMove = false;
                break;
            }
        }
        if (canMove) {
            for (int i = 0; i < 4; i++)
                fCoords[i].x++;
            for (int i = 0; i < 2; i++)
                corners[i].x++;
        }
    }

    int MoveDown(int[][] field) {
        int result = 0;
        boolean canmove = true;
        boolean delete = true;
        for (int i = 0; i < 4; i++) {
            if (fCoords[i].y == 23) { // достигло дна поля
                canmove = false;
            } else if (field[fCoords[i].y + 1][fCoords[i].x] == 1) { // столкновение с другими фигурами
                for (int j = 0; j < 4; j++)
                    if (fCoords[j].y < 4)
                        return 2; // конец игры, если фигура не может выйти на поле
                canmove = false;
            }
            if (canmove == false) {
                for (int j = 0; j < 4; j++)
                    field[fCoords[j].y][fCoords[j].x] = 1; // записываем фигуру на поле
                break;
            }
        }
        if (canmove) {
            result = 1;
            for (int i = 0; i < 4; i++)
                fCoords[i].y++;
            for (int i = 0; i < 2; i++)
                corners[i].y++;
        } else {
            for (int removable = 1; removable < 24; removable++) {
                delete=true;
                for (int j = 0; j < 10; j++) {
                    if (field[removable][j] == 0) {
                        delete = false;
                    }
                }
                if (delete) {
                    result-=10;
                    for (int i = removable; i > 0; i--)
                        for (int j = 0; j < 10; j++)
                            field[i][j] = field[i - 1][j];
                }
            }

        }
        return result;
    }

    void rotate(int[][] field) {

        int dim = corners[1].x - corners[0].x + 1;
        int[][] square = new int[dim][dim];
        int[][] possibleSquare = new int[dim][dim];

        int coordCount = 0;

        for (int i = 0; i < dim; i++)
            for (int j = 0; j < dim; j++) {
                square[i][j] = 0;
                possibleSquare[i][j] = 0;
            }

        for (int i = 0; i < 4; i++) {
            possibleSquare[fCoords[i].y - corners[0].y][fCoords[i].x - corners[0].x] = 1;
            square[fCoords[i].y - corners[0].y][fCoords[i].x - corners[0].x] = 1;
        }


        if (type == 2 || (type == 1 && rightRot)) {
            //rightRot
            for (int i = 0; i < dim; i++)
                for (int j = 0; j < dim; j++) {
                    square[j][i] = possibleSquare[dim - i - 1][j];
                }
            rightRot = false;
        } else if (type == 1 && !rightRot) {
            //leftRot
            for (int i = 0; i < dim; i++)
                for (int j = 0; j < dim; j++) {
                    square[j][i] = possibleSquare[i][dim - j - 1];
                }
            rightRot = true;
        }

        for (int i = 0; i < dim; i++)
            for (int j = 0; j < dim; j++) {
                if (square[i][j] == 1) {
                    possible_fCoords[coordCount].set(j + corners[0].x, i + corners[0].y);
                    coordCount++;
                }
            }

        Boolean canRotate = true;
        int possibleCorner = 0;

        if (corners[0].x < 0)
            possibleCorner = 0 - corners[0].x;
        else if (corners[1].x > 9)
            possibleCorner = 9 - corners[1].x;
        else if (corners[1].y > 23)
            canRotate = false;

        if (canRotate) {
            for (int i = 0; i < 4; i++) {
                if (field[possible_fCoords[i].y][possible_fCoords[i].x + possibleCorner] == 1) { // выход за границы
                    canRotate = false;
                    break;
                }
            }
        }
        if (canRotate) {
            for (int i = 0; i < 4; i++)
                possible_fCoords[i].set(possible_fCoords[i].x + possibleCorner, possible_fCoords[i].y);
            corners[0].x += possibleCorner;
            corners[1].x += possibleCorner;
        }
        if (canRotate) {
            for (int i = 0; i < 4; i++)
                fCoords[i].set(possible_fCoords[i].x, possible_fCoords[i].y);
        }
    }
}