package GameDev6;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.util.LinkedList;

public class ObjectManager {


    //Link Lists used to seperate the different Game Objects and to easily add and remove them by making use of inheritance - See External Sources Entry 6
    LinkedList<GameObject> players = new LinkedList<GameObject>(); //players
    LinkedList<GameObject> critters = new LinkedList<GameObject>(); //enemies
    LinkedList<GameObject> bullets = new LinkedList<GameObject>(); //bullets


    //get<InsertNames>() are used in for loops as where a temporary Game Object(which obtains the properties of a certain object in that list) to check for collision - See External Sources Entry 6
    public LinkedList<GameObject> getBullets() {
        return bullets;
    }

    public LinkedList<GameObject> getCritters() {
        return critters;
    }

    public LinkedList<GameObject> getPlayers() {
        return players;
    }


    public void addCritter(GameObject critter) {
        this.critters.add(critter);
    }

    public void RemoveCritter(GameObject critter) {
        this.critters.remove(critter);
    }

    public void addPlayer(GameObject player) {
        this.players.add(player);
    }

    public void RemovePlayer(GameObject player) {
        this.players.remove(player);
    }

    public void addBullet(GameObject bullet) {
        this.bullets.add(bullet);
    }

    public void RemoveBullet(GameObject bullet) {
        this.bullets.remove(bullet);
    }


    //The Player Class is a form of subclassing for class/implementation or inheritance as extends/makes use of the Game Object Class (which acts as a template)
    public static class Player extends GameObject {

        int size = 32; //Used when checking if the Player Object were to go off the screen

        //Constructor of Player Class which is inherited from the Game Object Class
        public Player(int x, int y, int VelX, int VelY, ID id, int hp) {
            super(x, y, VelX, VelY, GameDev6.ID.Player, hp);
        }

        private double RotationAngle; //Used to display the Player Object correctly relevant to the mouse cursor


        //Function to update the player movement that is called based on key inputs and ensures object doesn't leave screen
        public void update(double deltaTime) {
            x += velX * deltaTime; //Ensures that velX doesn't truncate/floor
            if (x > MainGame.WIDTH - size) {
                x = (MainGame.WIDTH - size);
                velX *= -1;
            } else if (x < size) {
                x = size;
                velX *= -1;
            }
        }


        //Function to display the graphic of the player
        public void display() {

            //Note the color of various rings is dependant on how far the player is from being fully reloaded, it acts as a visual indicator to show when the plaer has cooled down

            RotationAngle = getRotationAngle();
            StdDraw.setPenColor(Color.cyan);
            if (MainGame.timeSINCESHOT < MainGame.reloadtime) {
                StdDraw.setPenColor(Color.red);
            }
            StdDraw.filledCircle(x + 27 * Math.cos(RotationAngle + Math.PI / 6), y + 25 * Math.sin(RotationAngle + Math.PI / 6), 5);
            StdDraw.filledCircle(x + 27 * Math.cos(RotationAngle - Math.PI / 6), y + 25 * Math.sin(RotationAngle - Math.PI / 6), 5);
            StdDraw.setPenColor(Color.cyan);
            RotationAngle = Math.toDegrees(RotationAngle);
            if (MainGame.timeSINCESHOT < MainGame.reloadtime / 5) {
                StdDraw.setPenColor(Color.red);
            }
            StdDraw.arc(x, y, 34, RotationAngle + 210, RotationAngle + 240);
            StdDraw.arc(x, y, 34, RotationAngle + 120, RotationAngle + 150);
            StdDraw.setPenColor(Color.cyan);
            if (MainGame.timeSINCESHOT < 2 * MainGame.reloadtime / 5) {
                StdDraw.setPenColor(Color.red);
            }
            StdDraw.arc(x, y, 27, RotationAngle + 60, RotationAngle - 60);
            StdDraw.setPenColor(Color.cyan);
            if (MainGame.timeSINCESHOT < (3 * MainGame.reloadtime / 5)) {
                StdDraw.setPenColor(Color.red);
            }
            StdDraw.arc(x, y, 22, RotationAngle + 60, RotationAngle - 60);
            StdDraw.setPenColor(Color.cyan);
            if (MainGame.timeSINCESHOT < 4 * MainGame.reloadtime / 5) {
                StdDraw.setPenColor(Color.red);
            }
            StdDraw.arc(x, y, 17, RotationAngle + 60, RotationAngle - 60);
            StdDraw.setPenColor(Color.cyan);
            if (MainGame.timeSINCESHOT < MainGame.reloadtime) {
                StdDraw.setPenColor(Color.red);
            }
            StdDraw.arc(x, y, 12, RotationAngle + 60, RotationAngle - 60);
            x += velX;
        }


