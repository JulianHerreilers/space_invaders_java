package GameDev6;

import edu.princeton.cs.introcs.StdAudio;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;

public class MainGame extends Canvas implements Runnable { //See External Sources entry 8 and 9

    //Frame Size Variables and Background Variables
    public static final int WIDTH = 640, HEIGHT = WIDTH / 12 * 9;
    public static final int starsNUM = 50;
    public static int[] starsX = new int[starsNUM];
    public static int[] starsY = new int[starsNUM];

    //Reloading Variables
    public static final int reloadtime = 1000; // Reload Time in ms
    public static int timeAtSHOT = (int) System.currentTimeMillis() - reloadtime;
    public static int timeAtCHECK = (int) System.currentTimeMillis();
    public static int timeSINCESHOT;

    //MainGame and MainGame Object Variables
    public static int lvl = 0;
    public static final int critterVelXStart = 5;
    public static final int critterHPStart = 1;
    public static int critterVelX = critterVelXStart;
    public static int critterNextVelX = critterVelXStart;
    public static int critterHP = critterHPStart;
    public static int numberofupgrades = 1;
    private boolean CANSPAWN = true;
    private boolean CANUPGRADECRITTER = false;

    //Instance of ObjectManager to manage various game objects
    private ObjectManager objectManager = new ObjectManager();

    //Variables to control the state of the game
    public enum STATE {
        Menu,
        Help,
        Game,
        GameOver,
        Stats
    }
    public STATE gameState = STATE.Menu;

    //Scoring System Variables
    public static int score = 0;
    public static int highscore = 0;
    public static int attempts = 0;
    public static int sumofscores = 0;
    public static double averagescore = 0;

    private boolean running = false; //Dictates if game runs or stops
    private Thread thread;

    public MainGame() {

        //Setting up the window on which the Program is displayed
        StdDraw.enableDoubleBuffering();
        StdDraw.setCanvasSize(WIDTH, HEIGHT);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);

        //Starts in Menu
        StdAudio.loop("gamemusic.wav"); //See External Sources entry 3 & 7

        //User remains in menuCONTROl until the game begins
        menuCONTROL();

