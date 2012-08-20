import java.awt.Color;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class PaletteFactory {
	
	public static String white = "white";
	public static String yellow = "yellow";
	public static String green = "green";
	public static String red = "red";
	public static String blue = "blue";
	public static String dark_blue = "dark_blue";

	
	public static  List<Map<String, Color>> generatePalettes() {

		List<Map<String, Color>> palettesList = new LinkedList<Map<String, Color>>();
		
		Map<String, Color> originalMap = new HashMap<String, Color>();
		originalMap.put(white, new Color(Integer.parseInt("fcfeff",16)));
		originalMap.put(yellow, new Color(Integer.parseInt("fdfa27",16)));
		originalMap.put(green, new Color(Integer.parseInt("008517",16)));
		originalMap.put(red, new Color(Integer.parseInt("f91807",16)));
		originalMap.put(blue, new Color(Integer.parseInt("003ef9",16)));
		originalMap.put(dark_blue, new Color(Integer.parseInt("0c2585",16)));
		palettesList.add(originalMap);
		
		Map<String, Color> pastelsMap = new HashMap<String, Color>();
		pastelsMap.put(white, new Color(Integer.parseInt("EBEBEB",16)));
		pastelsMap.put(yellow, new Color(Integer.parseInt("FFF79A",16)));
		pastelsMap.put(green, new Color(Integer.parseInt("C4DF9B",16)));
		pastelsMap.put(red, new Color(Integer.parseInt("F7977A",16)));
		pastelsMap.put(blue, new Color(Integer.parseInt("6ECFF6",16)));
		pastelsMap.put(dark_blue, new Color(Integer.parseInt("8493CA",16)));
		palettesList.add(pastelsMap);
		
/*		Map<String, Color> autumnMap = new HashMap<String, Color>();
		autumnMap.put(white, new Color(Integer.parseInt("dddddd",16)));
		autumnMap.put(yellow, new Color(Integer.parseInt("ABA000",16)));
		autumnMap.put(green, new Color(Integer.parseInt("007236",16)));
		autumnMap.put(red, new Color(Integer.parseInt("9E0B0F",16)));
		autumnMap.put(blue, new Color(Integer.parseInt("0076A3",16)));
		autumnMap.put(dark_blue, new Color(Integer.parseInt("003471",16)));
		palettesList.add(autumnMap);
*/		
		Map<String, Color> brownTownMap = new HashMap<String, Color>();
		brownTownMap.put(white, new Color(Integer.parseInt("EBEBEB",16)));
		brownTownMap.put(yellow, new Color(Integer.parseInt("C7B299",16)));
		brownTownMap.put(green, new Color(Integer.parseInt("736357",16)));
		brownTownMap.put(red, new Color(Integer.parseInt("C69C6E",16)));
		brownTownMap.put(blue, new Color(Integer.parseInt("8C6239",16)));
		brownTownMap.put(dark_blue, new Color(Integer.parseInt("754C24",16)));
		palettesList.add(brownTownMap);
		
		Map<String, Color> candyStoreMap = new HashMap<String, Color>();
		candyStoreMap.put(white, new Color(Integer.parseInt("dddddd",16)));
		candyStoreMap.put(yellow, new Color(Integer.parseInt("C8C8A9",16)));
		candyStoreMap.put(green, new Color(Integer.parseInt("83AF9B",16)));
		candyStoreMap.put(red, new Color(Integer.parseInt("FE4365",16)));
		candyStoreMap.put(blue, new Color(Integer.parseInt("FC9D9A",16)));
		candyStoreMap.put(dark_blue, new Color(Integer.parseInt("F9CDAD",16)));
		palettesList.add(candyStoreMap);
		
		Map<String, Color> goldFishMap = new HashMap<String, Color>();
		goldFishMap.put(white, new Color(Integer.parseInt("dddddd",16)));
		goldFishMap.put(yellow, new Color(Integer.parseInt("F38630",16)));
		goldFishMap.put(green, new Color(Integer.parseInt("E0E4CC",16)));
		goldFishMap.put(red, new Color(Integer.parseInt("FA6900",16)));
		goldFishMap.put(blue, new Color(Integer.parseInt("A7DBD8",16)));
		goldFishMap.put(dark_blue, new Color(Integer.parseInt("69D2E7",16)));
		palettesList.add(goldFishMap);
		
		return palettesList;
	}
}
