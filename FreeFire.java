import java.util.*;

// Enum for weapon types
enum WeaponType {
    ASSAULT_RIFLE("AK47", 35, 30),
    SNIPER_RIFLE("M24", 65, 8),
    SHOTGUN("SPAS12", 50, 6),
    SMG("MP40", 20, 25),
    PISTOL("G18", 15, 20);
    
    private String name;
    private int damage;
    private int ammo;
    
    WeaponType(String name, int damage, int ammo) {
        this.name = name;
        this.damage = damage;
        this.ammo = ammo;
    }
    
    public int getDamage() { return damage; }
    public int getAmmo() { return ammo; }
    public String getName() { return name; }
}

// Player class
class Player {
    private String name;
    private int health;
    private int maxHealth;
    private int kills;
    private int level;
    private Weapon currentWeapon;
    private int coins;
    private boolean alive;
    
    public Player(String name) {
        this.name = name;
        this.health = 100;
        this.maxHealth = 100;
        this.kills = 0;
        this.level = 1;
        this.coins = 0;
        this.alive = true;
        this.currentWeapon = new Weapon(WeaponType.PISTOL);
    }
    
    public void takeDamage(int damage) {
        this.health -= damage;
        if (this.health <= 0) {
            this.health = 0;
            this.alive = false;
        }
    }
    
    public void heal(int amount) {
        this.health = Math.min(this.health + amount, this.maxHealth);
    }
    
    public void addKill() {
        this.kills++;
        this.coins += 50;
        if (this.kills % 5 == 0) {
            levelUp();
        }
    }
    
    public void levelUp() {
        this.level++;
        this.maxHealth += 10;
        this.health = this.maxHealth;
    }
    
    public void switchWeapon(WeaponType type) {
        this.currentWeapon = new Weapon(type);
    }
    
    public void shoot() {
        if (currentWeapon.getAmmo() > 0) {
            currentWeapon.fire();
        } else {
            System.out.println(name + " - Out of ammo!");
        }
    }
    
    // Getters and Setters
    public String getName() { return name; }
    public int getHealth() { return health; }
    public int getKills() { return kills; }
    public int getLevel() { return level; }
    public int getCoins() { return coins; }
    public boolean isAlive() { return alive; }
    public Weapon getCurrentWeapon() { return currentWeapon; }
    public void addCoins(int amount) { this.coins += amount; }
}

// Weapon class
class Weapon {
    private WeaponType type;
    private int currentAmmo;
    private int maxAmmo;
    
    public Weapon(WeaponType type) {
        this.type = type;
        this.maxAmmo = type.getAmmo();
        this.currentAmmo = maxAmmo;
    }
    
    public void fire() {
        if (currentAmmo > 0) {
            currentAmmo--;
        }
    }
    
    public void reload() {
        this.currentAmmo = maxAmmo;
    }
    
    public int getDamage() { return type.getDamage(); }
    public int getAmmo() { return currentAmmo; }
    public WeaponType getType() { return type; }
}

// Game class
class FreeFire {
    private List<Player> players;
    private int mapSize;
    private int maxPlayers;
    private GameStatus gameStatus;
    
    enum GameStatus {
        LOBBY, STARTING, IN_GAME, ENDED
    }
    
    public FreeFire(int maxPlayers) {
        this.players = new ArrayList<>();
        this.maxPlayers = maxPlayers;
        this.mapSize = 500; // Game map area
        this.gameStatus = GameStatus.LOBBY;
    }
    
    public void addPlayer(String name) {
        if (players.size() < maxPlayers) {
            players.add(new Player(name));
            System.out.println(name + " joined the game! Total players: " + players.size());
        } else {
            System.out.println("Game is full!");
        }
    }
    
    public void startGame() {
        if (players.size() >= 2) {
            gameStatus = GameStatus.IN_GAME;
            System.out.println("\n========== FREE FIRE - GAME STARTED ==========");
            System.out.println("Total Players: " + players.size());
            System.out.println("Map Size: " + mapSize + "x" + mapSize);
            System.out.println("==========================================\n");
        } else {
            System.out.println("Need at least 2 players to start!");
        }
    }
    
