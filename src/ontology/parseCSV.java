

package ontology;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class parseCSV {

    private static final char DEFAULT_SEPARATOR = ',';
    private static final char DEFAULT_QUOTE = '"';
	private static String cvsLine2;
    
    public parseCSV () {
    	
    }
    
    /**
     * 
     * @param csvFile
     * @return
     * @throws FileNotFoundException
     */
    public HashMap<Integer,HashMap<String,String>> getMapCSV_region(String csvFile) throws FileNotFoundException {
    	
        HashMap<Integer,HashMap<String,String>> csvMap = new HashMap<Integer, HashMap<String,String>>();
        Scanner scanner = new Scanner(new File(csvFile));
        int i=0;
        while (scanner.hasNext()) {
        	List<String> line = parseLine(scanner.nextLine());
        	if (i != 0) {
        		
                HashMap<String,String> rowCsvMap = new HashMap<String,String>();
                rowCsvMap.put("Date",line.get(0));
                //rowCsvMap.put("Country",line.get(1));
                //rowCsvMap.put("CodeRegion",line.get(2));
                rowCsvMap.put("Region",line.get(3));
                //rowCsvMap.put("Latitude",line.get(4));
                //rowCsvMap.put("Longitude",line.get(5));
                rowCsvMap.put("RecoveredWithSymptoms",line.get(6));
                rowCsvMap.put("IntensiveCare",line.get(7));
                rowCsvMap.put("TotalHospitalized",line.get(8));
                rowCsvMap.put("HomeIsolation",line.get(9));
                rowCsvMap.put("TotalPositves",line.get(10));
                rowCsvMap.put("DischargedHealed",line.get(13));
                rowCsvMap.put("Deceased",line.get(14));
                rowCsvMap.put("TotalCases",line.get(15));
                rowCsvMap.put("Swabs",line.get(16));
                rowCsvMap.put("TestedCases",line.get(17));
                
                csvMap.put(i,rowCsvMap);		
        	}
            i += 1;
        }
        scanner.close();
        
        return csvMap;
    }
    /**
     * 
     * @param cvsLine
     * @return
     */
    public static List<String> parseLine(String cvsLine) {
        return parseLine(cvsLine, DEFAULT_SEPARATOR, DEFAULT_QUOTE);
    }
    /**
     * 
     * @param cvsLine
     * @param separators
     * @return
     */
    public static List<String> parseLine(String cvsLine, char separators) {
        return parseLine(cvsLine, separators, DEFAULT_QUOTE);
    }
    /**
     * 
     * @param cvsLine
     * @param separators
     * @param customQuote
     * @return
     */
    public static List<String> parseLine(String cvsLine, char separators, char customQuote) {

        List<String> result = new ArrayList<>();

        cvsLine2 = cvsLine;
		if (cvsLine2 == null && cvsLine2.isEmpty()) {
            return result;
        }

        if (customQuote == ' ') {
            customQuote = DEFAULT_QUOTE;
        }

        if (separators == ' ') {
            separators = DEFAULT_SEPARATOR;
        }

        StringBuffer curVal = new StringBuffer();
        boolean inQuotes = false;
        boolean startCollectChar = false;
        boolean doubleQuotesInColumn = false;

        char[] chars = cvsLine2.toCharArray();

        for (char ch : chars) {

            if (inQuotes) {
                startCollectChar = true;
                if (ch == customQuote) {
                    inQuotes = false;
                    doubleQuotesInColumn = false;
                } else {

                    //Fixed : allow "" in custom quote enclosed
                    if (ch == '\"') {
                        if (!doubleQuotesInColumn) {
                            curVal.append(ch);
                            doubleQuotesInColumn = true;
                        }
                    } else {
                        curVal.append(ch);
                    }

                }
            } else {
                if (ch == customQuote) {

                    inQuotes = true;

                    //Fixed : allow "" in empty quote enclosed
                    if (chars[0] != '"' && customQuote == '\"') {
                        curVal.append('"');
                    }

                    //double quotes in column will hit this!
                    if (startCollectChar) {
                        curVal.append('"');
                    }

                } else if (ch == separators) {

                    result.add(curVal.toString());

                    curVal = new StringBuffer();
                    startCollectChar = false;

                } else if (ch == '\r') {
                    //ignore LF characters
                    continue;
                } else if (ch == '\n') {
                    //the end, break!
                    break;
                } else {
                    curVal.append(ch);
                }
            }

        }

        result.add(curVal.toString());

        return result;
    }

}
