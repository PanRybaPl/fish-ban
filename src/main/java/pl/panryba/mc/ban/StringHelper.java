package pl.panryba.mc.ban;

/**
 * @author PanRyba.pl
 */
public class StringHelper {
    public static String join(String separator, String[] parts, int index, int length) {
        StringBuilder sb = new StringBuilder();

        boolean first = true;
        for(int i = index; i < index + length; ++i) {
            if(first) {
                first = false;
            } else {
                sb.append(separator);
            }

            sb.append(parts[i]);
        }

        return sb.toString();
    }

    public static String join(String s, String[] args, int index) {
        return StringHelper.join(s, args, index, args.length - index);
    }
}