        displayFrame();
        start();
    }


    /****
     Menu Functions
     ****/

    //menuCONTROL manages which menu window to call and it displays a certain window based on the game state
    public void menuCONTROL() {
        while (gameState != MainGame.STATE.Game) {
            while (gameState == MainGame.STATE.Help) {
                menuHELP();
            }
            while (gameState == MainGame.STATE.Stats) {
                menuSTATS();
            }
            while (gameState == MainGame.STATE.Menu) {
                menuMAIN();
            }
        }
    }

    //menuMAIN the function of the default window in the program and allows the user to access the other menu windows, start the game loop or quit the program
    public void menuMAIN() { //See External Sources entry 4
        //Display of Main Menu
        resetMainMenu();
        StdDraw.setPenColor(Color.white);
        StdDraw.text(MainGame.WIDTH / 2, (MainGame.HEIGHT * 2 / 3), "COSMIC CONQUEST");
        StdDraw.filledRectangle(MainGame.WIDTH / 2, (MainGame.HEIGHT * 2 / 3) - 60, 45, 11);
        StdDraw.filledRectangle(MainGame.WIDTH / 2, (MainGame.HEIGHT * 2 / 3) - 100, 45, 11);
        StdDraw.filledRectangle(MainGame.WIDTH / 2, (MainGame.HEIGHT * 2 / 3) - 140, 45, 11);
        StdDraw.filledRectangle(MainGame.WIDTH / 2, (MainGame.HEIGHT * 2 / 3) - 180, 45, 11);
        StdDraw.text((MainGame.WIDTH * 3 / 4), (MainGame.HEIGHT * 7 / 8), "CURRENT SESSION HIGH SCORE: " + highscore);
        StdDraw.setPenColor(Color.black);
        StdDraw.text(MainGame.WIDTH / 2, (MainGame.HEIGHT * 2 / 3) - 60, "PLAY");
        StdDraw.text(MainGame.WIDTH / 2, (MainGame.HEIGHT * 2 / 3) - 100, "HELP");
        StdDraw.text(MainGame.WIDTH / 2, (MainGame.HEIGHT * 2 / 3) - 140, "STATS");
        StdDraw.text(MainGame.WIDTH / 2, (MainGame.HEIGHT * 2 / 3) - 180, "EXIT");

        StdDraw.show();
        StdDraw.pause(40);

        //Mouse and Key Inputs to either start MainGame Loop or Go to a Different Menu Window
        //Options are selected if Mouse is clicked and the cursor is located inside the option's "box"

        if (StdDraw.isMousePressed()) {

            //Mouse Input to start MainGame
            if ((StdDraw.mouseX() >= MainGame.WIDTH / 2 - 45) && (StdDraw.mouseX() <= MainGame.WIDTH / 2 + 45) && (StdDraw.mouseY() >= (MainGame.HEIGHT * 2 / 3) - 60 - 11) && (StdDraw.mouseY() <= (MainGame.HEIGHT * 2 / 3) - 60 + 11)) {
                gameState = STATE.Game;

                objectManager.addPlayer(new ObjectManager.Player(100, 50, 0, 0, ID.Player, 1));
                critterSpawner();

                //Starting Positions of the stars in the background are randomly generated each time a new game begins
                for (int i = 0; i < starsNUM; i++) {
                    starsX[i] = (int) (Math.random() * WIDTH);
                }
                for (int j = 0; j < starsNUM; j++) {
                    starsY[j] = (int) (Math.random() * HEIGHT);
                }
            }

            //Mouse Input to go to Help Window
            if ((StdDraw.mouseX() >= MainGame.WIDTH / 2 - 45) && (StdDraw.mouseX() <= MainGame.WIDTH / 2 + 45) && (StdDraw.mouseY() >= (MainGame.HEIGHT * 2 / 3) - 100 - 11) && (StdDraw.mouseY() <= (MainGame.HEIGHT * 2 / 3) - 100 + 11)) {
                gameState = STATE.Help;
            }

            //Mouse Input to go to Stats Window
            if ((StdDraw.mouseX() >= MainGame.WIDTH / 2 - 45) && (StdDraw.mouseX() <= MainGame.WIDTH / 2 + 45) && (StdDraw.mouseY() >= (MainGame.HEIGHT * 2 / 3) - 140 - 11) && (StdDraw.mouseY() <= (MainGame.HEIGHT * 2 / 3) - 140 + 11)) {
                gameState = STATE.Stats;
            }

            ////Mouse Input to go to Quit Program
            if ((StdDraw.mouseX() >= MainGame.WIDTH / 2 - 45) && (StdDraw.mouseX() <= MainGame.WIDTH / 2 + 45) && (StdDraw.mouseY() >= (MainGame.HEIGHT * 2 / 3) - 180 - 11) && (StdDraw.mouseY() <= (MainGame.HEIGHT * 2 / 3) - 180 + 11)) {
                System.exit(4);
            }
        }

        //Key Input to go to Quit Program
        if (StdDraw.isKeyPressed(81)) {
            System.out.println("QUIT");
            System.exit(3);
        }
    }

    //Function that
    public void menuHELP() {
        //Display of Help Window
        resetMainMenu();
        StdDraw.setPenColor(Color.white);
        StdDraw.text(MainGame.WIDTH / 2, (MainGame.HEIGHT * 3 / 4), "HELP");
        StdDraw.rectangle(MainGame.WIDTH / 2, (MainGame.HEIGHT * 3 / 4), 29, 11);
        StdDraw.text(MainGame.WIDTH / 2, (MainGame.HEIGHT * 3 / 4) - 60, "USE A AND D TO MOVE LEFT AND RIGHT");
        StdDraw.text(MainGame.WIDTH / 2, (MainGame.HEIGHT * 3 / 4) - 100, "AIM WITH YOUR MOUSE AND PRESS SPACEBAR TO SHOOT");
        StdDraw.text(MainGame.WIDTH / 2, (MainGame.HEIGHT * 3 / 4) - 140, "YOUR SPACESHIP INDICATES WHEN YOU CAN FIRE AGAIN");
        StdDraw.text(MainGame.WIDTH / 2, (MainGame.HEIGHT * 3 / 4) - 180, "KILLING A WAVE SPAWNS A FASTER WAVE, EVERY 4TH WAVE IS STRONGER");
        StdDraw.text(MainGame.WIDTH / 2, (MainGame.HEIGHT * 3 / 4) - 220, "SEE HOW LONG YOU CAN LAST!!!");
        StdDraw.text(MainGame.WIDTH / 2, (MainGame.HEIGHT * 3 / 4) - 280, "NOTE: PRESSING Q ENDS THE PROGRAM");
        StdDraw.text((MainGame.WIDTH / 8), 20, "BACK");
        StdDraw.rectangle((MainGame.WIDTH / 8), 21, 22, 11);
        StdDraw.show();
        StdDraw.pause(40);

        //Mouse and Key Inputs
        //Options are once again selected if Mouse is clicked and the cursor is located inside the option's "box"

        //Mouse Input to return to the Main Menu
        if (StdDraw.isMousePressed()) {
            if ((StdDraw.mouseX() >= MainGame.WIDTH / 8 - 11) && (StdDraw.mouseX() <= MainGame.WIDTH / 8 + 11) && (StdDraw.mouseY() >= 21 - 11) && (StdDraw.mouseY() <= 21 + 11)) {
                gameState = STATE.Menu;
            }
        }

        //Key Input to Quit Program
        if (StdDraw.isKeyPressed(81)) {
            System.out.println("QUIT");
            System.exit(3);
        }
    }

    //menuSTATS function displays various statistics of the player during the current session of the program
    public void menuSTATS() {
        //Display of Stats Menu
        resetMainMenu();
        StdDraw.setPenColor(Color.white);
        StdDraw.text(MainGame.WIDTH / 2, (MainGame.HEIGHT * 2 / 3), "STATS");
        StdDraw.rectangle(MainGame.WIDTH / 2, (MainGame.HEIGHT * 2 / 3), 29, 11);
        StdDraw.text(MainGame.WIDTH / 2, (MainGame.HEIGHT * 2 / 3) - 60, "HIGH SCORE THIS SESSION: " + highscore);
        StdDraw.text(MainGame.WIDTH / 2, (MainGame.HEIGHT * 2 / 3) - 100, "AVERAGE SCORE THIS SESSION: " + averagescore);
        StdDraw.text(MainGame.WIDTH / 2, (MainGame.HEIGHT * 2 / 3) - 140, "ATTEMPTS: " + attempts);
        StdDraw.text((MainGame.WIDTH / 8), 20, "BACK");
        StdDraw.rectangle((MainGame.WIDTH / 8), 21, 22, 11);
        StdDraw.show();
        StdDraw.pause(40);

        //Mouse and Key Inputs
        //Options are once again selected if Mouse is clicked and the cursor is located inside the option's "box"

        //Mouse Input to return to the Main Menu
        if (StdDraw.isMousePressed()) {
            if ((StdDraw.mouseX() >= MainGame.WIDTH / 8 - 11) && (StdDraw.mouseX() <= MainGame.WIDTH / 8 + 11) && (StdDraw.mouseY() >= 21 - 11) && (StdDraw.mouseY() <= 21 + 11)) {
                gameState = STATE.Menu;
            }
        }
        //Key Input to quit program
        if (StdDraw.isKeyPressed(81)) {
            System.out.println("QUIT");
            System.exit(3);
        }
    }

    //This function clears any existing graphics on the screen before a Window redisplays its options - Useful when swapping between Menu Windows
    public void resetMainMenu() {
        StdDraw.clear();
        StdDraw.setPenColor(Color.black);
        StdDraw.filledRectangle(0, 0, MainGame.WIDTH, MainGame.HEIGHT);
    }

    /****
     Start of MainGame Functions
     ****/

    //Starts MainGame
    public synchronized void start() {
        thread = new Thread(this); //This instance of our game
        thread.start();
        running = true; //Allows the MainGame Loop to start when run is called
    }

    //Stops MainGame - Ensure the game ends correctly
    public synchronized void stop() {
        try {
            thread.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //The run function starts and controls the MainGame Loop
    public void run() { //See External Sources entry 2
        {
            this.requestFocus(); // Don't need to click on screen to use keyboard //See External Sources entry 9
            long lastTime = System.nanoTime();
            double delta = 0;
            while (running) {
                long now = System.nanoTime();
                delta = (now - lastTime) * 0.000_000_001;
                lastTime = now;

                //MainGame Loop which calls various functions to update/display objects and game variables - these functions will be explained at their appropriate positions
                //MainGame Loop Continuously runs until the program is ended
                if (running) {
                    update(delta);
                    bulletCollision();
                    checkGAMEOVER();
                    displayFrame();

                    //Reload Timer by comparing time since last shot and time at each other instance in the game loop
                    timeSINCESHOT = timeAtCHECK - timeAtSHOT;

                    //LEFT PLAYER MOVEMENT WITH KEY "A"
                    if (StdDraw.isKeyPressed(65)) {
                        objectManager.getPlayers().get(0).setVelX(-8);
                    }

                    //RIGHT PLAYER MOVEMENT WITH KEY "D"
                    if (StdDraw.isKeyPressed(68)) {
                        objectManager.getPlayers().get(0).setVelX(8);
                    }

                    //PLAYER HAS 0 VELOCITY IF BOTH "A" AND "D" ARE PRESSED
                    if ((!(StdDraw.isKeyPressed(68))) && (!(StdDraw.isKeyPressed(65))) || ((StdDraw.isKeyPressed(68)) && (StdDraw.isKeyPressed(65)))) {
                        objectManager.getPlayers().get(0).setVelX(0);
                    }

                    //SHOOTS A BULLET FROM PLAYER POSITION IF SPACEBAR IS PRESSED & BULLET HAS RELOADED
                    if (StdDraw.isKeyPressed(32) && (timeSINCESHOT >= reloadtime)) {
                        objectManager.addBullet(new ObjectManager.Bullet(objectManager.getPlayers().get(0).getX(), objectManager.getPlayers().get(0).getY(), (int) (15 * Math.cos(bulletAngle())), (int) (15 * Math.sin(bulletAngle())), ID.Bullet, 1));
                        timeSINCESHOT = 0;
                        timeAtSHOT = (int) System.currentTimeMillis();
                        StdAudio.play("shoot.wav"); //See External Sources entry 2
                    }

                    //IF "Q" IS WHILE A GAME IS IN SESSION IT WILL DISPLAY "GAMEOVER"
                    if (StdDraw.isKeyPressed(81)) {
                        gameState = STATE.GameOver;
                    }

                    timeAtCHECK = (int) System.currentTimeMillis();

                    //If an object has 0 health then isDead is set to true which will be checked for in these for loops. If IsDead is true the object is removed from the list
                    objectManager.bullets.removeIf(GameObject::isDead);
                    objectManager.critters.removeIf(GameObject::isDead);

                    //Spawns a new wave of enemies if allowed
                    critterSpawner();

                    //Controls Wave system as each wave consists of 8 enemies and if all are dead then a new wave can be spawned
                    if (score == lvl * 8) {
                        CANSPAWN = true;

                        //Controls a new wave of enemies can be "upgraded"(with more health)
                        if (score == numberofupgrades * 32) {


                            CANUPGRADECRITTER = true;
                            numberofupgrades++;
                        }
                    }
                }
            }
            stop();
        }
    }

    /****
     Start of MainGame Functions
     ****/

    //Function updates the player,critter and bullet variables
    private void update(double deltaTime) {

        //Creates a temporary player game object that loops through the List of Players(only one) and updates the various variables of the Player (See ObjectManager>Player Class)
        for (GameObject player : objectManager.getPlayers()) {
            ((ObjectManager.Player) player).update(deltaTime);
        }

        //Creates a temporary bullet game object that loops through the List of Bullets and updates the variables of the corresponding Bullet
        for (GameObject bullet : objectManager.getBullets()) {
            ((ObjectManager.Bullet) bullet).update(deltaTime);
        }

        //Critter Update Function
        int vel = critterVelX;
        clamp(vel);

    }

    //clamp function ensures that the critters due not move off the screen and rather bounce of the edge
    public void clamp(int Vel) {
        int tempX;
        int tempSize;

        for (GameObject critter : objectManager.getCritters()) {
            tempX = critter.getX(); //Midpoint of the critter
            tempSize = 15;  //HalfSize of the Square

            //checks that if any of the critters hit the wall, every critter's velocity is set to the opposite direction

            //Checks if hits right wall
            if (tempX > MainGame.WIDTH - tempSize) {
                for (GameObject critter1 : objectManager.getCritters()) {
                    critter1.setVelX(-1 * Vel);
                    critter1.setY(critter1.getY() - 15);
                }

                //Checks if hits left wall
            } else if (tempX < tempSize) {
                for (GameObject critter1 : objectManager.getCritters()) {
                    critter1.setVelX(Vel);
                    critter1.setY(critter1.getY() - 15);
                }
            }
        }
    }

    /****
     Display Method
     ****/

    //displayFrame function controls the graphics of the screen by displaying the MainGame Screen and the various MainGame Game-Related Objects
    private void displayFrame() { //See External Sources entry 4

        //Clears the screen and draws the "black sky" on the window
        StdDraw.clear();
        StdDraw.setPenColor(Color.black);
        StdDraw.filledRectangle(0, 0, WIDTH, HEIGHT);

        //Draws the Stars in the background
        StdDraw.setPenColor(Color.white);
        for (int n = 0; n < starsNUM; n++) {
            StdDraw.filledCircle(starsX[n], starsY[n], 1);
            starsY[n] -= 5;

            //Stars appear to be moving downwards, if they hit the bottom on the window they are projected again at the top of the window
            if (starsY[n] <= 0) {
                starsY[n] = HEIGHT;
            }
        }

        //Creates a temporary player game object that loops through the List of Players(only one) and displays the Player (See ObjectManager>Player Class)
        for (GameObject player : objectManager.getPlayers()) {
            player.display();
        }

        //Creates a temporary bullet game object that loops through the List of Bullets and displays the corresponding bullet (See ObjectManager>Bullet Class)
        for (GameObject bullet : objectManager.getBullets()) {
            bullet.display();
        }

        //Creates a temporary critter game object that loops through the List of Critters and displays the corresponding critter (See ObjectManager>Critter Class)
        for (GameObject critter : objectManager.getCritters()) {
            critter.display();
        }

        //Displays the Player's current score and Wave in the top corner
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(32, HEIGHT - 13, "Score: " + score);
        StdDraw.text(32, HEIGHT - 27, "Wave: " + lvl);

        //Displays a crosshair at the Mouse's current position
        StdDraw.setPenColor(Color.white);
        StdDraw.filledRectangle(StdDraw.mouseX(), StdDraw.mouseY(), 1, 5);
        StdDraw.filledRectangle(StdDraw.mouseX(), StdDraw.mouseY(), 5, 1);

        //StdDraw shows all objects that need to be displayed, pauses the screen for a short instance and then clears the screen
        StdDraw.show();
        StdDraw.pause(42);
        StdDraw.clear();

    }

    /****
     Collision and Spawner Functions
     ****/

    //playerCollision function checks if the player and critters have collided using Rectangular Collision Detection
    private void playerCollision() {
        int py, px, cx, cy;
        double pA;

        //A temporary player position is compared to a temporary critter that loops through the position of all the critters
        for (GameObject player : objectManager.getPlayers()) {
            px = player.getX();
            py = player.getY();
            pA = player.getRotationAngle();
            for (GameObject critter : objectManager.getCritters()) {
                cx = critter.getX();
                cy = critter.getY();

                //Breaking Collision Up into 3 Categories(by dividing the first 2 quadrants into 3) for more accuracy
                //1st & 3rd Section Rectangular Collision Detection
                if ((pA >= 0 && pA < Math.PI / 3) || (pA >= 2 * Math.PI / 3 && pA <= Math.PI)) {

                    if (((px + 30 * Math.cos(pA + 30) >= cx - 15) && (px - 30 <= cx + 15)) && ((py + 30 >= cy - 15) && (py - 32 <= cy + 15))) {
                        gameState = STATE.GameOver;
                        break;
                    }
                }

                //2nd Section Rectangular Collision Detection
                if (pA >= Math.PI / 3 && pA < 2 * Math.PI / 3) {

                    if (((px + 30 * Math.cos(pA + 60) >= cx - 15) && (px - 30 <= cx + 15)) && ((py + 32 >= cy - 15) && (py - 32 <= cy + 15))) {

                        gameState = STATE.GameOver;
                        break;
                    }
                }
            }
        }
    }

    //bulletCollision function checks if the bullets and critters have collided using Rectangular Collision Detection
    private void bulletCollision() {
        int bx, by, cx, cy;

        //A temporary bullet's position(that loops through the position of all the bullets) is compared to a temporary critter that loops through the position of all the critters
        for (GameObject bullet : objectManager.getBullets()) {
            bx = bullet.getX();
            by = bullet.getY();
            for (GameObject critter : objectManager.getCritters()) {
                cx = critter.getX();
                cy = critter.getY();

                //Rectangular Collision Detection
                if (((bx + 6 >= cx - 15) && (bx - 6 <= cx + 15)) && ((by + 6 >= cy - 15) && (by - 6 <= cy + 15))) {
                    critter.setHp(critter.getHp() - 1);
                    bullet.setHp(critter.getHp() - 1);
                    bullet.isDead = true;
                    if (critter.getHp() == 0) {
                        critter.isDead = true;
                        score++;
                    }

                    StdAudio.play("hit.wav"); //See External Sources entry 3
                    break;
                }
            }
        }
    }

    //bulletAngle determines the angle at which the bullet is projected at when Shot
    public double bulletAngle() {
        double mX = StdDraw.mouseX(); //See External Sources entry 5
        double mY = StdDraw.mouseY();
        int tempX = objectManager.players.get(0).getX();
        int tempY = objectManager.players.get(0).getY();

        //Angle is the angle between the horizontal to the right of the player and the mouse position relative to the Player's current Position
        double angle = Math.atan2(mY - tempY, mX - tempX); //See External Source entry 1

        //Ensures that a large amount of bullets cannot be going in a straight line inline with the Player
        if (angle >= 5*Math.PI /6 || angle<= -Math.PI/2) {
            angle = 3.05;
        } else if (angle < 0.1 && angle >= -Math.PI/2) {
            angle = 0.1;
        }
        return angle;
    }

    //Function that handles the new waves of enemies
    private void critterSpawner() {
        if (MainGame.score == lvl * 8 && CANSPAWN) {
            int gap = 16;

            //Increases the health that the critters will spawn with
            if (CANUPGRADECRITTER) {
                critterHP++;
                CANUPGRADECRITTER = false;
            }

            //Spawns 2 rows of critters with 4 in each row
            critterVelX = critterNextVelX;
            for (int i = 0; i < 4; i++) {
                objectManager.addCritter(new ObjectManager.Critter(gap, HEIGHT - 78, critterVelX, 0, ID.Critter, critterHP));
                gap += 40;
            }
            gap = 16;
            for (int i = 0; i < 4; i++) {
                objectManager.addCritter(new ObjectManager.Critter(gap, HEIGHT - 38, critterVelX, 0, ID.Critter, critterHP));
                gap += 40;
            }

            //Ensures that a new wave of enemies is only spawned at certain score  intervals
            CANSPAWN = false;
            lvl++;
            critterNextVelX += 5;
        }
    }

    /****
     MainGame Over/Restart Functions
     ****/

    //Function that sets the gameState to GameOver if either the playerCollision occurs or any critter hits the bottom of the screen
    public void checkGAMEOVER() {
        playerCollision();
        for (GameObject critter : objectManager.getCritters()) {
            if (critter.getY() - 15 <= 0) {
                gameState = STATE.GameOver;
            }
            if (gameState == STATE.GameOver) {
                gameoverReset();
                break;
            }
        }
    }

    //Function that displays both the Gameover screen and then reopens the Main Menu
    public void gameoverReset() {

        //Display GamOver Window, plays a Gameover Audio pauses it for 2.3 seconds
        StdDraw.setPenColor(Color.black);
        StdDraw.filledRectangle(0, 0, WIDTH, HEIGHT);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 35, "SCORE: " + score);
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 55, "RETURNING TO BASE");
        StdDraw.setPenColor(Color.RED);
        StdAudio.play("playerdead.wav");


        StdDraw.text(WIDTH / 2, HEIGHT / 2, "GAMEOVER :(");
        StdDraw.show();
        StdDraw.pause(2300);

        //Manages Scoring System for menuSTATS page
        if (score > highscore) {
            highscore = score;
        }
        sumofscores += score;
        attempts++;
        averagescore = sumofscores / attempts;

        //Restarting the game by reopening the Main Menu
        resetGameContents();
        menuCONTROL();

    }

    //Function that resets everything that ensure the game always restarts with the same values
    private void resetGameContents() {

        //Removes all GameObjects
        for (GameObject Critter : objectManager.getCritters()) {
            Critter.isDead = true;
        }
        for (GameObject bullet : objectManager.getBullets()) {
            bullet.isDead = true;
        }
        objectManager.bullets.removeIf(GameObject::isDead);
        objectManager.critters.removeIf(GameObject::isDead);
        objectManager.players.remove(0);

        //Resetting all the starting game variables to their starting form
        critterHP = critterHPStart;
        CANSPAWN = true;
        lvl = 0;
        score = 0;
        critterVelX = critterVelXStart;
        critterNextVelX = critterVelXStart;
        critterHP = critterHPStart;
        numberofupgrades = 1;
        gameState = STATE.Menu;
        CANUPGRADECRITTER = false;
    }


    public static void main(String[] args) {
        new MainGame();
    }
}

