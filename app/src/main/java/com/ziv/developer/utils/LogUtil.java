package com.ziv.developer.utils;

import android.util.Log;

import com.ziv.developer.BuildConfig;

/**
 * DEBUG版本日志默认全部打开，OPEN_LOG标志位失效
 * RELEASE版本后，由OPEN_LOG控制日志是否开启
 * OPEN_MSG_CLASS_HEAD标志位单独控制是否输出详细信息头日志
 *
 * setLogLevel控制输出日志级别，一般用于日志持久化系统，默认开启DEBUG，INFO，WARN，ERROR四个级别
 */
public class LogUtil {
    private static final String TAG = "LogUtil";
    private static boolean DEBUG = BuildConfig.DEBUG;

    private static final int CURRENT_STACK_INDEX = 6;

    private static final boolean OPEN_LOG = true;
    private static final boolean OPEN_MSG_CLASS_HEAD = true;

    private static final int LOG_LEVEL_VERBOSE = Log.VERBOSE;
    private static final int LOG_LEVEL_DEBUG = Log.DEBUG;
    private static final int LOG_LEVEL_INFO = Log.INFO;
    private static final int LOG_LEVEL_WARN = Log.WARN;
    private static final int LOG_LEVEL_ERROR = Log.ERROR;
    private static final int LOG_LEVEL_ASSERT = Log.ASSERT;

    // default value
    private static int level = LOG_LEVEL_ERROR;

    /**
     * 输出日志级别控制，默认LOG_LEVEL_ERROR = Log.ERROR
     *
     * @param logLevel 日志级别
     */
    public void setLogLevel(int logLevel){
        level = logLevel;
    }

    public static void i(String tag, String msg) {
        if ((DEBUG || OPEN_LOG) && (level >= LOG_LEVEL_INFO)) {
            String logTag = getTag(tag);
            if (OPEN_MSG_CLASS_HEAD) {
                Log.i(logTag, getMsgHead() + msg);
            } else {
                Log.i(logTag, msg);
            }
        }
    }

    public static void d(String tag, String msg) {
        if ((DEBUG || OPEN_LOG) && (level >= LOG_LEVEL_DEBUG)) {
            String logTag = getTag(tag);
            if (OPEN_MSG_CLASS_HEAD) {
                Log.d(logTag, getMsgHead() + msg);
            } else {
                Log.d(logTag, msg);
            }
        }
    }

    public static void e(String tag, String msg) {
        if ((DEBUG || OPEN_LOG) && (level >= LOG_LEVEL_ERROR)) {
            String logTag = getTag(tag);
            if (OPEN_MSG_CLASS_HEAD) {
                Log.e(logTag, getMsgHead() + msg);
            } else {
                Log.e(logTag, msg);
            }
        }
    }

    public static void w(String tag, String msg) {
        if ((DEBUG || OPEN_LOG) && (level >= LOG_LEVEL_WARN)) {
            String logTag = getTag(tag);
            if (OPEN_MSG_CLASS_HEAD) {
                Log.w(logTag, getMsgHead() + msg);
            } else {
                Log.w(logTag, msg);
            }
        }
    }

    public static void processLog(String msg) {
        if (DEBUG || OPEN_LOG) {
            Log.d("Process", getMsgHead() + msg);
        }
    }

    public static void stateLog(String msg) {
        if (DEBUG || OPEN_LOG) {
            Log.d("State", getMsgHead() + msg);
        }
    }

    public static void timeLog(String tag, ITimeCoast timeCoast) {
        String logTag = getTag(tag);
        if (timeCoast == null) {
            Log.e(logTag, "time coast function is null.");
            return;
        }
        long startTime = System.currentTimeMillis();
        timeCoast.functionRun();
        Log.d(logTag, "time cost: " + (System.currentTimeMillis() - startTime));
    }

    /**
     * 针对网络请求结果输出较多被控制台截断
     */
    public static void largeLog(String tag, String msg) {
        int segmentSize = 3 * 1024;
        largeLog(tag, msg, segmentSize);
    }

    public static void largeLog(String tag, String msg, int segmentSize) {
        String logTag = getTag(tag);
        if (msg == null || msg.length() == 0) {
            Log.d(logTag, "msg length = 0 or msg is null.");
            return;
        }
        int length = msg.length();
        if (length > segmentSize) {
            // 循环分段输出超长部分
            while (msg.length() > segmentSize) {
                String logContent = msg.substring(0, segmentSize);
                msg = msg.replace(logContent, "");
                Log.d(logTag, logContent);
            }
        }
        // 长度小于限制后直接打印
        Log.d(logTag, msg);
    }

    private static String getTag(String tag){
        if (tag == null || tag.trim().equals("")){
            return TAG;
        }
        return tag;
    }

    /**
     * 获取日志信息头
     * [Class.method(line)-thread-time]
     */
    private static String getMsgHead() {
        return "[" + getThreadInfo() + "->" + getCallMethodInfo() + "]: ";
    }

    /**
     * 当前方法所在线程信息，进程号
     */
    private static String getThreadInfo() {
        Thread thread = Thread.currentThread();
        String name = thread.getName();
        long id = thread.getId();

        return name + "[" +id +"]";
    }

    /**
     * 获取调用方法信息
     * stackTrace[5]当前方法所在栈
     * stackTrace[6]调用当前方法的方法所在栈信息
     */
    private static String getCallMethodInfo() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
//        int stackIndex = 6;
//        String replace = stackTrace[stackIndex].toString().replace(BuildConfig.APPLICATION_ID + ".", "");
//        String className = stackTrace[stackIndex].getClassName();
//        String methodName = stackTrace[stackIndex].getMethodName();
//        String fileName = stackTrace[stackIndex].getFileName();
//        int lineNumber = stackTrace[stackIndex].getLineNumber();

        String info = stackTrace[CURRENT_STACK_INDEX].toString();
        int index = getUpperCaseIndex(info);
        if (index >= 0) {
            info = info.substring(index);
        }
        return info;
    }

    /**
     * 获取传入字符串的第一个大写字母所在的index
     *
     * @param info 待检测字符串
     * @return index
     */
    private static int getUpperCaseIndex(String info) {
        int length = info.length();
        int index = -1;
        for (int i = 0; i < length; i++) {
            char letter = info.charAt(i);
            if (Character.isUpperCase(letter)){
                index = i;
                break;
            }
        }
        return index;
    }

    public interface ITimeCoast {
        void functionRun();
    }
}
