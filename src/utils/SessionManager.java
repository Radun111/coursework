package utils;

public class SessionManager {
    private static String loggedInStudentId = null;

    public static void setLoggedInStudentId(String id) { 
        loggedInStudentId = id; 
    }

    public static String getLoggedInStudentId() { 
        return loggedInStudentId; 
    }

    public static void clearSession() { 
        loggedInStudentId = null; 
    }

    public static boolean isLoggedIn() { 
        return loggedInStudentId != null; 
    }
}
