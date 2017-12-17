package com.zemian.adocblog.app.support;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A simple command line arguments and options parser in the format of "--optionName=optionValue" or
 * "-optionName=optionValue".
 *
 * The optionName can be one ore more characters. The equals sign must be used when providing optionValue;
 * else it will be treated as program arguments instead! If you ommit the "=optionValue" part, then it
 * will default to "=true" for that optionName. The optionValue must be a single string. Any other argument
 * that are not part of options are store in a new args list.
 *
 * The constructor will immediately parse the input arguments, and it should not throw any exceptions. In case
 * user did not use correct format, by above rule, it will go into the argument list instead. We purposely made
 * this class simple, and let end user to perform any validation they need for their program.
 *
 * Usage example:
 *   Hello --help
 *   Hello --num=101 --verbose one two three
 * ----
 * public class Hello {}
 *     public static void main(String[] args) {
 *         CmdOpts opts = new CmdOpts(args);
 *         if (opts.hasOpt("help"))
 *           System.out.println("You need help");
 *         int num = opts.getIntOpt("num", 0);
 *         System.out.println("Using num=" + num);
 *         System.out.println("Using new args=" + opts.getArgs());
 *     }
 * }
 * ----
 *
 * Created by Zemian Deng 2017.
 */
public class CmdOpts {
    private Map<String, String> opts = new HashMap<>();
    private List<String> args = new ArrayList<>();

    public CmdOpts(String[] fullArguments) {
        parse(fullArguments);
    }

    private void parse(String[] fullArguments) {
        for (String arg : fullArguments) {
            if (arg.startsWith("--")) {
                addOpt(arg.substring(2));
            } else if (arg.startsWith("-")) {
                addOpt(arg.substring(1));
            } else {
                args.add(arg);
            }

        }
    }

    private void addOpt(String option) {
        if (option != null && !"".equals(option)) {
            String[] options = StringUtils.split(option, "=", 2);
            String optionName = options[0];
            String optionValue = "true";
            if (options.length == 2) {
                optionValue = options[1];
            }
            opts.put(optionName, optionValue);
        }
    }

    public int getOptsSize() {
        return opts.size();
    }

    public Set<String> getOptsNames() {
        return opts.keySet();
    }

    public boolean hasOpt(String name) {
        return opts.containsKey(name);
    }

    public String getOpt(String name) {
        return opts.get(name);
    }

    public String getOpt(String name, String def) {
        String result = getOpt(name);
        if (result == null)
            result = def;
        return result;
    }

    public boolean getBooleanOpt(String name) {
        return Boolean.parseBoolean(getOpt(name));
    }

    public boolean getBooleanOpt(String name, boolean def) {
        String result = getOpt(name);
        if (result == null)
            return def;
        return Boolean.parseBoolean(result);
    }

    public int getIntOpt(String name) {
        return Integer.parseInt(getOpt(name));
    }

    public int getIntOpt(String name, int def) {
        String result = getOpt(name);
        if (result == null)
            return def;
        return Integer.parseInt(result);
    }

    public long getLongOpt(String name) {
        return Long.parseLong(getOpt(name));
    }

    public long getLongOpt(String name, long def) {
        String result = getOpt(name);
        if (result == null)
            return def;
        return Long.parseLong(result);
    }

    public int getArgsSize() {
        return args.size();
    }
    public List<String> getArgs() {
        return args;
    }
    public String[] getArgsArray() {
        return args.toArray(new String[args.size()]);
    }

    public String getArgOrError(int index, String errorIfMissing) {
        if (index >= args.size() || index < 0) {
            throw new IllegalArgumentException(errorIfMissing);
        }
        return args.get(index);
    }

    public String getArg(int index) {
        return args.get(index);
    }
}
