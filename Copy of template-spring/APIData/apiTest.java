

import com.fasterxml.jackson.core.JsonParser;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.mapsengine.MapsEngine;
import com.google.api.services.mapsengine.MapsEngineRequestInitializer;
import com.google.api.services.mapsengine.model.Feature;
import com.google.api.services.mapsengine.model.FeaturesListResponse;
import com.google.api.services.mapsengine.model.GeoJsonPoint;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;
import org.json.XML;

/** Java "Hello, world!" example using the client library */
public class apiTest {

  static final String SAMPLE_TABLE_ID = "12421761926155747447-06672618218968397709";
  static final String PUBLIC_API_KEY = "AIzaSyD2A-fa-gSewkCLL-fK0BfRNTcGkrSxOmM";

  public static void main(String[] args) throws Exception {
    HttpTransport transport = new NetHttpTransport();
    JsonFactory jsonFactory = new GsonFactory();

    // This request initializer will ensure the API key is sent with every HTTP request.
    MapsEngineRequestInitializer apiKeyInitializer =
        new MapsEngineRequestInitializer(PUBLIC_API_KEY);

    MapsEngine engine = new MapsEngine.Builder(transport, jsonFactory, null)
        .setMapsEngineRequestInitializer(apiKeyInitializer)
        .setApplicationName("Google-MapsEngineSample/1.0")
        .build();
    long before = System.currentTimeMillis();
    String HotWireData = httpGet("http://api.hotwire.com/v1/deal/hotel?dest=chicago&apikey=52hz382hkxv22acvxjuxhq8e&limit=300");
    System.out.println("Time: " + (System.currentTimeMillis() - before));
    String GoogleAPIData = httpGet("https://maps.googleapis.com/maps/api/geocode/json?address=1311+S+5th+St,+Waco,+TX&key=" + PUBLIC_API_KEY);
    //String EventfulAPIData = httpGet("http://api.eventful.com/json/events/search?&keywords=Music&location=San+Diego&app_key=BznmrDJCddHGGfRJ");
    
    JSONObject xmlJSONobj = XML.toJSONObject(HotWireData);
    HotWireData = HotWireData.toString();
    HotWireData = makePretty(xmlJSONobj.toString());
    //EventfulAPIData = makePretty(EventfulAPIData);
    System.out.println(HotWireData);
    System.out.println(GoogleAPIData);
    //System.out.println(EventfulAPIData);
    
    //readFeaturesFromTable(engine);
  }

  public static void readFeaturesFromTable(MapsEngine me) throws IOException {
    // Query the table for offices in WA that are within 100km of Perth.
    FeaturesListResponse featResp =  me.tables().features().list(SAMPLE_TABLE_ID)
        .setVersion("published")
        .setWhere("State='WA' AND ST_DISTANCE(geometry,ST_POINT(115.8589,-31.9522)) < 100000")
        .execute();

    for (Feature feat : featResp.getFeatures()) {
      System.out.println(
          "Properties: " + "\n\t" +
          "Name: " + feat.getProperties().get("Fcilty_nam") + "\n\t" +
          "Geometry Type: " + feat.getGeometry().getType());

      if (feat.getGeometry() instanceof GeoJsonPoint)  {
        GeoJsonPoint point = (GeoJsonPoint) feat.getGeometry();
        System.out.println("\t" +
            "Longitude: " + point.getCoordinates().get(0) + ", " +
            "Latitude: " +  point.getCoordinates().get(1));
      } else {
        System.out.println("Only points are expected in this table!");
        return;
      }
    }
    
  }
  
  public static String makePretty(String JSONpretty) {
	int i = 0;
	StringBuilder stringFinal = new StringBuilder();
	StringBuilder sb = new StringBuilder();
	
	while (i != JSONpretty.length()) {
		sb.append(JSONpretty.charAt(i));
		if (sb.toString().contains("{") || sb.toString().contains("[") || sb.toString().contains("]") || sb.toString().contains("}") || sb.toString().contains(",")) {
			stringFinal.append(sb.toString() + "\n");
			sb.delete(0, i);
		}
		i++;
	}
	
	return stringFinal.toString();
  }
  
  public static String httpGet(String urlStr) throws IOException {
	  URL url = new URL(urlStr);
	  HttpURLConnection conn =
	      (HttpURLConnection) url.openConnection();

	  if (conn.getResponseCode() != 200) {
	    throw new IOException(conn.getResponseMessage());
	  }

	  // Buffer the result into a string
	  BufferedReader rd = new BufferedReader(
	      new InputStreamReader(conn.getInputStream()));
	  StringBuilder sb = new StringBuilder();
	  String line;
	  while ((line = rd.readLine()) != null) {
		if (line.contains("{") || line.contains("[") || line.contains("]") || line.contains("}") || line.contains(",")) {
			line += "\n";
		}
	    sb.append(line);
	  }
	  rd.close();

	  conn.disconnect();
	  return sb.toString();
	}
}