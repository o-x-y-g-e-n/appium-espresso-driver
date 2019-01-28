package io.appium.espressoserver.test.model;

import com.google.gson.Gson;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.appium.espressoserver.lib.model.BackdoorMethodArg;
import io.appium.espressoserver.lib.model.MobileBackdoorMethod;
import io.appium.espressoserver.lib.model.MobileBackdoorParams;
import io.appium.espressoserver.test.assets.Helpers;

import static io.appium.espressoserver.lib.model.MobileBackdoorParams.InvocationTarget.ACTIVITY;
import static junit.framework.Assert.fail;
import static org.junit.Assert.*;

public class MobileBackdoorMethodTest {

    @Test
    public void shouldParseArgumentsAndTypes() {
        MobileBackdoorMethod method = new MobileBackdoorMethod();
        List<BackdoorMethodArg> args = new ArrayList<>();
        BackdoorMethodArg arg1 = new BackdoorMethodArg("java.lang.String", "Oh");
        args.add(arg1);

        BackdoorMethodArg arg2 = new BackdoorMethodArg("java.lang.Integer", "10");
        args.add(arg2);

        BackdoorMethodArg arg3 = new BackdoorMethodArg("int", "20");
        args.add(arg3);

        BackdoorMethodArg arg4 = new BackdoorMethodArg("Boolean", "true");
        args.add(arg4);

        method.setArgs(args);
        assertArrayEquals(new Class[]{String.class, Integer.class, int.class, Boolean.class}, method.getArgumentTypes());
        assertArrayEquals(new Object[]{"Oh", 10, 20, true}, method.getArguments());

    }

    @Test
    public void shouldNotAllowInvalidArgumentTypes() {
        MobileBackdoorMethod method = new MobileBackdoorMethod();
        List<BackdoorMethodArg> args = new ArrayList<>();
        BackdoorMethodArg arg1 = new BackdoorMethodArg("java.lang.Lol", "Oh");
        args.add(arg1);
        method.setArgs(args);
        try {
            method.getArgumentTypes();
            fail("expected exception was not occured.");
        } catch (Exception e) {
            assertTrue(e.getMessage().contains(
                    String.format("Class not found: java.lang.Lol")));
        }

    }

    @Test
    public void shouldNotAllowIncompatibleValueForGivenType() {
        MobileBackdoorMethod method = new MobileBackdoorMethod();
        List<BackdoorMethodArg> args = new ArrayList<>();
        BackdoorMethodArg arg1 = new BackdoorMethodArg("int", "lol");
        args.add(arg1);
        method.setArgs(args);
        try {
            method.getArguments();
            fail("expected exception was not occured.");
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("For input string: \"lol\""));
        }

    }

    @Test
    public void shouldPullMethodsWithArguments() throws IOException {
        String backdoorMethods = Helpers.readAssetFile("backdoor-methods.json");
        MobileBackdoorParams params = MobileBackdoorParams.class.cast((new Gson()).fromJson(backdoorMethods, MobileBackdoorParams.class));
        List<MobileBackdoorMethod> mobileBackdoor = params.getMethods();

        assertEquals(ACTIVITY, params.getTarget());

        MobileBackdoorMethod firstMethod = mobileBackdoor.get(0);
        assertEquals("firstMethod", firstMethod.getName());
        assertArrayEquals(new Class[]{boolean.class, int.class, byte.class, char.class}, firstMethod.getArgumentTypes());
        assertArrayEquals(new Object[]{true, 1, (byte) 20, 'X'}, firstMethod.getArguments());

        MobileBackdoorMethod secondMethod = mobileBackdoor.get(1);
        assertEquals("secondMethod", secondMethod.getName());
        assertArrayEquals(new Class[]{boolean.class, int.class, byte.class, Character.class, String.class}, secondMethod.getArgumentTypes());
        assertArrayEquals(new Object[]{true, 1, (byte) 20, 'X', "Y"}, secondMethod.getArguments());

    }
}