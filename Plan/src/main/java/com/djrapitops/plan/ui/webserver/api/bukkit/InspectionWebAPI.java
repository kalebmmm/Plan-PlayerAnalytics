/* 
 * Licence is provided in the jar as license.yml also here:
 * https://github.com/Rsl1122/Plan-PlayerAnalytics/blob/master/Plan/src/main/resources/license.yml
 */
package main.java.com.djrapitops.plan.ui.webserver.api.bukkit;

import main.java.com.djrapitops.plan.Plan;
import main.java.com.djrapitops.plan.data.UserData;
import main.java.com.djrapitops.plan.data.cache.PageCacheHandler;
import main.java.com.djrapitops.plan.ui.webserver.response.Response;
import main.java.com.djrapitops.plan.ui.webserver.response.api.BadRequestResponse;
import main.java.com.djrapitops.plan.ui.webserver.response.api.JsonResponse;
import main.java.com.djrapitops.plan.utilities.uuid.UUIDUtility;
import main.java.com.djrapitops.plan.utilities.webserver.api.WebAPI;

import java.util.Map;
import java.util.UUID;

/**
 * @author Fuzzlemann
 */
public class InspectionWebAPI implements WebAPI {
    @Override
    public Response onResponse(Plan plan, Map<String, String> variables) {
        String playerString = variables.get("player");

        if (playerString == null) {
            String error = "Player String not included";
            return PageCacheHandler.loadPage(error, () -> new BadRequestResponse(error));
        }

        UUID uuid = UUIDUtility.getUUIDOf(playerString);

        if (uuid == null) {
            String error = "UUID not found";
            return PageCacheHandler.loadPage(error, () -> new BadRequestResponse(error));
        }

        UserData userData = plan.getInspectCache().getFromCache(uuid);

        if (userData == null) {
            String error = "User not cached";
            return PageCacheHandler.loadPage(error, () -> new BadRequestResponse(error));
        }

        return PageCacheHandler.loadPage("inspectionJson: " + uuid, () -> new JsonResponse(plan.getInspectCache().getFromCache(uuid)));
    }
}