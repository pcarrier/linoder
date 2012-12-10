package com.github.pcarrier.linoder.api;

import com.github.pcarrier.linoder.api.exceptions.APIFailureException;
import com.github.pcarrier.linoder.api.exceptions.InvalidResponseException;
import com.github.pcarrier.linoder.api.exceptions.NetworkFailureException;
import com.github.pcarrier.linoder.api.exceptions.RemoteCallException;
import com.github.pcarrier.linoder.model.*;
import us.monoid.json.JSONArray;
import us.monoid.json.JSONException;
import us.monoid.json.JSONObject;
import us.monoid.web.Resty;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.*;

public class API {

    private final Resty resty;
    private final String baseURI;
    private final boolean debug;

    private static final String PARAM_ENCODING = "UTF-8";

    API(Resty resty, String baseURI, boolean debug) {
        this.resty = resty;
        this.baseURI = baseURI;
        this.debug = debug;
    }

    /* The core API logic is here. The rest is just Pierre being obnoxious. */
    public Object run(String method, Map<String, String> args) throws RemoteCallException {
        StringBuilder uriBuilder = new StringBuilder(baseURI);
        uriBuilder.append("api_responseFormat=json&api_action=");
        uriBuilder.append(method);

        if (args != null) {
            try {
                for (Map.Entry<String, String> entry : args.entrySet()) {
                    uriBuilder.append("&");
                    uriBuilder.append(URLEncoder.encode(entry.getKey(), PARAM_ENCODING));
                    uriBuilder.append("=");
                    uriBuilder.append(URLEncoder.encode(entry.getValue(), PARAM_ENCODING));
                }
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Cannot encode in UTF-8!");
            }
        }

        try {
            String uri = uriBuilder.toString();

            if (debug)
                System.err.printf("Querying %s\n", uri);

            JSONObject envelop = resty.json(new URI(uri)).object();

            JSONArray errors = envelop.getJSONArray("ERRORARRAY");

            if (errors != null) {
                if (errors.length() != 0) {
                    LinkedList<String> errorDescriptions = new LinkedList<String>();
                    for (int i = 0; i < errors.length(); i++) {
                        errorDescriptions.add(errors.getJSONObject(i).getString("ERRORMESSAGE"));
                    }

                    throw new APIFailureException(errorDescriptions);
                }
            }

            Object data = envelop.get("DATA");

            if (debug)
                System.err.printf("Got %s\n", data);

            return data;

        } catch (IOException e) {
            throw new NetworkFailureException(e);
        } catch (URISyntaxException e) {
            throw new InvalidResponseException(e);
        } catch (JSONException e) {
            throw new InvalidResponseException(e);
        }
    }

    public final SortedMap<Integer, DataCenter> getAvailableDataCenters() throws RemoteCallException {
        try {
            JSONArray resp = (JSONArray) run("avail.datacenters", null);

            final SortedMap<Integer, DataCenter> res = new TreeMap<Integer, DataCenter>();

            for (int i = 0; i < resp.length(); i++) {
                JSONObject dcObject = resp.getJSONObject(i);

                int id = dcObject.getInt("DATACENTERID");
                String location = dcObject.getString("LOCATION");

                res.put(id, new DataCenter(id, location));
            }

            return res;
        } catch (ClassCastException e) {
            throw new InvalidResponseException(e);
        } catch (JSONException e) {
            throw new InvalidResponseException(e);
        }
    }

    public final SortedMap<Integer, Plan> getAvailablePlans() throws RemoteCallException {
        final SortedMap<Integer, Plan> res = new TreeMap<Integer, Plan>();

        try {
            JSONArray resp = (JSONArray) run("avail.linodeplans", null);

            for (int i = 0; i < resp.length(); i++) {
                JSONObject planObject = resp.getJSONObject(i);
                Plan plan = new Plan(planObject);
                res.put(plan.getId(), plan);
            }

            return res;
        } catch (ClassCastException e) {
            throw new InvalidResponseException(e);
        } catch (JSONException e) {
            throw new InvalidResponseException(e);
        }
    }

