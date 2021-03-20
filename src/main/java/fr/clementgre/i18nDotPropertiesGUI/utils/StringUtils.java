package fr.clementgre.i18nDotPropertiesGUI.utils;

import java.util.Arrays;
import java.util.Map;

public class StringUtils {

    public static int levenshteinDistanceWithLengths(String s1, String s2){
        int max =(Math.max(s1.length(), s2.length()));
        return  levenshteinDistance(s1, s2) * 1000 / (max > 0 ? max : 1);
    }

    // from https://en.wikibooks.org/wiki/Algorithm_Implementation/Strings/Levenshtein_distance#Java
    public static int levenshteinDistance (CharSequence lhs, CharSequence rhs) {
        int len0 = lhs.length() + 1;
        int len1 = rhs.length() + 1;

        // the array of distances
        int[] cost = new int[len0];
        int[] newcost = new int[len0];

        // initial cost of skipping prefix in String s0
        for (int i = 0; i < len0; i++) cost[i] = i;

        // dynamically computing the array of distances

        // transformation cost for each letter in s1
        for (int j = 1; j < len1; j++) {
            // initial cost of skipping prefix in String s1
            newcost[0] = j;

            // transformation cost for each letter in s0
            for(int i = 1; i < len0; i++) {
                // matching current letters in both strings
                int match = (lhs.charAt(i - 1) == rhs.charAt(j - 1)) ? 0 : 1;

                // computing cost for each transformation
                int cost_replace = cost[i - 1] + match;
                int cost_insert  = cost[i] + 1;
                int cost_delete  = newcost[i - 1] + 1;

                // keep minimum cost
                newcost[i] = Math.min(Math.min(cost_insert, cost_delete), cost_replace);
            }

            // swap cost/newcost arrays
            int[] swap = cost; cost = newcost; newcost = swap;
        }

        // the distance is the cost for transforming all letters in both strings
        return cost[len0 - 1];
    }

    public static String removeBefore(String string, String rejex){
        if(rejex.isEmpty()) return string;
        int index = string.indexOf(rejex);

        if(index == -1) return string;
        if(index < string.length()) return string.substring(index + rejex.length());

        return "";
    }
    public static String removeBeforeNotEscaped(String string, String rejex){

        int fromIndex = 0;
        while(true){

            int index = string.indexOf(rejex, fromIndex);
            if(index == -1) return string;

            if(!string.startsWith("\\", index-1)){
                if(index < string.length()) return string.substring(index + rejex.length());
                return "";
            }else{
                fromIndex = index + 1;
            }

        }
    }

    public static Map.Entry<String, Integer> getLastInt(String expression){
        String stringResult = expression;
        StringBuffer result = new StringBuffer();

        for(int i = expression.length() - 1; i >= 0; i--){
            try{
                result.append(Integer.parseInt(expression.substring(i, i+1)));
                stringResult = stringResult.substring(0, i);
            }catch(NumberFormatException ignored){
                break;
            }
        }

        if(result.toString().isEmpty()) return Map.entry(expression, -1);
        return Map.entry(stringResult, Integer.parseInt(result.reverse().toString()));
    }

    public static String incrementName(String name){

        Map.Entry<String, Integer> lastIntData = getLastInt(name);

        if(lastIntData.getValue() != -1){
            return lastIntData.getKey() + (lastIntData.getValue()+1);
        }

        if(name.length() == 1){
            if(name.replaceAll("^[A-Ya-y]", "").isEmpty()){
                return Character.toString(name.charAt(0) + 1);
            }
        }

        return name;
    }

    public static String upperCaseFirstChar(String str){
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }


    public static String removeBeforeLastRegex(String string, String rejex){
        if(rejex.isEmpty()) return string;
        int index = string.lastIndexOf(rejex);

        if(index == -1) return string;
        if(index < string.length()) return string.substring(index + rejex.length());

        return "";
    }
    public static String removeAfterLastRegex(String string, String rejex){
        if(rejex.isEmpty()) return string;
        int index = string.lastIndexOf(rejex);

        if(index == -1) return string;
        if(index < string.length()) return string.substring(0, index);

        return "";
    }
    public static String removeAfterLastRegexIgnoringCase(String string, String rejex){
        if(rejex.isEmpty()) return string;
        int index = string.toLowerCase().lastIndexOf(rejex.toLowerCase());

        if(index == -1) return string;
        if(index < string.length()) return string.substring(0, index);

        return "";
    }
    public static String removeAfter(String string, String rejex){
        if(rejex.isEmpty()) return "";
        int index = string.indexOf(rejex);

        if(index == -1) return string;
        if(index < string.length()) return string.substring(0, index);

        return "";
    }

    public static double clamp(double val, double min, double max) {
        return Math.max(min, Math.min(max, val));
    }
    public static float clamp(float val, float min, float max) {
        return Math.max(min, Math.min(max, val));
    }
    public static int clamp(int val, int min, int max) {
        return Math.max(min, Math.min(max, val));
    }


    public static Double getDouble(String text){
        try{
            return Double.parseDouble(text);
        }catch (NumberFormatException e){
            return null;
        }
    }
    public static Integer getInt(String text){
        try{
            return Integer.parseInt(text);
        }catch (NumberFormatException e){
            return null;
        }
    }
    public static int getAlwaysInt(String text){
        try{
            return Integer.parseInt(text);
        }catch (NumberFormatException e){
            return 0;
        }
    }
    public static Long getLong(String text){
        try{
            return Long.parseLong(text);
        }catch (NumberFormatException e){
            return null;
        }
    }
    public static long getAlwaysLong(String text){
        try{
            return Long.parseLong(text);
        }catch (NumberFormatException e){
            return 0;
        }
    }
    public static double getAlwaysDouble(String text){
        try{
            return Double.parseDouble(text);
        }catch (NumberFormatException e){
            return 0;
        }
    }


    public static String[] cleanArray(String[] array) {
        return Arrays.stream(array).filter(x -> !x.isBlank()).toArray(String[]::new);
    }

    public static boolean getAlwaysBoolean(String text) {
        return "true".equalsIgnoreCase(text);
    }
    public static Boolean getBoolean(String text) {
        if("true".equalsIgnoreCase(text)){
            return true;
        }else if("false".equalsIgnoreCase(text)){
            return false;
        }else{
            return null;
        }
    }

}
