package dungeonmania.mvp;

import dungeonmania.DungeonManiaController;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.RoundResponse;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MidnightArmourTest {
    @Test
    @Tag("5-7")
    @DisplayName("Test building midnight armour")
    public void buildMidnightArmour() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_midnightArmourTest_build", "c_midnightArmourTest");

        assertEquals(0, TestUtils.getInventory(res, "sword").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());

        // Pick up Sword
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sword").size());

        // Pick up Sun Stone
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // Build Midnight Armour
        assertEquals(0, TestUtils.getInventory(res, "midnight_armour").size());
        res = assertDoesNotThrow(() -> dmc.build("midnight_armour"));
        assertEquals(1, TestUtils.getInventory(res, "midnight_armour").size());

        // Materials used in construction disappear from inventory
        assertEquals(0, TestUtils.getInventory(res, "sword").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());
    }

    @Test
    @Tag("5-7")
    @DisplayName("Test building midnight armour fail because there is a zombie toast inside dungeon")
    public void buildMidnightArmourFail() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_midnightArmourTest_buildFail", "c_midnightArmourTest");

        assertEquals(0, TestUtils.getInventory(res, "sword").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(1, TestUtils.countType(res, "zombie_toast"));

        // assert that there are no zombies
        // Pick up Sword
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sword").size());

        // Pick up Sun Stone
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // Fails to build Midnight Armour
        assertEquals(0, TestUtils.getInventory(res, "midnight_armour").size());
        assertThrows(InvalidActionException.class, () ->
                dmc.build("midnight_armour")
        );

        assertEquals(0, TestUtils.getInventory(res, "midnight_armour").size());
    }

    public void assertBattleCalculations(
        BattleResponse battle, boolean enemyDies, String configFilePath, String enemyType) {
    List<RoundResponse> rounds = battle.getRounds();
    double playerHealth = battle.getInitialPlayerHealth(); // Should come from config
    double enemyHealth = battle.getInitialEnemyHealth(); // Should come from config
    double playerAttack = Double.parseDouble(TestUtils.getValueFromConfigFile("player_attack", configFilePath));
    double enemyAttack = Double
            .parseDouble(TestUtils.getValueFromConfigFile(enemyType + "_attack", configFilePath));

    for (RoundResponse round : rounds) {
        assertEquals(-enemyAttack / 10, round.getDeltaCharacterHealth(), 0.001);
        assertEquals(-playerAttack / 5, round.getDeltaEnemyHealth(), 0.001);
        // Delta health is negative
        enemyHealth += round.getDeltaEnemyHealth();
        playerHealth += round.getDeltaCharacterHealth();
    }

    if (enemyDies) {
        assertTrue(enemyHealth <= 0);
    } else {
        assertTrue(playerHealth <= 0);
    }
}

    @Test
    @Tag("11-23")
    @DisplayName("Test midnight armour increases attack damage")
    public void testMidnightArmourIncreasesAttackDamage() {
        DungeonManiaController dmc = new DungeonManiaController();
        String config = "c_midnightArmourTest";
        DungeonResponse res = dmc.newGame("d_midnightArmourTest_build", config);

        // Pick up Sword
        res = dmc.tick(Direction.RIGHT);

        // Pick up Sun Stone
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sword").size());
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
        // Build Midnight Armour
        assertEquals(0, TestUtils.getInventory(res, "midnight_armour").size());
        res = assertDoesNotThrow(() -> dmc.build("midnight_armour"));
        assertEquals(1, TestUtils.getInventory(res, "midnight_armour").size());

        // Battle the spider
        res = dmc.tick(Direction.RIGHT);

        BattleResponse battle = res.getBattles().get(0);
        RoundResponse firstRound = battle.getRounds().get(0);

        // This is the attack without the sword
        double playerBaseAttack = Double.parseDouble(TestUtils.getValueFromConfigFile("player_attack", config));
        double mArmourAttack = Double.parseDouble(TestUtils.getValueFromConfigFile("midnight_armour_attack", config));
        assertEquals((playerBaseAttack + mArmourAttack) / 5, -firstRound.getDeltaEnemyHealth(), 0.001);
    }

    @Test
    @Tag("11-24")
    @DisplayName("Test midnight armour reduces enemy attack")
    public void testMidnightArmourReducesEnemyAttack() throws InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        String config = "c_midnightArmourTest";
        DungeonResponse res = dmc.newGame("d_midnightArmourTest_build", config);

        // Pick up Sword
        res = dmc.tick(Direction.RIGHT);

        // Pick up Sun Stone
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sword").size());
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // Build Midnight Armour
        assertEquals(0, TestUtils.getInventory(res, "midnight_armour").size());
        res = assertDoesNotThrow(() -> dmc.build("midnight_armour"));
        assertEquals(1, TestUtils.getInventory(res, "midnight_armour").size());

        // Battle the spider
        res = dmc.tick(Direction.RIGHT);

        BattleResponse battle = res.getBattles().get(0);
        RoundResponse firstRound = battle.getRounds().get(0);

        // Assumption: Shield effect calculation to reduce damage makes enemyAttack =
        // enemyAttack - shield effect
        int enemyAttack = Integer.parseInt(TestUtils.getValueFromConfigFile("spider_attack", config));
        int mArmourEffect = Integer.parseInt(TestUtils.getValueFromConfigFile("midnight_armour_defence", config));
        int expectedDamage = (enemyAttack - mArmourEffect) / 10;
        // Delta health is negative so take negative here
        assertEquals(expectedDamage, -firstRound.getDeltaCharacterHealth(), 0.001);
    }
}
