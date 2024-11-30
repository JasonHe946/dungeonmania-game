package dungeonmania.mvp;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SunStoneTest {

    // @Test
    // @Tag("4-1")
    // @DisplayName("Test player cannot walk through a closed door")
    // public void cannotWalkClosedDoor() {
    //     DungeonManiaController dmc;
    //     dmc = new DungeonManiaController();
    //     DungeonResponse res = dmc.newGame(
    //         "d_DoorsKeysTest_cannotWalkClosedDoor", "c_DoorsKeysTest_cannotWalkClosedDoor");
    //     Position pos = TestUtils.getEntities(res, "player").get(0).getPosition();

    //     // try to walk through door and fail
    //     res = dmc.tick(Direction.RIGHT);
    //     assertEquals(pos, TestUtils.getEntities(res, "player").get(0).getPosition());
    // }

    @Test
    @Tag("4-2")
    @DisplayName("Test player can pick up a sun stone and add to inventory")
    public void pickUpKey() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sunStoneTest_pickUp", "c_sunStoneTest_pickUp");

        assertEquals(1, TestUtils.getEntities(res, "sun_stone").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());

        // pick up key
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(0, TestUtils.getEntities(res, "sun_stone").size());

    }

    @Test
    @Tag("4-3")
    @DisplayName("Test player can use a sun stone to open and walk through a door")
    public void useStoneForDoor() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(
            "d_sunStoneTest_useStoneForDoor", "c_sunStoneTest_useStoneForDoor");

        // pick up sun stone
        res = dmc.tick(Direction.RIGHT);
        Position pos = TestUtils.getEntities(res, "player").get(0).getPosition();
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // walk through door using stone, stone should still be in inventory afterwards
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
        assertNotEquals(pos, TestUtils.getEntities(res, "player").get(0).getPosition());
    }

    @Test
    @Tag("4-5")
    @DisplayName("Test player can unlock two doors using the same sun stone")
    public void unlockMultipleDoors() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(
            "d_sunStoneTest_unlockMultipleDoors", "c_sunStoneTest_unlockMultipleDoors");

        // pick up sun stone
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
        Position pos1 = TestUtils.getEntities(res, "player").get(0).getPosition();

        // walk through door_1
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
        Position pos2 = TestUtils.getEntities(res, "player").get(0).getPosition();

        assertNotEquals(pos1, pos2);
        // walk through door_2
        res = dmc.tick(Direction.RIGHT);
        Position pos3 = TestUtils.getEntities(res, "player").get(0).getPosition();
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
        Position pos4 = TestUtils.getEntities(res, "player").get(0).getPosition();
        assertNotEquals(pos3, pos4);
    }

    @Test
    @Tag("5-4")
    @DisplayName("Test building a shield with a sun stone")
    public void buildShieldWithStone() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sunStoneTest_buildShieldWithStone", "c_sunStoneTest_buildShieldWithStone");

        assertEquals(0, TestUtils.getInventory(res, "wood").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());

        // Pick up Wood x2
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(2, TestUtils.getInventory(res, "wood").size());

        // Pick up sun stone
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // Build Shield
        assertEquals(0, TestUtils.getInventory(res, "shield").size());
        res = assertDoesNotThrow(() -> dmc.build("shield"));
        assertEquals(1, TestUtils.getInventory(res, "shield").size());

        // Materials used in construction disappear from inventory except for sun stone
        assertEquals(0, TestUtils.getInventory(res, "wood").size());
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
    }

    @Test
    @Tag("13-4")
    @DisplayName("Test achieving a basic treasure goal with one sun stone and 2 treasures")
    public void treasure() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sunStoneTest_treasureGoal", "c_sunStoneTest_treasureGoal");

        // move player to right
        res = dmc.tick(Direction.RIGHT);

        // assert goal not met
        assertTrue(TestUtils.getGoals(res).contains(":treasure"));

        // collect sun_stone
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // assert goal not met
        assertTrue(TestUtils.getGoals(res).contains(":treasure"));

        // collect treasure
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());

        // assert goal not met
        assertTrue(TestUtils.getGoals(res).contains(":treasure"));

        // collect treasure
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(2, TestUtils.getInventory(res, "treasure").size());

        // assert goal met
        assertEquals("", TestUtils.getGoals(res));
    }
}

