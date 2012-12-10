package com.github.pcarrier.linoder.api;

import com.github.pcarrier.linoder.api.exceptions.RemoteCallException;
import junit.framework.Assert;
import org.junit.Test;
import us.monoid.json.JSONException;
import us.monoid.json.JSONObject;

import java.net.URISyntaxException;
import java.util.HashMap;

public class EchoTest extends LiveTest {
    public EchoTest() throws URISyntaxException {
    }

    @Test
    public void testEcho() throws RemoteCallException, JSONException {
        HashMap<String, String> parms = new HashMap<String, String>();
        parms.put("foo", "1");
        parms.put("bar", "hello");

        JSONObject res = (JSONObject) getAPI().run("test.echo", parms);
        Assert.assertEquals(res.getInt("foo"), 1);
        Assert.assertEquals(res.getString("bar"), "hello");
    }
}
