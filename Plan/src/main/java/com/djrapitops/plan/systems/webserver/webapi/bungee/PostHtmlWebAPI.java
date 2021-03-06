/* 
 * Licence is provided in the jar as license.yml also here:
 * https://github.com/Rsl1122/Plan-PlayerAnalytics/blob/master/Plan/src/main/resources/license.yml
 */
package main.java.com.djrapitops.plan.systems.webserver.webapi.bungee;

import main.java.com.djrapitops.plan.Settings;
import main.java.com.djrapitops.plan.api.IPlan;
import main.java.com.djrapitops.plan.api.exceptions.WebAPIException;
import main.java.com.djrapitops.plan.systems.info.InformationManager;
import main.java.com.djrapitops.plan.systems.webserver.PageCache;
import main.java.com.djrapitops.plan.systems.webserver.response.AnalysisPageResponse;
import main.java.com.djrapitops.plan.systems.webserver.response.InspectPageResponse;
import main.java.com.djrapitops.plan.systems.webserver.response.Response;
import main.java.com.djrapitops.plan.systems.webserver.webapi.WebAPI;
import org.apache.commons.lang3.text.StrSubstitutor;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * WebAPI for posting Html pages such as Inspect or server pages.
 *
 * @author Rsl1122
 */
public class PostHtmlWebAPI extends WebAPI {

    @Override
    public Response onRequest(IPlan plugin, Map<String, String> variables) {
        try {
            String html = variables.get("html");
            String target = variables.get("target");
            InformationManager infoManager = plugin.getInfoManager();
            switch (target) {
                case "inspectPage":
                    String uuid = variables.get("uuid");

                    Map<String, String> map = new HashMap<>();
                    map.put("networkName", Settings.BUNGEE_NETWORK_NAME.toString());

                    PageCache.cachePage("inspectPage:" + uuid, () -> new InspectPageResponse(infoManager, UUID.fromString(uuid), StrSubstitutor.replace(html, map)));
                    break;
                case "analysisPage":
                    PageCache.cachePage("analysisPage:" + variables.get("sender"), () -> new AnalysisPageResponse(html));
                    break;
                default:
                    return badRequest("Faulty Target");
            }
            return success();
        } catch (NullPointerException e) {
            return badRequest(e.toString());
        }
    }

    @Override
    public void sendRequest(String address) throws WebAPIException {
        throw new IllegalStateException("Wrong method call for this WebAPI, call sendRequest(String, UUID, UUID) instead.");
    }

    public void sendInspectHtml(String address, UUID uuid, String html) throws WebAPIException {
        addVariable("uuid", uuid.toString());
        addVariable("html", html);
        addVariable("target", "inspectPage");
        super.sendRequest(address);
    }

    public void sendAnalysisHtml(String address, String html) throws WebAPIException {
        addVariable("html", html);
        addVariable("target", "analysisPage");
        super.sendRequest(address);
    }
}