        //Functions that are used to change the Velocity of the Player if certain Key Inputs are true
        public void setVelX(int velX) {
            this.velX = velX;
        }

        public int getVelX() {
            return velX;
        }
    }

    //The Bullet Class is a form of subclassing for class/implementation or inheritance as extends/makes use of the Game Object Class (which acts as a template)
    public static class Bullet extends GameObject {

        int size = 6; //radius of the bullet

        //Constructor of Bullet Class which is inherited from the Game Object Class
        public Bullet(int x, int y, int VelX, int VelY, ID id, int hp) {
            super(x, y, VelX, VelY, ID.Bullet, hp);
        }


        //Function is called in MainGame and updates the bullet's velocity
        public void update(double deltaTime) {

            //If the bullet hits the top of the screen, isDead becomes true and then it will be removed in the Game Loop (helps clear up memory)
            if (y > MainGame.HEIGHT) {
                isDead = true;
            }

            x += velX * deltaTime; //Ensures that velX doesn't truncate/floor

            //Bouncing effect of bullet against walls
            if (x > MainGame.WIDTH - size) {
                x = (MainGame.WIDTH - size);
                velX *= -1;
            } else if (x < size) {
                x = size;
                velX *= -1;
            }
        }

        //Function that displays the bullet and then updates its position for the next time its called
        public void display() {
            StdDraw.setPenColor(Color.yellow);
            StdDraw.filledCircle(x, y, 6);
            x += velX;
            y += velY;
            StdDraw.setPenColor(Color.black);

        }


        public void setVelX(int velX) {
            this.velX = velX;
        }

        public int getVelX() {
            return velX;
        }

        //Used when bulletCollision is called
        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }


    //The Critter Class is a form of subclassing for class/implementation or inheritance as extends/makes use of the Game Object Class (which acts as a template)
    public static class Critter extends GameObject {

        int size = 15; //Half-Width and Half-Height of Player Object

        //Constructor of Critter Class which is inherited from the Game Object Class
        public Critter(int x, int y, int VelX, int VelY, ID id, int hp) {
            super(x, y, VelX, VelY, ID.Critter, hp);
        }


        //Note the critter's y-position is updated in the clamp method in GameMain

        //Function that displays the critter and then updates its x-position for the next time its called
        public void display() {

            //The Color of the critter is based on how much Health(hp) it still has
            if (hp == 1) {
                StdDraw.setPenColor(Color.red);
            }
            if (hp == 2) {
                StdDraw.setPenColor(Color.green);
            }
            if (hp == 3) {
                StdDraw.setPenColor(Color.blue);
            }
            if (hp == 4) {
                StdDraw.setPenColor(Color.yellow);
            }

            StdDraw.filledRectangle(x, y, size, size);
            StdDraw.setPenColor(Color.black);
            StdDraw.filledRectangle(x + 4, y + 4, 2, 2);
            StdDraw.filledRectangle(x - 4, y + 4, 2, 2);
            StdDraw.rectangle(x, y - 4, 10, 4);
            StdDraw.line(x - 3, y - 4, x - 3, y + 4);
            StdDraw.line(x + 3, y - 4, x + 3, y + 4);

            x += velX;
        }


        //Both Functions used in the clamp method in GameMain
        public void setVelX(int velX) {
            this.velX = velX;
        }

        public int getVelX() {
            return velX;
        }

    }


}
