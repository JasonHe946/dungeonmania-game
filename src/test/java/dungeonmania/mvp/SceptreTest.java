package dungeonmania.mvp;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import dungeonmania.exceptions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SceptreTest {
    @Test
    @Tag("1")
    @DisplayName("Test building a sceptre with wood and key")
    public void buildSceptre() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sceptreTest_buildSceptreWithWoodAndKey", "c_sceptreTest");

        assertEquals(0, TestUtils.getInventory(res, "wood").size());
        assertEquals(0, TestUtils.getInventory(res, "key").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());

        // Pick up Wood
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "wood").size());
        assertEquals(new Position(5, 1), getMercPos(res));

        // Pick up Key
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "key").size());
        assertEquals(new Position(5, 0), getMercPos(res));

        // Pick up Sun stone
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(new Position(4, 0), getMercPos(res));

        // Build Sceptre
        assertEquals(0, TestUtils.getInventory(res, "sceptre").size());
        res = assertDoesNotThrow(() -> dmc.build("sceptre"));
        assertEquals(1, TestUtils.getInventory(res, "sceptre").size());
        // Materials used in construction disappear from inventory
        assertEquals(0, TestUtils.getInventory(res, "wood").size());
        assertEquals(0, TestUtils.getInventory(res, "sword").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());
    }

    @Test
    @Tag("2")
    @DisplayName("Test building a sceptre with arrow and treasure")
    public void buildSceptre2() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sceptreTest_buildSceptreWithArrowAndTreasure", "c_sceptreTest");

        assertEquals(0, TestUtils.getInventory(res, "arrow").size());
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());

        // Pick up Arrow
        res = dmc.tick(Direction.RIGHT);

        // Pick up Arrow
        res = dmc.tick(Direction.RIGHT);

        // Pick up Treasure
        res = dmc.tick(Direction.RIGHT);

        // Pick up Sun stone
        res = dmc.tick(Direction.RIGHT);
        assertEquals(2, TestUtils.getInventory(res, "arrow").size());
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // Build Sceptre
        assertEquals(0, TestUtils.getInventory(res, "sceptre").size());
        res = assertDoesNotThrow(() -> dmc.build("sceptre"));
        assertEquals(1, TestUtils.getInventory(res, "sceptre").size());

        // Materials used in construction disappear from inventory
        assertEquals(0, TestUtils.getInventory(res, "arrow").size());
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());
    }

    @Test
    @Tag("2")
    @DisplayName("Test interaction with a mercenary that has been mind controlled")
    public void mindControlInteration() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sceptreTest_buildSceptreWithArrowAndTreasure", "c_sceptreTest");
        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();

        // Pick up Arrow
        res = dmc.tick(Direction.RIGHT);

        // Pick up Arrow
        res = dmc.tick(Direction.RIGHT);

        // Pick up Treasure
        res = dmc.tick(Direction.RIGHT);

        // Pick up Sun stone
        res = dmc.tick(Direction.RIGHT);

        // Build Sceptre
        assertEquals(0, TestUtils.getInventory(res, "sceptre").size());
        res = assertDoesNotThrow(() -> dmc.build("sceptre"));
        assertEquals(1, TestUtils.getInventory(res, "sceptre").size());

        // interact and mind control mercenary
        // mercenary can be interacted even though it can't be bribed
        res = assertDoesNotThrow(() -> dmc.interact(mercId));
        // move and try interact but mercenary is allied
        res = dmc.tick(Direction.DOWN);
        assertThrows(InvalidActionException.class, () ->
        dmc.interact(mercId)
);
    }

    @Test
    @Tag("4")
    @DisplayName("Test sceptre mind control on a mercenary")
    public void mindControl2() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sceptreTest_mindControl", "c_sceptreTest");

        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();

        // Pick up Wood
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "wood").size());

        // Pick up Key
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "key").size());

        // Pick up Sun stone
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(new Position(3, 1), getPlayerPos(res));

        // attempt to interact with mercenary but fails
        assertThrows(InvalidActionException.class, () ->
                dmc.interact(mercId)
        );

        // Build Sceptre
        assertEquals(0, TestUtils.getInventory(res, "sceptre").size());
        res = assertDoesNotThrow(() -> dmc.build("sceptre"));
        assertEquals(1, TestUtils.getInventory(res, "sceptre").size());

        assertEquals(new Position(3, 1), getPlayerPos(res));
        assertEquals(new Position(5, 1), getMercPos(res));

        // achieves mindcontrol
        res = assertDoesNotThrow(() -> dmc.interact(mercId));

        assertEquals(new Position(3, 1), getPlayerPos(res));
        assertEquals(new Position(4, 1), getMercPos(res));
        // walk into mercenary, a battle does not occur
        res = dmc.tick(Direction.RIGHT);
        assertEquals(0, res.getBattles().size());
    }

    private Position getMercPos(DungeonResponse res) {
        return TestUtils.getEntities(res, "mercenary").get(0).getPosition();
    }

    private Position getPlayerPos(DungeonResponse res) {
        return TestUtils.getEntities(res, "player").get(0).getPosition();
    }
}