    public void combat(String attacker, String defender) {
        Player p1 = getPlayer(attacker);
        Player p2 = getPlayer(defender);
        
        if (p1 == null || p2 == null || !p1.isAlive() || !p2.isAlive()) {
            System.out.println("Invalid combat!");
            return;
        }
        
        int damage = p1.getCurrentWeapon().getDamage();
        System.out.println("\n[COMBAT] " + attacker + " attacks " + defender);
        System.out.println("Weapon: " + p1.getCurrentWeapon().getType().getName());
        System.out.println("Damage: " + damage);
        
        p2.takeDamage(damage);
        
        System.out.println(defender + " health: " + p2.getHealth());
        
        if (!p2.isAlive()) {
            p1.addKill();
            System.out.println(attacker + " eliminated " + defender + "!");
            System.out.println(attacker + " - Kills: " + p1.getKills() + ", Level: " + p1.getLevel());
        }
    }
    
    public void grantLoot(String playerName, int coins, int health) {
        Player player = getPlayer(playerName);
        if (player != null) {
            player.addCoins(coins);
            player.heal(health);
            System.out.println(playerName + " found loot! +$" + coins + " +❤️ " + health);
        }
    }
    
    public void displayLeaderboard() {
        System.out.println("\n========== LEADERBOARD ==========");
        players.sort((p1, p2) -> p2.getKills() - p1.getKills());
        
        int rank = 1;
        for (Player p : players) {
            String status = p.isAlive() ? "ALIVE" : "ELIMINATED";
            System.out.printf("%d. %-15s | Kills: %d | Level: %d | Health: %d | Coins: $%d | Status: %s%n",
                rank++, p.getName(), p.getKills(), p.getLevel(), p.getHealth(), p.getCoins(), status);
        }
        System.out.println("================================\n");
    }
    
    public Player getWinner() {
        for (Player p : players) {
            if (p.isAlive()) {
                return p;
            }
        }
        return null;
    }
    
    public void endGame() {
        gameStatus = GameStatus.ENDED;
        Player winner = getWinner();
        System.out.println("\n========== GAME ENDED ==========");
        if (winner != null) {
            System.out.println("🏆 WINNER: " + winner.getName());
            System.out.println("Kills: " + winner.getKills());
            System.out.println("Prize: $" + (winner.getKills() * 100));
        }
        System.out.println("================================\n");
    }
    
    private Player getPlayer(String name) {
        for (Player p : players) {
            if (p.getName().equalsIgnoreCase(name)) {
                return p;
            }
        }
        return null;
    }
    
    public List<Player> getPlayers() { return players; }
    public GameStatus getGameStatus() { return gameStatus; }
}

// Main Game Simulation
public class FreeFire_Main {
    public static void main(String[] args) {
        System.out.println("🔥 Welcome to Free Fire Game 🔥\n");
        
        // Create game with max 50 players
        FreeFire game = new FreeFire(50);
        
        // Add players
        game.addPlayer("Commander");
        game.addPlayer("Warrior");
        game.addPlayer("Sniper");
        game.addPlayer("Ghost");
        game.addPlayer("Phantom");
        
        // Start game
        game.startGame();
        
        // Simulate gameplay
        game.getPlayer("Commander").switchWeapon(WeaponType.ASSAULT_RIFLE);
        game.getPlayer("Warrior").switchWeapon(WeaponType.SHOTGUN);
        game.getPlayer("Sniper").switchWeapon(WeaponType.SNIPER_RIFLE);
        
        // Combat scenarios
        game.combat("Commander", "Warrior");
        game.combat("Sniper", "Ghost");
        game.combat("Commander", "Sniper");
        
        // Loot collection
        game.grantLoot("Commander", 500, 20);
        game.grantLoot("Warrior", 300, 15);
        
        // Display leaderboard
        game.displayLeaderboard();
        
        // More combat
        game.combat("Commander", "Phantom");
        game.combat("Warrior", "Phantom");
        
        // Final leaderboard and winner
        game.displayLeaderboard();
        game.endGame();
    }
}
