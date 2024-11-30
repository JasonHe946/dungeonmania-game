package dungeonmania.mvp;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SwampTileTest {

    // spiders, hostile mercenaries, zombieToast
    @Test
    @Tag("6-4")
    @DisplayName("Test the effects of swamp tile on spiders")
    public void swampSpider() {
        //      0        1       2       3       4       5      6       7       8       9
        //  0                                                       S2,3,4/ST   S5
        //  1   P1                                                      S1      S6
        //  2   P2
        //  3   P3
        //  4   P4

        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_swampTileTest_swampSpider", "c_swampTileTest_swampSpider");

        assertEquals(1, TestUtils.getEntities(res, "spider").size());
        assertEquals(1, TestUtils.getEntities(res, "swamp_tile").size());

        assertEquals(new Position(7, 1), getSpiderPos(res));

        // spider walks onto swamp tile
        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(7, 0), getSpiderPos(res));

        // spider stays on swamp tile
        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(7, 0), getSpiderPos(res));

        // spider stays on swamp tile
        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(7, 0), getSpiderPos(res));

        // spider moves off swamp tile
        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(8, 0), getSpiderPos(res));

        // spider moves normally
        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(8, 1), getSpiderPos(res));

    }

    @Test
    @Tag("6-4")
    @DisplayName("Test the effects of swamp tile on zombie toast")
    public void swampToast() {

        //      1        2       3       4       5       6       7       8      9
        // 0                                             W      W       W       W
        // 1    P1                                       W      T1     T2-4/ST  T5
        // 2    P2                                       W      W       W       W
        // 3    P3
        // 4    P4

        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_swampTileTest_swampToast", "c_swampTileTest_swampToast");

        assertEquals(1, TestUtils.getEntities(res, "zombie_toast").size());
        assertEquals(1, TestUtils.getEntities(res, "swamp_tile").size());

        Position prevPosition = getZombiePos(res);
        assertEquals(new Position(7, 1), getZombiePos(res));

        // zombie walks onto swamp tile
        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(8, 1), getZombiePos(res));
        assertNotEquals(prevPosition, getZombiePos(res));
        prevPosition = getZombiePos(res);

        // zombie stays on swamp tile
        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(8, 1), getZombiePos(res));
        assertEquals(prevPosition, getZombiePos(res));
        prevPosition = getZombiePos(res);

        // zombie stays on swamp tile
        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(8, 1), getZombiePos(res));
        assertEquals(prevPosition, getZombiePos(res));
        prevPosition = getZombiePos(res);

        // zombie stays on swamp tile
        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(8, 1), getZombiePos(res));
        assertEquals(prevPosition, getZombiePos(res));
        prevPosition = getZombiePos(res);

        // zombie moves off swamp tile
        res = dmc.tick(Direction.DOWN);
        assertNotEquals(prevPosition, getZombiePos(res));
        prevPosition = getZombiePos(res);

        // zombie moves normally
        res = dmc.tick(Direction.DOWN);
        assertNotEquals(prevPosition, getZombiePos(res));
        prevPosition = getZombiePos(res);

    }

    @Test
    @Tag("12-5")
    @DisplayName("Testing the effects of swamp tile on a hostile mercenary")
    public void swampMerc() {
        //                                                          Wall     Wall     Wall          Wall    Wall
        // P1       P2/Treasure      P3/Treasure    P4/Treasure      M6       M5      M2/M3/M4      M1      Wall
        //                                              P5          Wall     Wall     Wall          Wall    Wall
        //                                              P6
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_swampTileTest_swampMerc", "c_swampTileTest_swampMerc");

        // walks onto swamp tile (move 2)
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(7, 1), getMercPos(res));

        // stays on swamp tile (move 3)
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(7, 1), getMercPos(res));

        // stays on swamp tile (move 4)
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(7, 1), getMercPos(res));

        // walks off swamp tile (move 5)
        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(6, 1), getMercPos(res));

        // continues normal movement (move 6)
        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(5, 1), getMercPos(res));


        assertEquals(3, TestUtils.getInventory(res, "treasure").size());

    }

    @Test
    @Tag("12-5")
    @DisplayName("Testing the effects of swamp tile on a hostile assassin")
    public void swampAss() {
        //                                                          Wall     Wall     Wall          Wall    Wall
        // P1       P2/Treasure      P3/Treasure    P4/Treasure      A6       A5      A2/A3/A4      A1      Wall
        //                                              P5          Wall     Wall     Wall          Wall    Wall
        //                                              P6
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_swampTileTest_swampAss", "c_swampTileTest_swampAss");

        // walks onto swamp tile (move 2)
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(7, 1), getAssPos(res));

        // stays on swamp tile (move 3)
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(7, 1), getAssPos(res));

        // stays on swamp tile (move 4)
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(7, 1), getAssPos(res));

        // walks off swamp tile (move 5)
        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(6, 1), getAssPos(res));

        // continues normal movement (move 6)
        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(5, 1), getAssPos(res));


        assertEquals(3, TestUtils.getInventory(res, "treasure").size());

    }

    @Test
    @Tag("1-4")
    @DisplayName("Test that swamp tiles has no effect on players")
    public void swampPlayer() {
        //
        // P1       P2/ST     P3     P4      P5
        //
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_swampTileTest_swampPlayer", "c_swampTileTest_swampPlayer");

        // walks onto swamp tile (move 2)
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(2, 1), getPlayerPos(res));

        // stays on swamp tile (move 3)
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(3, 1), getPlayerPos(res));

        // stays on swamp tile (move 4)
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(4, 1), getPlayerPos(res));

        // walks off swamp tile (move 5)
        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(4, 2), getPlayerPos(res));

        // continues normal movement (move 6)
        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(4, 3), getPlayerPos(res));
    }

    @Test
    @Tag("12-7")
    @DisplayName("Testing swamp tile has no effect on an allied mercenary and player")
    public void swampAlly() {
        //                                      Wall    Wall    Wall
        // P1       P2/Treasure/M4      M3      M2      M1      Wall
        //             P3/M5/ST                 Wall    Wall    Wall
        //              P4/M6
        //              P5/M7
        //              P6
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_swampTileTest_swampAlly", "c_swampTileTest_swampAlly");

        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();

        // pick up treasure
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());

        // achieve bribe
        res = assertDoesNotThrow(() -> dmc.interact(mercId));
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());
        assertEquals(new Position(3, 1), getMercPos(res));
        assertEquals(new Position(2, 1), getPlayerPos(res));

        // walk through swamp tile, shouldn't slow down either player or ally (move 3)
        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(2, 1), getMercPos(res));
        assertEquals(new Position(2, 2), getPlayerPos(res));

        // walk through swamp tile, shouldn't slow down either player or ally (move 4)
        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(2, 2), getMercPos(res));
        assertEquals(new Position(2, 3), getPlayerPos(res));

        // walk through swamp tile, shouldn't slow down either player or ally (move 5)
        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(2, 3), getMercPos(res));
        assertEquals(new Position(2, 4), getPlayerPos(res));

        // walk through swamp tile, shouldn't slow down either player or ally (move 6)
        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(2, 4), getMercPos(res));
        assertEquals(new Position(2, 5), getPlayerPos(res));
    }

    @Test
    @Tag("12-5")
    @DisplayName("Testing swamp tile still has effect on a far away allied mercenary")
    public void swampFarAlly() {
        //                                                          Wall     Wall        Wall          Wall    Wall
        // P1       P2/Treasure      P3                 P4           M6   ST/M3/M4/M5    M2            M1      Wall
        //                                              P5          Wall     Wall        Wall          Wall    Wall
        //                                              P6
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_swampTileTest_swampFarAlly", "c_swampTileTest_swampFarAlly");

        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();

        // collect treasure
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());
        assertEquals(new Position(7, 1), getMercPos(res));

        // bribe mercenary (move 3)
        res = assertDoesNotThrow(() -> dmc.interact(mercId));
        assertEquals(new Position(6, 1), getMercPos(res));

        // stays on swamp tile (move 4)
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(6, 1), getMercPos(res));

        // stays on swamp tile (move 5)
        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(6, 1), getMercPos(res));

        // continues normal movement (move 6 onwards)
        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(5, 1), getMercPos(res));
        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(4, 1), getMercPos(res));


    }






    private Position getSpiderPos(DungeonResponse res) {
        return TestUtils.getEntities(res, "spider").get(0).getPosition();
    }

    private Position getZombiePos(DungeonResponse res) {
        return TestUtils.getEntities(res, "zombie_toast").get(0).getPosition();
    }

    private Position getMercPos(DungeonResponse res) {
        return TestUtils.getEntities(res, "mercenary").get(0).getPosition();
    }

    private Position getAssPos(DungeonResponse res) {
        return TestUtils.getEntities(res, "assassin").get(0).getPosition();
    }

    private Position getPlayerPos(DungeonResponse res) {
        return TestUtils.getEntities(res, "player").get(0).getPosition();
    }
}
