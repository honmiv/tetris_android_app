package honmiv.tetris;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    int[][] field;
    Figure figure;
    Figure nextFigure;

    boolean gamePaused;

    TextView fieldTXT;
    TextView filledFieldTXT;
    TextView statsTXT;
    TextView nextFigureTV;
    TextView highScoreTXT;
    TextView nextFigureTextView;

    ImageButton leftBtn;
    ImageButton rightBtn;
    Button leftBtnHitBox;
    Button rightBtnHitBox;

    ImageButton rotateBtn;
    ImageButton downBtn;

    Button startBtn;
    ImageButton pauseBtn;
    ImageButton finishBtn;

    SharedPreferences savedData;
    Editor savedDataEditor;

    long screenUpdateFrequency;

    boolean leftBtnPressed;
    boolean leftBtnBlocked;
    long leftBtnDelay;

    boolean rightBtnPressed;
    boolean rightBtnBlocked;
    long rightBtnDelay;

    boolean rotateBtnPressed;

    boolean figureMoveBlocked;

    long startFigureMovePeriod;
    long figureMovePeriod;
    long tmpFigureMovePeriod;

    Typeface typeFace;
    Typeface brickTypeFace;

    int moveResult;
    boolean gameOver;

    Runnable gameRunning;
    Runnable leftBtnRun;
    Runnable rightBtnRun;


    boolean movedToLeft;
    boolean movedToRight;

    int points;
    int highScore;
    int level;

    private static final String KEY_HIGHSCORE = "HIGH_SCORE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        savedData = getPreferences(MODE_PRIVATE);
        savedDataEditor = savedData.edit();

        movedToLeft = false;
        movedToRight = false;

        fieldTXT = (TextView) findViewById(R.id.fieldTextView);
        filledFieldTXT = (TextView) findViewById(R.id.filledFieldTextView);
        nextFigureTV = (TextView) findViewById(R.id.nextFigureTV);
        nextFigureTextView =(TextView)findViewById(R.id.nextFigureTextView);

        typeFace = Typeface.createFromAsset(getAssets(), "PTM55F.ttf");
        brickTypeFace = Typeface.createFromAsset(getAssets(), "BrickGame.ttf");

        fieldTXT.setTypeface(brickTypeFace);
        nextFigureTV.setTypeface(brickTypeFace);
        nextFigureTextView.setTypeface(typeFace);

        leftBtn = (ImageButton) findViewById(R.id.leftBtn);
        rightBtn = (ImageButton) findViewById(R.id.rightBtn);
        leftBtnHitBox = (Button) findViewById(R.id.leftBtnHitBox);
        rightBtnHitBox = (Button) findViewById(R.id.rightBtnHitBox);

        rotateBtn = (ImageButton) findViewById(R.id.rotateBtn);
        downBtn = (ImageButton) findViewById(R.id.downBtn);
        statsTXT = (TextView) findViewById(R.id.statsTextView);
        highScoreTXT = (TextView) findViewById(R.id.highScoreTextView);
        startBtn = (Button) findViewById(R.id.startBtn);
        pauseBtn = (ImageButton) findViewById(R.id.pauseBtn);
        finishBtn = (ImageButton) findViewById(R.id.finishBtn);

        statsTXT.setTypeface(typeFace);
        startBtn.setTypeface(typeFace, Typeface.BOLD);

        highScoreTXT.setTypeface(typeFace);

        leftBtnPressed = false;
        leftBtnDelay = 250;
        highScore = 0;
        rightBtnPressed = false;
        rightBtnDelay = 250;

        rotateBtnPressed = false;

        figureMoveBlocked = false;

        screenUpdateFrequency = 15;

        leftBtnHitBox.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!gamePaused) {
                    leftBtnRun = new Runnable() {
                        @Override
                        public void run() {
                            if (leftBtnPressed) {
                                if (movedToLeft && !leftBtnBlocked) {
                                    movedToLeft = false;
                                    leftBtnBlocked = true;
                                } else if (leftBtnBlocked) {
                                    leftBtnBlocked = false;
                                    new Handler().postDelayed(leftBtnRun, leftBtnDelay);
                                }
                                if (leftBtnDelay == 250) {
                                    leftBtnDelay = 65;
                                } else if (leftBtnDelay >= 25)
                                    leftBtnDelay -= 15;
                            }
                        }
                    };
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        leftBtnPressed = true;
                        new Handler().post(leftBtnRun);
                    }
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        leftBtnDelay = 250;
                        leftBtnBlocked = false;
                        leftBtnPressed = false;
                    }
                }
                return false;
            }
        });

        rightBtnHitBox.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!gamePaused) {
                    rightBtnRun = new Runnable() {
                        @Override
                        public void run() {
                            if (rightBtnPressed) {
                                if (movedToRight && !rightBtnBlocked) {
                                    movedToRight = false;
                                    rightBtnBlocked = true;
                                    rightBtnBlocked = true;
                                } else if (rightBtnBlocked) {
                                    rightBtnBlocked = false;
                                    new Handler().postDelayed(rightBtnRun, rightBtnDelay);
                                }
                                if (rightBtnDelay == 250) {
                                    rightBtnDelay = 65;
                                } else if (rightBtnDelay >= 25)
                                    rightBtnDelay -= 15;
                            }
                        }
                    };
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        rightBtnPressed = true;
                        new Handler().post(rightBtnRun);
                    }
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        rightBtnDelay = 250;
                        rightBtnBlocked = false;
                        rightBtnPressed = false;
                    }
                }
                return false;
            }
        });

        rotateBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN && !gamePaused)
                    rotateBtnPressed = true;
                return false;
            }
        });

        downBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!gamePaused) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN)
                        figureMovePeriod = 30;
                    if (event.getAction() == MotionEvent.ACTION_UP ||
                            event.getAction() == MotionEvent.ACTION_CANCEL)
                        figureMovePeriod = tmpFigureMovePeriod;
                }
                return false;
            }
        });

        pauseBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                    if (!gamePaused) {
                        pauseBtn.setImageResource(R.drawable.resume);
                        gamePaused = true;
                    } else if (gamePaused) {
                        pauseBtn.setImageResource(R.drawable.pause);
                        gamePaused = false;
                        new Handler().post(gameRunning);
                    }
                return false;
            }
        });

        finishBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    gameOver = true;
                    gameRunning = null;
                    if (gamePaused) {
                        gamePaused = false;
                        pauseBtn.setImageResource(R.drawable.pause);
                        updateScreen();
                    }
                }
                return false;
            }
        });
        FullScreenMode();
    }

    @Override
    protected void onResume() {
        FullScreenMode();
        super.onResume();
    }

    public void FullScreenMode() {
        //fullscreen on 4.4+ devices
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int UI_OPTIONS = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                    View.SYSTEM_UI_FLAG_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
            getWindow().getDecorView().setSystemUiVisibility(UI_OPTIONS);
        } else
            //fullscreen on 4.1-4.3 devices
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public void startBtnClicked(View view) {
        startBtn.setVisibility(View.INVISIBLE);
        statsTXT.setVisibility(View.VISIBLE);
        leftBtn.setVisibility(View.VISIBLE);
        rightBtn.setVisibility(View.VISIBLE);
        downBtn.setVisibility(View.VISIBLE);
        rotateBtn.setVisibility(View.VISIBLE);
        pauseBtn.setVisibility(View.VISIBLE);
        finishBtn.setVisibility(View.VISIBLE);
        nextFigureTextView.setVisibility(View.VISIBLE);
        pauseBtn.setImageResource(R.drawable.pause);
        filledFieldTXT.setTypeface(brickTypeFace, Typeface.NORMAL);

        moveResult = 0;
        gameOver = false;
        gamePaused = false;
        startFigureMovePeriod = 220;
        figureMovePeriod = tmpFigureMovePeriod = startFigureMovePeriod;

        if (savedData.contains(KEY_HIGHSCORE))
            highScore = savedData.getInt(KEY_HIGHSCORE, 0);
        else highScore = 0;
        points = 0;
        level = 0;

        field = new int[24][10];
        for (int i = 0; i < 24; i++)
            for (int j = 0; j < 10; j++)
                field[i][j] = 0;

        figure = new Figure(new Random().nextInt(5), field);
        nextFigure = new Figure(new Random().nextInt(5), field);

        gameRunning = new Runnable() {
            @Override
            public void run() {
                if (!figureMoveBlocked) {
                    int moveResult = figure.MoveDown(field);
                    if (moveResult == 2) {
                        gameOver = true;
                        gameRunning = null;
                    } else if (moveResult <= 0) {
                        points += moveResult  * (-1);
                        if (moveResult < -10) {
                            points += (moveResult  * (-1) / 10 - 1) * 10;
                        }
                        level = points / 100;
                        if (points > highScore) {
                            highScore = points;
                            savedDataEditor.putInt(KEY_HIGHSCORE, highScore);
                            savedDataEditor.apply();
                            savedDataEditor.commit();
                        }
                        if (startFigureMovePeriod - level * 20 > 0) {
                            figureMovePeriod = startFigureMovePeriod - level * 20;
                            tmpFigureMovePeriod = figureMovePeriod;
                        }
                        figure = nextFigure;
                        figureMovePeriod = tmpFigureMovePeriod;
                        nextFigure = new Figure(new Random().nextInt(5), field);
                    } else if (moveResult ==1) {
                        figureMoveBlocked = true;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                figureMoveBlocked = false;
                            }
                        }, figureMovePeriod);
                    }
                }
                if (!movedToLeft) {
                    movedToLeft = true;
                    figure.MoveLeft(field);
                    new Handler().postDelayed(leftBtnRun, leftBtnDelay);
                }
                if (!movedToRight) {
                    movedToRight = true;
                    figure.MoveRight(field);
                    new Handler().postDelayed(rightBtnRun, rightBtnDelay);
                }
                if (rotateBtnPressed) {
                    figure.rotate(field);
                    rotateBtnPressed = false;
                }
                updateScreen();
            }
        };
        updateScreen();
    }

    @Override
    protected void onPause() {
        pauseBtn.setImageResource(R.drawable.resume);
        gamePaused = true;
        super.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    void updateScreen() {
        if (!gameOver) {
            String str = "";
            String strFilled = "";
            int[][] shownField = new int[24][10];
            for (int i = 4; i < 24; i++) {
                for (int j = 0; j < 10; j++) {
                    shownField[i][j] = field[i][j];
                    for (int k = 0; k < 4; k++)
                        if (figure.fCoords[k].x == j && figure.fCoords[k].y == i) {
                            shownField[i][j] = 1;
                            break;
                        }
                    if (shownField[i][j] == 1) {
                            str += " ";
                            strFilled += "A";
                    } else {
                            str += "B";
                            strFilled += " ";
                    }
                }
                if (i != 23) {
                    str += "\n";
                    strFilled += "\n";
                }
            }

            String nextFigureStr = "";
            int dim = nextFigure.corners[1].x - nextFigure.corners[0].x + 1;
            int[][] figureSquare = new int[dim][dim];
            for (int i = 0; i < dim; i++) {
                for (int j = 0; j < dim; j++) {
                    figureSquare[i][j] = 0;
                }
            }
            for (int i = 0; i < 4; i++) {
                figureSquare[nextFigure.fCoords[i].y - nextFigure.corners[0].y][nextFigure.fCoords[i].x - nextFigure.corners[0].x] = 1;
            }
            for (int i = 0; i < dim; i++) {
                for (int j = 0; j < dim; j++) {
                    if (figureSquare[i][j] == 1) nextFigureStr += "A";
                    else nextFigureStr += " ";
                }
                nextFigureStr += "\n";
            }
            fieldTXT.setText(str);
            filledFieldTXT.setText(strFilled);
            statsTXT.setText(getString(R.string.pointText) + ": " + String.valueOf(points) + "\n" + getString(R.string.levelText) + ": " + String.valueOf(level));
            nextFigureTV.setText(nextFigureStr);
            highScoreTXT.setText(getString(R.string.highScoreText) + ":\n" + String.valueOf(highScore));
        } else {
            leftBtn.setVisibility(View.INVISIBLE);
            rightBtn.setVisibility(View.INVISIBLE);
            downBtn.setVisibility(View.INVISIBLE);
            rotateBtn.setVisibility(View.INVISIBLE);
            statsTXT.setVisibility(View.INVISIBLE);
            pauseBtn.setVisibility(View.INVISIBLE);
            finishBtn.setVisibility(View.INVISIBLE);
            nextFigureTextView.setVisibility(View.INVISIBLE);
            startBtn.setVisibility(View.VISIBLE);

            nextFigureTV.setText("");
            highScoreTXT.setText("");

            fieldTXT.setText(filledFieldTXT.getText());
            filledFieldTXT.setTypeface(typeFace, Typeface.BOLD);
            filledFieldTXT.setText(getString(R.string.gameOverText) + ": " + String.valueOf(points));
        }
        if (!gamePaused)
            new Handler().postDelayed(gameRunning, screenUpdateFrequency);
    }
}