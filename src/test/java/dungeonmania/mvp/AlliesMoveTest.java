package dungeonmania.mvp;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AlliesMoveTest {
    @Test
    @Tag("13-1")
    @DisplayName("Testing NEW mercenary movement after becoming allied")
    public void allyMovement() {

        //                          E       W       W       W       W       W
        //  P1      P2/T    P3      M5      M4      M3      M2      M1      W
        //                  P4      P5      W       W       W       W       W

        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_alliesMoveTest_allyChase", "c_alliesMoveTest_allyChase");

        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();
        assertEquals(new Position(8, 1), getMercPos(res));

        // pick up treasure
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());
        assertEquals(new Position(7, 1), getMercPos(res));

        // achieve bribe
        res = assertDoesNotThrow(() -> dmc.interact(mercId));
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());
        assertEquals(new Position(6, 1), getMercPos(res));

        // testing that mercenary moves towards player now instead of random movement
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(5, 1), getMercPos(res));
        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(4, 1), getMercPos(res));

    }

    @Test
    @Tag("13-1")
    @DisplayName("Testing NEW mercenary movement after adjcaent to player")
    public void allyMovementAdjacent() {

        //                  E       W       W       W       W       W
        //  P1      P2/T    P3/M5   M4      M3      M2      M1      W
        //                  P4/M6           W       W       W       W
        //  P7/M9    P6/M8   P5/M7
        //  P8/M10   P9/M11  P10

        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_alliesMoveTest_allyAdjacent", "c_alliesMoveTest_allyAdjacent");

        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();
        assertEquals(new Position(7, 1), getMercPos(res));

        // pick up treasure
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());
        assertEquals(new Position(6, 1), getMercPos(res));

        // achieve bribe
        res = assertDoesNotThrow(() -> dmc.interact(mercId));
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());
        assertEquals(new Position(5, 1), getMercPos(res));

        // testing that mercenary moves towards player now instead of random movement
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(4, 1), getMercPos(res));
        // since player and ally is now adjacent, ally should follow player
        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(3, 1), getMercPos(res));
        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(3, 2), getMercPos(res));
        res = dmc.tick(Direction.LEFT);
        assertEquals(new Position(3, 3), getMercPos(res));
        res = dmc.tick(Direction.LEFT);
        assertEquals(new Position(2, 3), getMercPos(res));
        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(1, 3), getMercPos(res));
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(1, 4), getMercPos(res));
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(2, 4), getMercPos(res));
    }

    @Test
    @Tag("13-1")
    @DisplayName("Testing NEW bribed mercenary movement before reaching player")
    public void allyDjikMovement() {

        //                  E
        //  P1      P2/T    P3/M5   M4      M3      M2      M1
        //                  P4/M6
        //  P7/M9    P6/M8   P5/M7
        //  P8/M10   P9/M11  P10

        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_alliesMoveTest_allyDjikMovement", "c_alliesMoveTest_allyDjikMovement");

        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();
        assertEquals(new Position(7, 1), getMercPos(res));

        // pick up treasure
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());
        assertEquals(new Position(6, 1), getMercPos(res));

        // achieve bribe
        res = assertDoesNotThrow(() -> dmc.interact(mercId));
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());
        assertEquals(new Position(5, 1), getMercPos(res));

        // testing that mercenary moves towards player now instead of random movement
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(4, 1), getMercPos(res));
        // since player and ally is now adjacent, ally should follow player
        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(3, 1), getMercPos(res));
        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(3, 2), getMercPos(res));
        res = dmc.tick(Direction.LEFT);
        assertEquals(new Position(3, 3), getMercPos(res));
        res = dmc.tick(Direction.LEFT);
        assertEquals(new Position(2, 3), getMercPos(res));
        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(1, 3), getMercPos(res));
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(1, 4), getMercPos(res));
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(2, 4), getMercPos(res));
    }

    @Test
    @Tag("13-1")
    @DisplayName("Testing complex bribed mercenary movement before reaching player")
    public void allyComplexMovement() {

        //         1       2           3       4       5       6       7       8       9

        // 0       P1      P2/T        P3      P4      P5       W      W       W       W
        // 1                                          M6        M5     M4      M3
        // 2                                                           W       M2
        // 3        E                                                  W       M1

        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_alliesMoveTest_allyComplex", "c_alliesMoveTest_allyComplex");

        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();
        assertEquals(new Position(8, 3), getMercPos(res));

        // pick up treasure
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());
        assertEquals(new Position(8, 2), getMercPos(res));

        // achieve bribe
        res = assertDoesNotThrow(() -> dmc.interact(mercId));
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());
        assertEquals(new Position(8, 1), getMercPos(res));

        // testing that mercenary moves towards player now instead of random movement
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(7, 1), getMercPos(res));
        // since player and ally is now adjacent, ally should follow player
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(6, 1), getMercPos(res));
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(5, 1), getMercPos(res));
        res = dmc.tick(Direction.LEFT);
        assertEquals(new Position(5, 0), getMercPos(res));
        res = dmc.tick(Direction.LEFT);
        assertEquals(new Position(4, 0), getMercPos(res));

    }

    private Position getMercPos(DungeonResponse res) {
        return TestUtils.getEntities(res, "mercenary").get(0).getPosition();
    }

}
