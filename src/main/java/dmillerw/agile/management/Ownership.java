package dmillerw.agile.management;

import com.google.common.collect.Sets;
import dmillerw.agile.serialize.Serializer;
import org.pircbotx.User;

import java.util.Set;

public class Ownership {

    public static Set<String> allowedUsers = Sets.newHashSet();
    public static Set<String> allowedMasks = Sets.newHashSet();

    public static void giveOwnership(User user) {
        allowedUsers.add(user.getNick());
        allowedMasks.add(user.getHostmask());
        Serializer.saveOwnership();
    }

    public static void removeOwnership(User user) {
        allowedUsers.remove(user.getNick());
        allowedMasks.remove(user.getHostmask());
        Serializer.saveOwnership();
    }

    public static boolean hasOwnership(User user) {
        return allowedMasks.contains(user.getHostmask()) || allowedMasks.contains(user.getNick());
    }
}
