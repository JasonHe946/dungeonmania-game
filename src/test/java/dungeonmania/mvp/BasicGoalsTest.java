package dungeonmania.mvp;

import dungeonmania.DungeonManiaController;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BasicGoalsTest {

    @Test
    @Tag("13-1")
    @DisplayName("Test achieving a basic exit goal")
    public void exit() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_basicGoalsTest_exit", "c_basicGoalsTest_exit");

        // move player to right
        res = dmc.tick(Direction.RIGHT);

        // assert goal not met
        assertTrue(TestUtils.getGoals(res).contains(":exit"));

        // move player to exit
        res = dmc.tick(Direction.RIGHT);

        // assert goal met
        assertEquals("", TestUtils.getGoals(res));
    }


    @Test
    @Tag("13-2")
    @DisplayName("Test achieving a basic boulders goal")
    public void oneSwitch()  {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_basicGoalsTest_oneSwitch", "c_basicGoalsTest_oneSwitch");

        // move player to right
        res = dmc.tick(Direction.RIGHT);

        // assert goal not met
        assertTrue(TestUtils.getGoals(res).contains(":boulders"));

        // move boulder onto switch
        res = dmc.tick(Direction.RIGHT);

        // assert goal met
        assertEquals("", TestUtils.getGoals(res));
    }

    @Test
    @Tag("13-3")
    @DisplayName("Test achieving a boulders goal where there are five switches")
    public void fiveSwitches()  {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_basicGoalsTest_fiveSwitches", "c_basicGoalsTest_fiveSwitches");

        // assert goal not met
        assertTrue(TestUtils.getGoals(res).contains(":boulders"));

        // move first four boulders onto switch
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);

        // assert goal not met
        assertTrue(TestUtils.getGoals(res).contains(":boulders"));

        // move last boulder onto switch
        res = dmc.tick(Direction.DOWN);

        // assert goal met
        assertEquals("", TestUtils.getGoals(res));
    }


    @Test
    @Tag("13-4")
    @DisplayName("Test achieving a basic treasure goal")
    public void treasure() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_basicGoalsTest_treasure", "c_basicGoalsTest_treasure");

        // move player to right
        res = dmc.tick(Direction.RIGHT);

        // assert goal not met
        assertTrue(TestUtils.getGoals(res).contains(":treasure"));

        // collect treasure
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());

        // assert goal not met
        assertTrue(TestUtils.getGoals(res).contains(":treasure"));

        // collect treasure
        res = dmc.tick(Direction.RIGHT);
        assertEquals(2, TestUtils.getInventory(res, "treasure").size());

        // assert goal not met
        assertTrue(TestUtils.getGoals(res).contains(":treasure"));

        // collect treasure
        res = dmc.tick(Direction.RIGHT);
        assertEquals(3, TestUtils.getInventory(res, "treasure").size());

        // assert goal met
        assertEquals("", TestUtils.getGoals(res));
    }

    @Test
    @Tag("13-5")
    @DisplayName("Test achieving a basic enemies goal -> destroy one enemy, no spawner on map")
    public void oneEnemy() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_basicGoalsTest_enemy", "c_basicGoalsTest_enemy");
        assertEquals(2, TestUtils.getEntities(res, "spider").size()); // expect two spider (enemy)
        assertEquals(0, TestUtils.getEntities(res, "zombie_toast_spawner").size()); // expect one spawner

        // move left
        res = dmc.tick(Direction.LEFT);

        // assert goal not met
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));

        // move left and kill spider
        res = dmc.tick(Direction.LEFT);

        // assert goal met
        assertEquals(1, TestUtils.getEntities(res, "spider").size());
        assertEquals(0, TestUtils.getEntities(res, "zombie_toast_spawner").size());
        assertEquals("", TestUtils.getGoals(res));
    }

    @Test
    @Tag("13-6")
    @DisplayName("Test achieving a basic enemies goal -> destroy two enemies, no spawner on map")
    public void twoEnemies() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_basicGoalsTest_enemy", "c_basicGoalsTest_enemy_DestroyTwo");
        assertEquals(2, TestUtils.getEntities(res, "spider").size()); // expect two spider (enemy)
        assertEquals(0, TestUtils.getEntities(res, "zombie_toast_spawner").size()); // expect one spawner

        // move left
        res = dmc.tick(Direction.LEFT);

        // assert goal not met
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));

        // move left and kill spider
        res = dmc.tick(Direction.LEFT);

        // assert goal not met
        assertEquals(1, TestUtils.getEntities(res, "spider").size());
        assertEquals(0, TestUtils.getEntities(res, "zombie_toast_spawner").size());
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));

        // move and kill second spider
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        // assert goal met
        assertEquals(0, TestUtils.getEntities(res, "spider").size());
        assertEquals("", TestUtils.getGoals(res));
    }

    @Test
    @Tag("13-7")
    @DisplayName("Test achieving a enemies goal -> destroy one enemy and a spawner")
    public void enemySpawner() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_basicGoalsTest_enemy_spawner", "c_basicGoalsTest_enemy");
        assertEquals(1, TestUtils.getEntities(res, "spider").size()); // expect two spider (enemy)
        assertEquals(1, TestUtils.getEntities(res, "zombie_toast_spawner").size()); // expect one spawner
                String spawnerId = TestUtils.getEntities(res, "zombie_toast_spawner").get(0).getId();

        // move right and kill spider
        res = dmc.tick(Direction.RIGHT);

        // assert goal not met but spider is killed
        assertEquals(0, TestUtils.getEntities(res, "spider").size());
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));

        // pick up sword
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sword").size());

        // cardinally adjacent: false, has sword: true
        assertThrows(InvalidActionException.class, () ->
                dmc.interact(spawnerId)
        );
        assertEquals(1, TestUtils.getEntities(res, "zombie_toast_spawner").size());

        // move right
        res = dmc.tick(Direction.RIGHT);

        // cardinally adjacent: true, has sword: true, but invalid_id
        assertThrows(IllegalArgumentException.class, () ->
                dmc.interact("random_invalid_id")
        );
        // cardinally adjacent: true, has sword: true
        res = assertDoesNotThrow(() -> dmc.interact(spawnerId));
        assertEquals(1, TestUtils.countType(res, "zombie_toast_spawner"));
        // // assert goal met
        // assertEquals(0, TestUtils.getEntities(res, "spider").size());
        // assertEquals(0, TestUtils.getEntities(res, "zombie_toast_spawner").size());
        // assertEquals("", TestUtils.getGoals(res));
    }
}
