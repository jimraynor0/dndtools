package org.toj.dnd.irctoolkit.game;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

public class GameTest {

    private Game outOfBattle;
    private Game inBattle;

    @Before
    public void setUp() throws Exception {
        outOfBattle = new Game("outOfBattle");
        outOfBattle.addPc("Jim");
        outOfBattle.addPc("Hanbak");
        outOfBattle.addPc("웹");
        outOfBattle.addPc("Ogre");
        outOfBattle.damage("웹", 7);
        outOfBattle.applyState("han", "slow|sv");

        inBattle = new Game("inBattle");
        inBattle.startBattle();
        inBattle.addPc("Jim");
        inBattle.addPc("Hanbak");
        inBattle.addPc("웹");
        inBattle.addPc("Ogre");
        inBattle.getBattle().addCharByInit("Jim", 10);
        inBattle.getBattle().addCharByInit("Hanbak", 20);
        inBattle.getBattle().addCharByInit("웹", 0);
        inBattle.getBattle().addCharByInit("Ogre", -10);
        inBattle.getBattle().startRound(1);
        inBattle.damage("웹", 7);
        inBattle.applyState("han", "slow|sv");
    }

    // @Test
    // public void testToJsonObject() {
    // assertEquals("{\"aliases\":{},\"battle\":null,\"name\":\"outOfBattle\",\"statString\":\"Jim{init=0}{surge-0, ap-0}{slow|sv|0|0.0}, Ogre{init=0}{surge-0, ap-0}, Hanbak{init=0}{surge-0, ap-0}, 웹{init=0}{surge-0, ap-0}\"}",
    // outOfBattle.toJsonObject().toString());
    // assertEquals("{\"aliases\":{},\"battle\":{\"chars\":[{\"init\":20,\"name\":\"Hanbak\",\"states\":[{\"appliedOnInit\":20,\"appliedOnRound\":1,\"endCondition\":\"sv\",\"name\":\"slow\"}],\"thp\":0,\"wound\":0},{\"init\":10,\"name\":\"Jim\",\"states\":[],\"thp\":0,\"wound\":0},{\"init\":0,\"name\":\"웹\",\"states\":[],\"thp\":0,\"wound\":7},{\"init\":-10,\"name\":\"Ogre\",\"states\":[],\"thp\":0,\"wound\":0}],\"current\":{\"init\":20,\"name\":\"Hanbak\",\"states\":[{\"appliedOnInit\":20,\"appliedOnRound\":1,\"endCondition\":\"sv\",\"name\":\"slow\"}],\"thp\":0,\"wound\":0},\"round\":1},\"name\":\"inBattle\",\"statString\":\"Jim{init=0}{surge-0, ap-0}, Ogre{init=0}{surge-0, ap-0}, Hanbak{init=0}{surge-0, ap-0}, 웹{init=0}{surge-0, ap-0}\"}",
    // inBattle.toJsonObject().toString());
    // }

    // @Test
    // public void testLoadJSONObject() {
    // Game game = Game.load(inBattle.toJsonObject());
    // System.out.println(game);
    // }

    @Test
    public void testLoadString() {
        fail("Not yet implemented");
    }

    @Test
    public void testAddPc() {
        fail("Not yet implemented");
    }

    @Test
    public void testMapAlias() {
        fail("Not yet implemented");
    }

    @Test
    public void testHasAliases() {
        fail("Not yet implemented");
    }

    @Test
    public void testAddAlias() {
        fail("Not yet implemented");
    }

    @Test
    public void testRemoveAlias() {
        fail("Not yet implemented");
    }

    @Test
    public void testFindCharByNameOrAbbre() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetStatStringWithParameter() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetStatString() {
        fail("Not yet implemented");
    }

    @Test
    public void testInBattle() {
        fail("Not yet implemented");
    }

    @Test
    public void testEndBattle() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetTopic() {
        fail("Not yet implemented");
    }

    @Test
    public void testApplyState() {
        fail("Not yet implemented");
    }

    @Test
    public void testRemoveState() {
        fail("Not yet implemented");
    }

    @Test
    public void testDamage() {
        fail("Not yet implemented");
    }

    @Test
    public void testHeal() {
        fail("Not yet implemented");
    }

    @Test
    public void testRenameChar() {
        fail("Not yet implemented");
    }
}
