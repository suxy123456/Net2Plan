package com.net2plan;

import com.net2plan.interfaces.networkDesign.Net2PlanException;
import com.net2plan.interfaces.networkDesign.NetPlan;
import com.net2plan.internal.Version;
import com.net2plan.utils.InternalUtils;
import com.shc.easyjson.JSON;
import com.shc.easyjson.JSONArray;
import com.shc.easyjson.JSONObject;
import com.shc.easyjson.JSONValue;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.util.Map;

public class Net2PlanOaaSClient
{
    private String baseURL;
    private Client client;
    private WebTarget target;
    private final int defaultPort = 8080;
    public Net2PlanOaaSClient(String ipAddress, String user, String password, int... optionalPort)
    {
        int port;
        if(optionalPort.length == 0)
            port = defaultPort;
        else if(optionalPort.length == 1)
            port = optionalPort[0];
        else
            throw new Net2PlanException("More than one port is not allowed");

        this.baseURL =  "http://"+ipAddress+":"+port+"/net2plan-oaas-server-"+ Version.getVersion()+"/OaaS";
        this.client = ClientBuilder.newClient().register(MultiPartFeature.class);
        this.target = this.client.target(baseURL);
    }

    public Response uploadCatalog(File catalogFile)
    {
        WebTarget this_target = target.path("catalogs");

        //File f = new File("C:\\Users\\César\\Desktop\\Net2Plan-0.6.1\\workspace\\BuiltInExamples.jar");
        FileDataBodyPart body = new FileDataBodyPart("file",catalogFile);
        MultiPart multi = new MultiPart();
        multi.bodyPart(body);

        Invocation.Builder inv = this_target.request(MediaType.MULTIPART_FORM_DATA).accept(MediaType.APPLICATION_JSON);
        Response r = inv.post(Entity.entity(multi,MediaType.MULTIPART_FORM_DATA));

        return r;
    }

    public Response execute(InternalUtils.ExecutionType type, String name, Map<String, String> userParams, NetPlan netPlan)
    {
        WebTarget this_target = target.path("execute");

        JSONObject json = new JSONObject();
        json.put("type",new JSONValue(type.toString()));
        //json.put("name",new JSONValue("com.net2plan.examples.ocnbook.offline.Offline_fa_ospfWeightOptimization_EA"));
        json.put("name",new JSONValue(name));
        if(userParams == null || userParams.size() == 0)
            json.put("userParams", new JSONValue(new JSONArray()));
        else
            json.put("userParams", new JSONValue());
        //Hacer un método que parsee un mapa a JSONObject;

        //File topologyFile = new File("C:\\Users\\César\\Desktop\\Net2Plan-0.6.1\\workspace\\data\\networkTopologies\\example7nodes.n2p");
        JSONObject netPlanJSON = netPlan.saveToJSON();

        json.put("netPlan", new JSONValue(netPlanJSON));

        String json_string = JSON.write(json);
        System.out.println(json_string);

        Invocation.Builder inv = target.request(MediaType.APPLICATION_JSON_TYPE).accept(MediaType.APPLICATION_JSON_TYPE);
        Response r = inv.post(Entity.entity(json_string, MediaType.APPLICATION_JSON_TYPE));

        return r;
    }
}