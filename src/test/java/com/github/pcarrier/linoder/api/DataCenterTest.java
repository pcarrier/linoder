package com.github.pcarrier.linoder.api;

import com.github.pcarrier.linoder.api.exceptions.RemoteCallException;
import com.github.pcarrier.linoder.model.DataCenter;
import org.junit.Assert;
import org.junit.Test;

import java.net.URISyntaxException;
import java.util.Collection;

public class DataCenterTest extends LiveTest {
    public DataCenterTest() throws URISyntaxException {
        super();
    }

    @Test
    public void listDC() throws RemoteCallException {
        Collection<DataCenter> dataCenters = getAPI().getAvailableDataCenters().values();
        Assert.assertTrue(dataCenters.size() > 4);

        DataCenter london = null;
        for (DataCenter dc : dataCenters) {
            if (dc.getLocation().contains("London")) {
                london = dc;
                break;
            }
        }

        Assert.assertNotNull(london);
    }
}