    public final SortedMap<Integer, Linode> getLinodes() throws RemoteCallException {

        try {
            JSONArray resp = (JSONArray) run("linode.list", null);

            final SortedMap<Integer, Linode> res = new TreeMap<Integer, Linode>();

            for (int i = 0; i < resp.length(); i++) {
                JSONObject linodeObject = resp.getJSONObject(i);
                Linode linode = new Linode(linodeObject);
                res.put(linode.getId(), linode);
            }

            return res;
        } catch (ClassCastException e) {
            throw new InvalidResponseException(e);
        } catch (JSONException e) {
            throw new InvalidResponseException(e);
        }
    }

    public final Linode getLinode(int id) throws RemoteCallException {
        final HashMap<String, String> params = new HashMap<String, String>();
        params.put("LinodeID", Integer.toString(id));

        JSONArray resp = (JSONArray) run("linode.list", params);
        try {
            return new Linode((JSONObject) resp.get(0));
        } catch (JSONException e) {
            return null;
        } catch (ClassCastException e) {
            throw new InvalidResponseException(e);
        }
    }

    public final Linode getLinode(String idOrLabel) throws RemoteCallException {
        try {
            return getLinode(Integer.parseInt(idOrLabel));
        } catch (NumberFormatException e) {
            SortedMap<Integer, Linode> linodes = getLinodes();
            for (Linode linode : linodes.values()) {
                if (linode.getLabel().equals(idOrLabel)) {
                    return linode;
                }
            }
        }
        return null;
    }

    public final SortedMap<Integer, Address> getAddresses(int linodeId) throws RemoteCallException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("LinodeID", Integer.toString(linodeId));

        try {
            final JSONArray resp = (JSONArray) this.run("linode.ip.list", params);

            final TreeMap<Integer, Address> res = new TreeMap<Integer, Address>();

            for (int i = 0; i < resp.length(); i++) {
                JSONObject addressObject = resp.getJSONObject(i);
                final Address address = new Address(addressObject);
                res.put(address.getId(), address);
            }

            return res;
        } catch (ClassCastException e) {
            throw new InvalidResponseException(e);
        } catch (JSONException e) {
            throw new InvalidResponseException(e);
        }
    }

    public final int cloneLinode(int orig, int dataCenterId, int planId, int paymentTerm) throws RemoteCallException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("LinodeID", Integer.toString(orig));
        params.put("DatacenterID", Integer.toString(dataCenterId));
        params.put("PlanID", Integer.toString(planId));
        params.put("PaymentTerm", Integer.toString(paymentTerm));

        try {
            JSONObject res = (JSONObject) run("linode.clone", params);
            return res.getInt("LinodeID");
        } catch (ClassCastException e) {
            throw new InvalidResponseException(e);
        } catch (JSONException e) {
            throw new InvalidResponseException(e);
        }
    }

    public final void bootLinode(int linodeId) throws RemoteCallException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("LinodeID", Integer.toString(linodeId));
        run("linode.boot", params);
    }

    public final void bootLinode(int linodeId, int configId) throws RemoteCallException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("LinodeID", Integer.toString(linodeId));
        params.put("ConfigID", Integer.toString(configId));
        run("linode.boot", params);
    }

    public final void rebootLinode(int linodeId) throws RemoteCallException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("LinodeID", Integer.toString(linodeId));
        run("linode.reboot", params);
    }

    public final void rebootLinode(int linodeId, int configId) throws RemoteCallException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("LinodeID", Integer.toString(linodeId));
        params.put("ConfigID", Integer.toString(configId));
        run("linode.reboot", params);
    }

    public final void shutdownLinode(int linodeId) throws RemoteCallException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("LinodeID", Integer.toString(linodeId));
        run("linode.shutdown", params);
    }

    public final void resizeLinode(int linodeId, int planId) throws RemoteCallException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("LinodeID", Integer.toString(linodeId));
        params.put("PlanID", Integer.toString(planId));
        run("linode.resize", params);
    }

    public final void updateLinode(int linodeId, LinodeUpdate update) throws RemoteCallException {
        final Map<String, String> params = update.toParams();
        params.put("LinodeID", Integer.toString(linodeId));
        run("linode.update", params);
    }

    public final void deleteLinode(int linodeId, boolean skipChecks) throws RemoteCallException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("LinodeID", Integer.toString(linodeId));
        params.put("skipChecks", skipChecks ? "1" : "0");
        run("linode.delete", params);
    }
}
